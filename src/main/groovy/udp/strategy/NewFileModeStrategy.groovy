package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class NewFileModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setFileStatus(UnifiedDiff.FileStatus.Added)
        model.setMode(extractNewFileMode(line))
    }
    private static String extractNewFileMode(String newFileModeLine) {
        StrategyHelper.extractDataFromLine(newFileModeLine, LineExpression.NEW_FILE_MODE, 1)
    }
}
