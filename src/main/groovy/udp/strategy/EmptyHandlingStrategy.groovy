package udp.strategy

import udp.UnifiedDiff
import udp.strategy.LineHandlingStrategy

class EmptyHandlingStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {

    }
}
