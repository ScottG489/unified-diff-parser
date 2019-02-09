package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class RenameToStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setToFile(extractRenameTo(line));
    }

    private static String extractRenameTo(String renameToLine) {
        return StrategyHelper.extractDataFromLine(renameToLine, LineExpression.getRENAME_TO(), 1);
    }

}
