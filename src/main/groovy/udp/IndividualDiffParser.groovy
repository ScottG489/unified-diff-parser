package udp

import java.util.regex.Matcher
import java.util.regex.Pattern

class IndividualDiffParser {
    private UnifiedDiff unifiedDiff

    IndividualDiffParser(String rawDiff) {
        unifiedDiff = new UnifiedDiff(rawDiff)
        // TODO: Initialize as modified, will be overridden if otherwise.
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Modified)
    }

    // TODO: Inconsistency between added/deleted text and binary files:
    // TODO:        Text: To/From file respectively is '/dev/null' along with file being added/deleted
    // TODO:        Binary: To/From file respectively are both the same
    UnifiedDiff parse() {
        unifiedDiff.setDiffHeader(extractDiffHeader(unifiedDiff.getRawDiff()))
        Iterator<String> lineIter = unifiedDiff.getDiffHeader().readLines().iterator()
        while (lineIter.hasNext()) {
            String diffGitLine = lineIter.next()
            if (!isDiffGitLine(diffGitLine)) {
                // First line isn't git diff line. Malformed.
            }
            String extendedHeaderLine = lineIter.next()
            if (isNewFileModeLine(extendedHeaderLine)) {
                unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Added)
                unifiedDiff.setMode(extractNewFileMode(extendedHeaderLine))
            } else if (isDeletedFileModeLine(extendedHeaderLine)) {
                unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Removed)
                unifiedDiff.setMode(extractDeletedFileMode(extendedHeaderLine))
            } else if (isOldModeLine(extendedHeaderLine)) {
                if (isNewModeLine(lineIter.next())) {

                }  else {
                    // malformed. next line needs to be new mode line
                }
            } else if (isSimilarityIndexLine(extendedHeaderLine)) {
                String copyOrRenameFromLine = lineIter.next()
                if (isCopyFromLine(copyOrRenameFromLine)) {
                    if (isCopyToLine(lineIter.next())) {

                    } else {
                        // malformed. next line needs to be copy to line
                    }
                } else if (isRenameFromLine(copyOrRenameFromLine)) {
                    if (isRenameToLine(lineIter.next())) {

                    } else {
                        // malformed. next line needs to be rename to line
                    }
                } else {
                    // malformed, following a similarity index line needs to be copy from or rename from line
                }
            } else if (isDissimilarityIndexLine(extendedHeaderLine)) {

            }
            if (isIndexLine(lineIter.next())) {
                // handle index line here then...

                if (!lineIter.hasNext()) {
                    // Empty file so we're done parsing header
                    break
                }
            } else {
                // malformed? i believe there's always a index line
                // I think it's required but git code suggests it's possible for it not to be.
                // Need to investigate git source code further
                // If not then this and below section needs to be refactored accordingly.
            }
            String postHeaderLine = lineIter.next()
            if (isFromFileLine(postHeaderLine)) {
                unifiedDiff.setFromFile(extractFromFile(postHeaderLine))
                String toFileLine = lineIter.next()
                if (isToFileLine(toFileLine)) {
                    unifiedDiff.setToFile(extractToFile(toFileLine))
                }  else {
                    // malformed. next line needs to be to file line
                }
            } else if (isBinaryFileLine(postHeaderLine)) {
                unifiedDiff.setIsBinary(true)
                unifiedDiff.setFromFile(extractFromFileFromBinaryLine(postHeaderLine))
                unifiedDiff.setToFile(extractToFileFromBinaryLine(postHeaderLine))
            } else if (isGitBinaryFileLine(postHeaderLine)) {
                unifiedDiff.setIsBinary(true)
            }
        }
        // TODO: Get the to and from file names from the first line as a last ditch effort
        if (unifiedDiff.getFromFile() == null) {
            unifiedDiff.setFromFile(extractFromFileFromFirstLine())
        }
        if (unifiedDiff.getToFile() == null) {
            unifiedDiff.setToFile(extractToFileFromFirstLine())
        }
        unifiedDiff.setDiffBody(extractDiffBody(unifiedDiff.getRawDiff()))
        return unifiedDiff
    }

    private static boolean isDiffGitLine(String diffLine) {
        return diffLine.matches(LineExpression.DIFF_GIT)
    }

    private static boolean isOldModeLine(String diffLine) {
        return diffLine.matches(LineExpression.OLD_MODE)
    }

    private static boolean isNewModeLine(String diffLine) {
        return diffLine.matches(LineExpression.NEW_MODE)
    }

    private static boolean isNewFileModeLine(String diffLine) {
        return diffLine.matches(LineExpression.NEW_FILE_MODE)
    }

    private static boolean isDeletedFileModeLine(String diffLine) {
        return diffLine.matches(LineExpression.DELETED_FILE_MODE)
    }

    private static boolean isSimilarityIndexLine(String diffLine) {
        return diffLine.matches(LineExpression.SIMILARITY_INDEX)
    }

    private static boolean isCopyFromLine(String diffLine) {
        return diffLine.matches(LineExpression.COPY_FROM)
    }

    private static boolean isCopyToLine(String diffLine) {
        return diffLine.matches(LineExpression.COPY_TO)
    }

    private static boolean isRenameFromLine(String diffLine) {
        return diffLine.matches(LineExpression.RENAME_FROM)
    }

    private static boolean isRenameToLine(String diffLine) {
        return diffLine.matches(LineExpression.RENAME_TO)
    }

    private static boolean isDissimilarityIndexLine(String diffLine) {
        return diffLine.matches(LineExpression.DISSIMILARITY_INDEX)
    }

    private static boolean isIndexLine(String diffLine) {
        return diffLine.matches(LineExpression.INDEX)
    }

    static boolean isBinaryFileLine(String diffLine) {
        return diffLine.matches(LineExpression.BINARY)
    }

    static boolean isGitBinaryFileLine(String diffLine) {
        return diffLine.matches(LineExpression.GIT_BINARY)
    }

    private static boolean isFromFileLine(String diffLine) {
        return diffLine.matches(LineExpression.FROM_FILE)
    }

    private static boolean isToFileLine(String diffLine) {
        return diffLine.matches(LineExpression.TO_FILE)
    }

    private static String extractDataFromHeaderLine(
            String headerLine,
            Pattern headerLineExpression,
            int groupNumber) {
        Matcher m = headerLineExpression.matcher(headerLine)
        m.find()
        String w = ""
        return m.group(groupNumber)
    }

    private String extractFromFileFromFirstLine() {
        Matcher m = LineExpression.DIFF_GIT.matcher(unifiedDiff.getDiffHeader())
        m.find()
        return m.group(1)
    }

    private String extractToFileFromFirstLine() {
        Matcher m = LineExpression.DIFF_GIT.matcher(unifiedDiff.getDiffHeader())
        m.find()
        return m.group(2)
    }

    private static String extractNewFileMode(String newFileModeLine) {
        extractDataFromHeaderLine(newFileModeLine, LineExpression.NEW_FILE_MODE, 1)
    }

    static String extractDeletedFileMode(String deletedFileModeLine) {
        return extractDataFromHeaderLine(deletedFileModeLine, LineExpression.DELETED_FILE_MODE, 1)
    }

    private static String extractFromFile(String fromFileLine) {
        return extractDataFromHeaderLine(fromFileLine, LineExpression.FROM_FILE, 2)
    }

    private static String extractToFile(String toFileLine) {
        return extractDataFromHeaderLine(toFileLine, LineExpression.TO_FILE, 2)
    }

    private static String extractFromFileFromBinaryLine(String binaryFileLine) {
        return extractDataFromHeaderLine(binaryFileLine, LineExpression.BINARY, 2)
    }

    private static String extractToFileFromBinaryLine(String binaryFileLine)  {
        return extractDataFromHeaderLine(binaryFileLine, LineExpression.BINARY, 4)
    }

    private static String extractDiffBody(String rawDiff) {
        String expression = '\n@@'
        Pattern p = Pattern.compile(expression)
        Matcher m = p.matcher(rawDiff)
        if (m.find()) {
            rawDiff.substring(m.start() + 1)
        } else {
            // Is either a binary or empty file so there's no body
            return ""
        }
    }

    private static String extractDiffHeader(String rawDiff) {
        String expression = '\n@@'
        Pattern p = Pattern.compile(expression)
        Matcher m = p.matcher(rawDiff)
        if (m.find()) {
            // + 1 to get newline
            return rawDiff.substring(0, m.start() + 1)
        } else {
            // Is either a binary or empty file so it's all a header
            return rawDiff
        }
    }

    private static class LineExpression {
        // Always first line
        static final Pattern DIFF_GIT = Pattern.compile("diff --git a/(.*) b/(.*)")
        // New and deleted are mutually exclusive and exclusive from old and new mode below
        static final Pattern NEW_FILE_MODE = Pattern.compile("new file mode (.*)")
        static final Pattern DELETED_FILE_MODE = Pattern.compile("deleted file mode (.*)")
        // Modified file mode. Should always go together.
        static final Pattern OLD_MODE = Pattern.compile("old mode (.*)")
        static final Pattern NEW_MODE = Pattern.compile("new mode (.*)")
        // Displayed with copy and rename
        static final Pattern SIMILARITY_INDEX = Pattern.compile("similarity index (.*)")
        // Both together. Exclusive from rename
        static final Pattern COPY_FROM = Pattern.compile("copy from (.*)")
        static final Pattern COPY_TO = Pattern.compile("copy to (.*)")
        // Both together. Exclusive from copy
        static final Pattern RENAME_FROM = Pattern.compile("rename from (.*)")
        static final Pattern RENAME_TO = Pattern.compile("rename to (.*)")
        // Not shown with copy or rename
        static final Pattern DISSIMILARITY_INDEX = Pattern.compile("dissimilarity index (.*)")
        static final Pattern INDEX = Pattern.compile("index (.*)\\.\\.(.*) *(.*)")
        // TODO: Not convinced these binary file name patters are 100% flawless
        static final Pattern BINARY = Pattern.compile("Binary files (a/)*(.*) and (b/)*(.*) differ")
        // TODO: This may be mercurial specific?
        static final Pattern GIT_BINARY = Pattern.compile("GIT binary patch")
        /*
        * "The index line includes the SHA-1 checksum before and after the
        * change. The <mode> is included if the file mode does not change;
        * otherwise, separate lines indicate the old and the new mode."
        */
        static final Pattern FROM_FILE = Pattern.compile("--- (a/)*(.*)")
        static final Pattern TO_FILE = Pattern.compile("\\+\\+\\+ (b/)*(.*)")
    }
}

