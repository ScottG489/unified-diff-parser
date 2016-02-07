package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.LineExpression
import udp.UnifiedDiff

class RenameToStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setToFile(extractRenameTo(line))
    }
    private static String extractRenameTo(String renameToLine) {
        StrategyHelper.extractDataFromLine(renameToLine, LineExpression.RENAME_TO, 1)
    }
}
