package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class RenameFromStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Renamed)
        unifiedDiff.setFromFile(extractRenameFrom(line))
    }
    private static String extractRenameFrom(String renameFromLine) {
        StrategyHelper.extractDataFromLine(renameFromLine, LineExpression.RENAME_FROM, 1)
    }
}
