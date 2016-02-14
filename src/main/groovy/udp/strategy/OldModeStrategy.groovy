package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.parse.LineExpression
import udp.parse.UnifiedDiff

class OldModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setOldMode(extractOldMode(line))
    }
    private static String extractOldMode(String oldModeLine) {
        StrategyHelper.extractDataFromLine(oldModeLine, LineExpression.OLD_MODE, 1)
    }
}
