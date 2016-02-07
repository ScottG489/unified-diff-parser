package udp

import rdglp.node.ParserNode

abstract class LineParser {
    protected ParserNode firstNode

    LineParser(ParserNode firstNode) {
        this.firstNode = firstNode
    }

    abstract Object parse(String lineParsable)
}