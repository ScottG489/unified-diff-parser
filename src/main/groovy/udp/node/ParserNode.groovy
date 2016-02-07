package udp.node

import rdglp.strategy.LineHandlingStrategy

interface ParserNode {
    boolean isApplicable(String line)
    LineHandlingStrategy getLineHandlingStrategy();
    Set<ParserNode> getNextNodes();
    void setNextNodes()
}
