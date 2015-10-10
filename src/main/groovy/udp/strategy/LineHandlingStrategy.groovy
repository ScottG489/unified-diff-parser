package udp.strategy

import udp.UnifiedDiff

interface LineHandlingStrategy {
    void handle(String line, UnifiedDiff unifiedDiff)
}
