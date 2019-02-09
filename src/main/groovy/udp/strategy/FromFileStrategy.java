package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class FromFileStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setFromFile(extractFromFile(line));
    }

    private static String extractFromFile(String fromFileLine) {
        return StrategyHelper.extractDataFromLine(fromFileLine, LineExpression.getFROM_FILE(), 2);
    }

}
