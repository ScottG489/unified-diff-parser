package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class ToFileStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setToFile(extractToFile(line));
    }

    private static String extractToFile(String toFileLine) {
        return StrategyHelper.extractDataFromLine(toFileLine, LineExpression.getTO_FILE(), 2);
    }

}
