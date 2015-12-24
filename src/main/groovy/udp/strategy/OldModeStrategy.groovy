package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class OldModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setOldMode(extractOldMode(line))
    }
    private static String extractOldMode(String oldModeLine) {
        StrategyHelper.extractDataFromLine(oldModeLine, LineExpression.OLD_MODE, 1)
    }
}
