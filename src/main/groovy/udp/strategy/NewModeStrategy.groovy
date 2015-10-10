package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class NewModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setMode(extractNewMode(line))
    }
    private static String extractNewMode(String newModeLine) {
        StrategyHelper.extractDataFromHeaderLine(newModeLine, LineExpression.NEW_MODE, 1)
    }
}
