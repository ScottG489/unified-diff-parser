package udp

import udp.node.GenericParserNode
import udp.node.ParserNode
import udp.strategy.*

import java.util.regex.Matcher

class IndividualDiffParser {
    private UnifiedDiff unifiedDiff

    IndividualDiffParser(String rawDiff) {
        unifiedDiff = new UnifiedDiff(rawDiff)
        // TODO: Initialize as modified, will be overridden if otherwise.
        // TODO: Do we want to consider mode changes as modified?
        // TODO: This will likely be set incorrectly for certain binary file statuses
        // TODO:    since we don't currently have detection on all of them for binaries
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Modified)
    }

    void parseFirstPass2(Iterator<String> lineIter, Set<ParserNode> nextNodes) {
        // TODO: Find a more succinct way of doing this
        if (!lineIter.hasNext() || (nextNodes == null || nextNodes.isEmpty())) return
        String line = lineIter.next()
        for (ParserNode node : nextNodes) {
            if (node.isApplicable(line)) {
                LineHandlingStrategy lineHandler = node.getLineHandlingStrategy()
                lineHandler.handle(line, unifiedDiff)
                parseFirstPass2(lineIter, node.getNextNodes())
                return
            }
        }
        throw new Exception("Malformed patch. Line was not expected: ${line}")
    }

    void parseFirstPass() {
        Iterator<String> lineIter = unifiedDiff.getRawDiff().readLines().iterator()
        // TODO: Think of a better way to name this or rework the call structure. This is
        // TODO:    currently needed because it's called recursively.
        parseFirstPass2(lineIter, [getNodeTree()].toSet())
    }

    // TODO: Inconsistency between added/deleted text and binary files:
    // TODO:        Text: To/From file respectively is '/dev/null' along with file being added/deleted
    // TODO:        Binary: To/From file respectively are both the same
    UnifiedDiff parse() {
        parseFirstPass()
        // TODO: Get the to and from file names from the first line as a last ditch effort
        // TODO: This is 'diff' specific and may be something preventing this from becoming a
        // TODO:    general purpose line parsing util.
        if (unifiedDiff.getFromFile() == null) {
            unifiedDiff.setFromFile(extractFromFileFromFirstLine())
        }
        if (unifiedDiff.getToFile() == null) {
            unifiedDiff.setToFile(extractToFileFromFirstLine())
        }
        return unifiedDiff
    }

    private String extractFromFileFromFirstLine() {
        Matcher m = LineExpression.DIFF_GIT.matcher(unifiedDiff.getRawDiff())
        m.find()
        return m.group(1)
    }

    private String extractToFileFromFirstLine() {
        Matcher m = LineExpression.DIFF_GIT.matcher(unifiedDiff.getRawDiff())
        m.find()
        return m.group(2)
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
                [indexNode, similarityIndexNode].toSet()
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

