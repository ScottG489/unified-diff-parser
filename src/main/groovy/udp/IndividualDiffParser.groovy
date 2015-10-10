package udp

import udp.node.GenericParserNode
import udp.node.ParserNode
import udp.strategy.*

import java.util.regex.Matcher
import java.util.regex.Pattern

class IndividualDiffParser {
    private UnifiedDiff unifiedDiff

    IndividualDiffParser(String rawDiff) {
        unifiedDiff = new UnifiedDiff(rawDiff)
        // TODO: Initialize as modified, will be overridden if otherwise.
        // TODO: Do we want to consider renames, mode changes, etc. as modified?
        // Solution to this is likely different diff types (classes) or at least
        // add rename/copy file status
        // TODO: This will likely be set incorrectly for certain binary file statuses
        // TODO:    since we don't currently have detection on all of them for binaries
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Modified)
    }

    // TODO: There's no reason this or parseHeader() need to return a UnifiedDiff. Can be void
    UnifiedDiff parseHeader2(Iterator<String> lineIter, Set<ParserNode> nextNodes) {
        if (!lineIter.hasNext() || (nextNodes == null || nextNodes.isEmpty())) return
        String line = lineIter.next()
        for (ParserNode node : nextNodes) {
            if (node.isApplicable(line)) {
                LineHandlingStrategy lineHandler = node.getLineHandlingStrategy()
                lineHandler.handle(line, unifiedDiff)
                return parseHeader2(lineIter, node.getNextNodes())
            }
        }
        throw new Exception("Malformed. Didn't match any expected nodes")
    }

    UnifiedDiff parseHeader() {
        Iterator<String> lineIter = unifiedDiff.getDiffHeader().readLines().iterator()
        return parseHeader2(lineIter, [getNodeTree()].toSet())
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

    static ParserNode getNodeTree() {
        ParserNode binaryFilesNode = new GenericParserNode(
                "Binary files (a/)*(.*) and (b/)*(.*) differ",
                new BinaryStrategy(),
                [].toSet()
        )
        ParserNode gitBinaryNode = new GenericParserNode(
                "GIT binary patch",
                new GitBinaryStrategy(),
                [].toSet()
        )
        ParserNode toFileNode = new GenericParserNode(
                "\\+\\+\\+ (b/)*(.*)",
                new ToFileStrategy(),
                [].toSet()
        )
        ParserNode fromFileNode = new GenericParserNode(
                "--- (a/)*(.*)",
                new FromFileStrategy(),
                [toFileNode].toSet()
        )
        ParserNode indexNode = new GenericParserNode(
                "index (.*)\\.\\.([^ ]*) *(.*)",
                new IndexStrategy(),
                [fromFileNode, binaryFilesNode, gitBinaryNode].toSet()
        )
        ParserNode copyToNode = new GenericParserNode(
                "copy to (.*)",
                new CopyToStrategy(),
                [indexNode].toSet()
        )
        ParserNode copyFromNode = new GenericParserNode(
                "copy from (.*)",
                new CopyFromStrategy(),
                [copyToNode].toSet()
        )
        ParserNode renameToNode = new GenericParserNode(
                "rename to (.*)",
                new RenameToStrategy(),
                [indexNode].toSet()
        )
        ParserNode renameFromNode = new GenericParserNode(
                "rename from (.*)",
                new RenameFromStrategy(),
                [renameToNode].toSet()
        )
        ParserNode similarityIndexNode = new GenericParserNode(
                "similarity index (.*)",
                new SimilarityIndexStrategy(),
                [copyFromNode, renameFromNode].toSet()
        )
        ParserNode newModeNode = new GenericParserNode(
                "new mode (.*)",
                new NewModeStrategy(),
                [indexNode].toSet()
        )
        ParserNode oldModeNode = new GenericParserNode(
                "old mode (.*)",
                new OldModeStrategy(),
                [newModeNode].toSet()
        )
        ParserNode deletedFileNode = new GenericParserNode(
                "deleted file mode (.*)",
                new DeletedFileModeStrategy(),
                [indexNode].toSet()
        )
        ParserNode newFileModeNode = new GenericParserNode(
                "new file mode (.*)",
                new NewFileModeStrategy(),
                [indexNode].toSet()
        )
        ParserNode diffGitNode = new GenericParserNode(
                "diff --git a/(.*) b/(.*)",
                new EmptyHandlingStrategy(),
                [newFileModeNode, deletedFileNode, indexNode, oldModeNode, similarityIndexNode].toSet()
        )

        return diffGitNode
    }
}

