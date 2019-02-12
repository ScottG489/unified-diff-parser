package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class NewModeStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setMode(extractNewMode(line));
    }

    private static String extractNewMode(String newModeLine) {
        return StrategyHelper.extractDataFromLine(newModeLine, LineExpression.NEW_MODE, 1);
    }

}
