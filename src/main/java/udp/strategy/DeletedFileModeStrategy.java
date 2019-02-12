package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class DeletedFileModeStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setFileStatus(UnifiedDiff.FileStatus.Removed);
        ((UnifiedDiff) model).setMode(extractDeletedFileMode(line));
    }

    public static String extractDeletedFileMode(String deletedFileModeLine) {
        return StrategyHelper.extractDataFromLine(deletedFileModeLine, LineExpression.DELETED_FILE_MODE, 1);
    }

}
