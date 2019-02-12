package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class RenameFromStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setFileStatus(UnifiedDiff.FileStatus.Renamed);
        ((UnifiedDiff) model).setFromFile(extractRenameFrom(line));
    }

    private static String extractRenameFrom(String renameFromLine) {
        return StrategyHelper.extractDataFromLine(renameFromLine, LineExpression.RENAME_FROM, 1);
    }

}
