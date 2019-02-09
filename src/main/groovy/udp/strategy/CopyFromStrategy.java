package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class CopyFromStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setFileStatus(UnifiedDiff.FileStatus.Copied);
        ((UnifiedDiff) model).setFromFile(extractCopyFrom(line));
    }

    private static String extractCopyFrom(String copyFromLine) {
        return StrategyHelper.extractDataFromLine(copyFromLine, LineExpression.getCOPY_FROM(), 1);
    }

}
