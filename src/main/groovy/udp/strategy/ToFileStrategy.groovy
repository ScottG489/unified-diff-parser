package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class ToFileStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setToFile(extractToFile(line))
    }
    private static String extractToFile(String toFileLine) {
        return StrategyHelper.extractDataFromLine(toFileLine, LineExpression.TO_FILE, 2)
    }
}
