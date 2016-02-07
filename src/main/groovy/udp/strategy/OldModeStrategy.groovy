package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import udp.LineExpression
import udp.UnifiedDiff

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
