package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class RenameFromStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setFileStatus(UnifiedDiff.FileStatus.Renamed)
        model.setFromFile(extractRenameFrom(line))
    }
    private static String extractRenameFrom(String renameFromLine) {
        StrategyHelper.extractDataFromLine(renameFromLine, LineExpression.RENAME_FROM, 1)
    }
}
