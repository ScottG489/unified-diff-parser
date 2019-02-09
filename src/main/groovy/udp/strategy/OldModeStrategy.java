package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class OldModeStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setOldMode(extractOldMode(line));
    }

    private static String extractOldMode(String oldModeLine) {
        return StrategyHelper.extractDataFromLine(oldModeLine, LineExpression.getOLD_MODE(), 1);
    }

}
