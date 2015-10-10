package udp.node

import udp.strategy.LineHandlingStrategy

interface ParserNode {
    boolean isApplicable(String line)
    LineHandlingStrategy getLineHandlingStrategy();
    Set<ParserNode> getNextNodes();
    void setNextNodes()
}
