package udp.node

import udp.strategy.LineHandlingStrategy

import java.util.regex.Pattern

class GenericParserNode implements ParserNode {
    private Pattern applicabilityPattern
    private LineHandlingStrategy lineHandlingStrategy
    private Set<ParserNode> possibleNextNodes

    GenericParserNode(
            String applicabilityPattern,
            LineHandlingStrategy lineHandlingStrategy,
            Set<ParserNode> possibleNextNodes) {
        this.applicabilityPattern = Pattern.compile(applicabilityPattern)
        this.lineHandlingStrategy = lineHandlingStrategy
        this.possibleNextNodes = possibleNextNodes
    }
    @Override
    boolean isApplicable(String line) {
        return line.matches(applicabilityPattern)
    }

    @Override
    LineHandlingStrategy getLineHandlingStrategy() {
        return lineHandlingStrategy
    }

    @Override
    Set<ParserNode> getNextNodes() {
        return possibleNextNodes
    }

    @Override
    void setNextNodes() {

    }
}
