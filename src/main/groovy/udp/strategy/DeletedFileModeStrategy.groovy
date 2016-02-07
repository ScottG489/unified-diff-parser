package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.LineExpression
import udp.UnifiedDiff

class DeletedFileModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setFileStatus(UnifiedDiff.FileStatus.Removed)
        model.setMode(extractDeletedFileMode(line))
    }

    static String extractDeletedFileMode(String deletedFileModeLine) {
        return StrategyHelper.extractDataFromLine(deletedFileModeLine, LineExpression.DELETED_FILE_MODE, 1)
    }
}
