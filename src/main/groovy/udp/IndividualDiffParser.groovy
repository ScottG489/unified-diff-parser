package udp

import rdglp.node.ParserNode
import rdglp.strategy.LineHandlingStrategy

import java.util.regex.Matcher

class IndividualDiffParser extends LineParser {
    private UnifiedDiff unifiedDiff

    IndividualDiffParser(ParserNode firstNode) {
        super(firstNode)
    }

    void parseWithNodeTree(Iterator<String> lineIter, Set<ParserNode> nextNodes) {
        // TODO: Find a more succinct way of doing this
        if (!lineIter.hasNext() || (nextNodes == null || nextNodes.isEmpty())) return
        String line = lineIter.next()
        for (ParserNode node : nextNodes) {
            if (node.isApplicable(line)) {
                LineHandlingStrategy lineHandler = node.getLineHandlingStrategy()
                lineHandler.handle(line, unifiedDiff)
                parseWithNodeTree(lineIter, node.getNextNodes())
                return
            }
        }
        throw new Exception("Malformed patch. Line was not expected: ${line}")
    }

    // TODO: Inconsistency between added/deleted text and binary files:
    // TODO:        Text: To/From file respectively is '/dev/null' along with file being added/deleted
    // TODO:        Binary: To/From file respectively are both the same
    UnifiedDiff parse(String lineParsable) {
        unifiedDiff = new UnifiedDiff(lineParsable)
        // TODO: Initialize as modified, will be overridden if otherwise.
        // TODO: Do we want to consider mode changes as modified?
        // TODO: This will likely be set incorrectly for certain binary file statuses
        // TODO:    since we don't currently have detection on all of them for binaries
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Modified)
        Iterator<String> lineIter = lineParsable.readLines().iterator()
        // TODO: Think of a better way to name this or rework the call structure. This is
        // TODO:    currently needed because it's called recursively.
        parseWithNodeTree(lineIter, [getNodeTree()].toSet())


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

    ParserNode getNodeTree() {
        return firstNode
    }
}

