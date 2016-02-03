package udp.strategy

import udp.UnifiedDiff

interface LineHandlingStrategy {
    // TODO: The dependency on UnifiedDiff might have to go
    void handle(String line, UnifiedDiff unifiedDiff)
}
