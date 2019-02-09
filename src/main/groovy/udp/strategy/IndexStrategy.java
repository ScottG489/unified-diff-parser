package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class IndexStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setChecksumBefore(extractChecksumBefore(line));
        ((UnifiedDiff) model).setChecksumAfter(extractChecksumAfter(line));
        // TODO: Only try to get the mode here if it hasn't changed
        // Will a index line as the second line always be a modified file?
        String mode = extractMode(line);
        if (!mode.isEmpty()) {
            ((UnifiedDiff) model).setMode(mode);
        }

    }

    private static String extractChecksumBefore(String indexLine) {
        return StrategyHelper.extractDataFromLine(indexLine, LineExpression.INDEX, 1);
    }

    private static String extractChecksumAfter(String indexLine) {
        return StrategyHelper.extractDataFromLine(indexLine, LineExpression.INDEX, 2);
    }

    private static String extractMode(String indexLine) {
        return StrategyHelper.extractDataFromLine(indexLine, LineExpression.INDEX, 3);
    }

}
