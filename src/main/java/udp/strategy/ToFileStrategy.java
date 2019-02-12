package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class ToFileStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setToFile(extractToFile(line));
    }

    private static String extractToFile(String toFileLine) {
        return StrategyHelper.extractDataFromLine(toFileLine, LineExpression.TO_FILE, 2);
    }

}
