package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class CopyToStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setToFile(extractCopyTo(line))
    }
    private static String extractCopyTo(String copyToLine) {
        StrategyHelper.extractDataFromLine(copyToLine, LineExpression.COPY_TO, 1)
    }
}
