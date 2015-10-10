package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class DeletedFileModeStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Removed)
        unifiedDiff.setMode(extractDeletedFileMode(line))
    }

    static String extractDeletedFileMode(String deletedFileModeLine) {
        return StrategyHelper.extractDataFromHeaderLine(deletedFileModeLine, LineExpression.DELETED_FILE_MODE, 1)
    }
}
