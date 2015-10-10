package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class NewFileModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Added)
        unifiedDiff.setMode(extractNewFileMode(line))
    }
    private static String extractNewFileMode(String newFileModeLine) {
        StrategyHelper.extractDataFromHeaderLine(newFileModeLine, LineExpression.NEW_FILE_MODE, 1)
    }
}
