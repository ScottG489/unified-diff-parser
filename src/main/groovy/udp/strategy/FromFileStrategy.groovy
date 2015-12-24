package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class FromFileStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setFromFile(extractFromFile(line))
    }
    private static String extractFromFile(String fromFileLine) {
        return StrategyHelper.extractDataFromLine(fromFileLine, LineExpression.FROM_FILE, 2)
    }
}
