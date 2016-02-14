package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.parse.LineExpression
import udp.parse.UnifiedDiff

class CopyToStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setToFile(extractCopyTo(line))
    }
    private static String extractCopyTo(String copyToLine) {
        StrategyHelper.extractDataFromLine(copyToLine, LineExpression.COPY_TO, 1)
    }
}
