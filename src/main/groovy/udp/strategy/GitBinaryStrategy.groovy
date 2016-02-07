package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import udp.UnifiedDiff

class GitBinaryStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setIsBinary(true)
    }
}
