package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class NewFileModeStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setFileStatus(UnifiedDiff.FileStatus.Added);
        ((UnifiedDiff) model).setMode(extractNewFileMode(line));
    }

    private static String extractNewFileMode(String newFileModeLine) {
        return StrategyHelper.extractDataFromLine(newFileModeLine, LineExpression.getNEW_FILE_MODE(), 1);
    }

}
