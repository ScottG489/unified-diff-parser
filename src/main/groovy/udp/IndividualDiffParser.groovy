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

    UnifiedDiff parse() {
        unifiedDiff.setDiffHeader(extractDiffHeader(unifiedDiff.getRawDiff()))
        for (String rawDiffLine : unifiedDiff.getDiffHeader().readLines()) {
            if (isNewFileModeLine(rawDiffLine)) {
                unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Added)
                unifiedDiff.setMode(extractNewFileMode(rawDiffLine))
            } else if (isDeletedFileModeLine(rawDiffLine)) {
                unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Removed)
                unifiedDiff.setMode(extractDeletedFileMode(rawDiffLine))
            } else if (isFromFileLine(rawDiffLine)) {
                unifiedDiff.setFromFile(extractFromFile(rawDiffLine))
            } else if (isToFileLine(rawDiffLine)) {
                unifiedDiff.setToFile(extractToFile(rawDiffLine))
            } else if (isBinaryFileLine(rawDiffLine)) {
                unifiedDiff.setIsBinary(true)
                // TODO: Not convinced these binary file name patters are 100% flawless
                unifiedDiff.setFromFile(extractFromFileFromBinaryLine(rawDiffLine))
                unifiedDiff.setToFile(extractToFileFromBinaryLine(rawDiffLine))
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

    private String extractFromFileFromFirstLine() {
        Matcher m = LineExpression.IS_FIRST_LINE.matcher(unifiedDiff.getDiffHeader())
        m.find()
        return m.group(1)
    }

    private String extractToFileFromFirstLine() {
        Matcher m = LineExpression.IS_FIRST_LINE.matcher(unifiedDiff.getDiffHeader())
        m.find()
        return m.group(2)
    }

    private static boolean isNewFileModeLine(String diffLine) {
        return diffLine.matches(LineExpression.IS_NEW_FILE_MODE)
    }

    static String extractNewFileMode(String newFileModeLine) {
        Matcher m = LineExpression.IS_NEW_FILE_MODE.matcher(newFileModeLine)
        m.find()
        return m.group(1)
    }

    private static boolean isDeletedFileModeLine(String diffLine) {
        return diffLine.matches(LineExpression.IS_DELETED_FILE_MODE)
    }

    static String extractDeletedFileMode(String deletedFileModeLine) {
        Matcher m = LineExpression.IS_DELETED_FILE_MODE.matcher(deletedFileModeLine)
        m.find()
        return m.group(1)
    }

    private static boolean isFromFileLine(String diffLine) {
        return diffLine.matches(LineExpression.IS_FROM_FILE)
    }

    private static String extractFromFile(String fromFileLine) {
        Matcher m = LineExpression.IS_FROM_FILE.matcher(fromFileLine)
        m.find()
        return m.group(2)
    }

    private static boolean isToFileLine(String diffLine) {
        return diffLine.matches(LineExpression.IS_TO_FILE)
    }

    private static String extractToFile(String toFileLine) {
        Matcher m = LineExpression.IS_TO_FILE.matcher(toFileLine)
        m.find()
        return m.group(2)
    }

    static boolean isBinaryFileLine(String diffLine) {
        return diffLine.matches(LineExpression.IS_BINARY)
    }

    private static String extractFromFileFromBinaryLine(String binaryFileLine) {
        Matcher m = LineExpression.IS_BINARY.matcher(binaryFileLine)
        m.find()
        return m.group(2)
    }

    private static String extractToFileFromBinaryLine(String binaryFileLine)  {
        Matcher m = LineExpression.IS_BINARY.matcher(binaryFileLine)
        m.find()
        return m.group(4)
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
        static final Pattern IS_NEW_FILE_MODE = Pattern.compile("new file mode (.*)")
        static final Pattern IS_DELETED_FILE_MODE = Pattern.compile("deleted file mode (.*)")
        static final Pattern IS_FROM_FILE = Pattern.compile("--- (a/)*(.*)")
        static final Pattern IS_TO_FILE = Pattern.compile("\\+\\+\\+ (b/)*(.*)")
        static final Pattern IS_BINARY = Pattern.compile("Binary files (a/)*(.*) and (b/)*(.*) differ")
        static final Pattern IS_FIRST_LINE = Pattern.compile("diff --git a/(.*) b/(.*)")
    }
}

