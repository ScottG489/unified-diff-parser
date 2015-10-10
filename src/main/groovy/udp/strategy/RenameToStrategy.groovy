package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class RenameToStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setToFile(extractRenameTo(line))
    }
    private static String extractRenameTo(String renameToLine) {
        StrategyHelper.extractDataFromHeaderLine(renameToLine, LineExpression.RENAME_TO, 1)
    }
}
