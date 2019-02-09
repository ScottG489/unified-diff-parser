package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.parse.LineExpression
import udp.parse.UnifiedDiff

class FromFileStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setFromFile(extractFromFile(line))
    }
    private static String extractFromFile(String fromFileLine) {
        return StrategyHelper.extractDataFromLine(fromFileLine, LineExpression.FROM_FILE, 2)
    }
}
