package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class CopyToStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setToFile(extractCopyTo(line));
    }

    private static String extractCopyTo(String copyToLine) {
        return StrategyHelper.extractDataFromLine(copyToLine, LineExpression.COPY_TO, 1);
    }

}
