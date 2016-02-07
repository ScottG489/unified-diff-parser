package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.LineExpression
import udp.UnifiedDiff

class ToFileStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setToFile(extractToFile(line))
    }
    private static String extractToFile(String toFileLine) {
        return StrategyHelper.extractDataFromLine(toFileLine, LineExpression.TO_FILE, 2)
    }
}
