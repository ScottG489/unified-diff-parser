import udp.node.GenericParserNode
import udp.node.ParserNode
import udp.strategy.*

    UnifiedDiff parseHeader2(Iterator<String> lineIter, Set<ParserNode> nextNodes) {
        if (!lineIter.hasNext() || (nextNodes == null || nextNodes.isEmpty())) return
        String line = lineIter.next()
        for (ParserNode node : nextNodes) {
            if (node.isApplicable(line)) {
                LineHandlingStrategy lineHandler = node.getLineHandlingStrategy()
                lineHandler.handle(line, unifiedDiff)
                return parseHeader2(lineIter, node.getNextNodes())
        throw new Exception("Malformed. Didn't match any expected nodes")
    }
    UnifiedDiff parseHeader() {
        Iterator<String> lineIter = unifiedDiff.getDiffHeader().readLines().iterator()
        return parseHeader2(lineIter, [getNodeTree()].toSet())

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