package udp

import java.util.regex.Matcher
import java.util.regex.Pattern

class LegacyIndividualDiffParser {
    private UnifiedDiff unifiedDiff

    LegacyIndividualDiffParser(String rawDiff) {
        unifiedDiff = new UnifiedDiff(rawDiff)
        // TODO: Initialize as modified, will be overridden if otherwise.
        // TODO: Do we want to consider renames, mode changes, etc. as modified?
        // Solution to this is likely different diff types (classes) or at least
        // add rename/copy file status
        // TODO: This will likely be set incorrectly for certain binary file statuses
        // TODO:    since we don't currently have detection on all of them for binaries
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Modified)
    }

    UnifiedDiff parseHeader() {
        Iterator<String> lineIter = unifiedDiff.getDiffHeader().readLines().iterator()
        if (lineIter.hasNext()) {
            // TODO: Pull this up a level outside this function? Especially since
            // TODO:    we might need to use it later for parsing file names
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
                String newModeLine = lineIter.next()
                if (isNewModeLine(newModeLine)) {
                    // TODO: Handle old mode somehow
//                    extractOldMode(extendedHeaderLine)
                    unifiedDiff.setMode(extractNewMode(newModeLine))
                }  else {
                    // malformed. next line needs to be new mode line
                }
            } else if (isSimilarityIndexLine(extendedHeaderLine)) {
                unifiedDiff.setSimilarityIndex(extractSimilarityIndex(extendedHeaderLine))
                String copyOrRenameFromLine = lineIter.next()
                if (isCopyFromLine(copyOrRenameFromLine)) {
                    String copyToLine = lineIter.next()
                    if (isCopyToLine(copyToLine)) {
                        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Copied)
                        unifiedDiff.setFromFile(extractCopyFrom(copyOrRenameFromLine))
                        unifiedDiff.setToFile(extractCopyTo(copyToLine))
                    } else {
                        // malformed. next line needs to be copy to line
                    }
                } else if (isRenameFromLine(copyOrRenameFromLine)) {
                    String renameToLine = lineIter.next()
                    if (isRenameToLine(renameToLine)) {
                        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Renamed)
                        unifiedDiff.setFromFile(extractRenameFrom(copyOrRenameFromLine))
                        unifiedDiff.setToFile(extractRenameTo(renameToLine))
                    } else {
                        // malformed. next line needs to be rename to line
                    }
                } else {
                    // malformed, following a similarity index line needs to be copy from or rename from line
                }
            } else if (isDissimilarityIndexLine(extendedHeaderLine)) {
                // TODO: Handle
            } else if (isIndexLine(extendedHeaderLine)) {
                // handle index line here then...
                unifiedDiff.setChecksumBefore(extractChecksumBefore(extendedHeaderLine))
                unifiedDiff.setChecksumAfter(extractChecksumAfter(extendedHeaderLine))
                // TODO: Only try to get the mode here if it hasn't changed
                // Will a index line as the second line always be a modified file?
                unifiedDiff.setMode(extractMode(extendedHeaderLine))
            } else {
                // Malformed? Should have been one of the above
            }
            if (!lineIter.hasNext()) {
                // End of header
                return unifiedDiff
            }
            String indexLine = lineIter.next()
            if (isIndexLine(indexLine)) {
                // handle index line here then...
                unifiedDiff.setChecksumBefore(extractChecksumBefore(indexLine))
                unifiedDiff.setChecksumAfter(extractChecksumAfter(indexLine))
                // TODO: No mode line as a index in this position indicates a
                // TODO:    state other than modified
            } else if (isGitBinaryFileLine(indexLine)) {
                unifiedDiff.setIsBinary(true)
            }
            if (!lineIter.hasNext()) {
                // No to/from file lines so break
                return unifiedDiff
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
            }
        } else {
            // empty diff, malformed
        }

        return unifiedDiff
    }
    // TODO: Inconsistency between added/deleted text and binary files:
    // TODO:        Text: To/From file respectively is '/dev/null' along with file being added/deleted
    // TODO:        Binary: To/From file respectively are both the same
    // TODO: This should be refactored using 'states' if only for readability
    // TODO: Should 'binary' 'header' lines be considered part of the header? Or are they
    // TODO:    really part of the body? Seems like they are more part of the body
    // TODO:    but since we want to know if the file is binary we parse them as though they are
    // TODO:    part of the header.
    UnifiedDiff parse() {
        unifiedDiff.setDiffHeader(extractDiffHeader(unifiedDiff.getRawDiff()))
        parseHeader()
        // TODO: Get the to and from file names from the first line as a last ditch effort
        if (unifiedDiff.getFromFile() == null) {
            unifiedDiff.setFromFile(extractFromFileFromFirstLine())
        }
        if (unifiedDiff.getToFile() == null) {
            unifiedDiff.setToFile(extractToFileFromFirstLine())
        }
        // TODO: Doesn't get body for binary patches
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

    private static String extractChecksumBefore(String indexLine) {
        extractDataFromHeaderLine(indexLine, LineExpression.INDEX, 1)
    }

    private static String extractChecksumAfter(String indexLine) {
        extractDataFromHeaderLine(indexLine, LineExpression.INDEX, 2)
    }

    private static String extractMode(String indexLine) {
        extractDataFromHeaderLine(indexLine, LineExpression.INDEX, 3)
    }

    private static String extractSimilarityIndex(String similarityIndexLine) {
        extractDataFromHeaderLine(similarityIndexLine, LineExpression.SIMILARITY_INDEX, 1)
    }

    private static String extractCopyFrom(String copyFromLine) {
        extractDataFromHeaderLine(copyFromLine, LineExpression.COPY_FROM, 1)
    }

    private static String extractCopyTo(String copyToLine) {
        extractDataFromHeaderLine(copyToLine, LineExpression.COPY_TO, 1)
    }

    private static String extractRenameFrom(String renameFromLine) {
        extractDataFromHeaderLine(renameFromLine, LineExpression.RENAME_FROM, 1)
    }

    private static String extractRenameTo(String renameToLine) {
        extractDataFromHeaderLine(renameToLine, LineExpression.RENAME_TO, 1)
    }

    private static String extractOldMode(String oldModeLine) {
        extractDataFromHeaderLine(oldModeLine, LineExpression.OLD_MODE, 1)
    }

    private static String extractNewMode(String newModeLine) {
        extractDataFromHeaderLine(newModeLine, LineExpression.NEW_MODE, 1)
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
        // XXX: This is incorrect as the diff body won't match this for binary patches.
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
        // XXX: This is incorrect as the diff body won't match this for binary patches.
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
        /*
        * "The index line includes the SHA-1 checksum before and after the
        * change. The <mode> is included if the file mode does not change;
        * otherwise, separate lines indicate the old and the new mode."
        */
        // In other words the last group will be empty if the mode changed.
        // TODO: Are there always 2 .'s here? Thought I saw something about them being
        // TODO:    used for padding which means that could change.
        static final Pattern INDEX = Pattern.compile("index (.*)\\.\\.([^ ]*) *(.*)")
        // TODO: Not convinced these binary file name patters are 100% flawless
        static final Pattern BINARY = Pattern.compile("Binary files (a/)*(.*) and (b/)*(.*) differ")
        static final Pattern GIT_BINARY = Pattern.compile("GIT binary patch")
        static final Pattern FROM_FILE = Pattern.compile("--- (a/)*(.*)")
        static final Pattern TO_FILE = Pattern.compile("\\+\\+\\+ (b/)*(.*)")
    }
}
