package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.LineExpression
import udp.UnifiedDiff

class NewModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setMode(extractNewMode(line))
    }
    private static String extractNewMode(String newModeLine) {
        StrategyHelper.extractDataFromLine(newModeLine, LineExpression.NEW_MODE, 1)
    }
}
