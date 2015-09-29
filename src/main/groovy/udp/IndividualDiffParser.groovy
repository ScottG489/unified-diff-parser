        // TODO: Do we want to consider renames, mode changes, etc. as modified?
        // Solution to this is likely different diff types (classes) or at least
        // add rename/copy file status
    // TODO: This should be refactored using 'states' if only for readability
                String newModeLine = lineIter.next()
                if (isNewModeLine(newModeLine)) {
                    // TODO: Handle old mode somehow
//                    extractOldMode(extendedHeaderLine)
                    unifiedDiff.setMode(extractNewMode(newModeLine))
                unifiedDiff.setSimilarityIndex(extractSimilarityIndex(extendedHeaderLine))
                    String copyToLine = lineIter.next()
                    if (isCopyToLine(copyToLine)) {
                        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Copied)
                        unifiedDiff.setFromFile(extractCopyFrom(copyOrRenameFromLine))
                        unifiedDiff.setToFile(extractCopyTo(copyToLine))
                    String renameToLine = lineIter.next()
                    if (isRenameToLine(renameToLine)) {
                        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Renamed)
                        unifiedDiff.setFromFile(extractRenameFrom(copyOrRenameFromLine))
                        unifiedDiff.setToFile(extractRenameTo(renameToLine))
                // TODO: Handle
            } else if (isIndexLine(extendedHeaderLine)) {
                unifiedDiff.setChecksumBefore(extractChecksumBefore(extendedHeaderLine))
                unifiedDiff.setChecksumAfter(extractChecksumAfter(extendedHeaderLine))
                // XXX: Only try to get the mode here if it hasn't changed
                // Will a index line as the second line always be a modified file?
                unifiedDiff.setMode(extractMode(extendedHeaderLine))
                // Malformed? Should have been one of the above
            }
            if (!lineIter.hasNext()) {
                // End of header
                break
            }
            String indexLine = lineIter.next()
            if (isIndexLine(indexLine)) {
                // handle index line here then...
                unifiedDiff.setChecksumBefore(extractChecksumBefore(indexLine))
                unifiedDiff.setChecksumAfter(extractChecksumAfter(indexLine))
                // TODO: No mode line as a index in this position indicates a
                // TODO:    state other than modified
            if (!lineIter.hasNext()) {
                // No to/from file lines so break
                break
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

        // In other words the last group will be empty if the mode changed.
        // TODO: Are there alwawys 2 .'s here? Thought I saw something about them being
        // TODO:    used for padding which means that could change.
        static final Pattern INDEX = Pattern.compile("index (.*)\\.\\.([^ ]*) *(.*)")
        // TODO: Not convinced these binary file name patters are 100% flawless
        static final Pattern BINARY = Pattern.compile("Binary files (a/)*(.*) and (b/)*(.*) differ")
        // TODO: This may be mercurial specific?
        static final Pattern GIT_BINARY = Pattern.compile("GIT binary patch")