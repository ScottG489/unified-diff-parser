package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class IndexStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setChecksumBefore(extractChecksumBefore(line))
        unifiedDiff.setChecksumAfter(extractChecksumAfter(line))
        // TODO: Only try to get the mode here if it hasn't changed
        // Will a index line as the second line always be a modified file?
        String mode = extractMode(line)
        if (!mode.isEmpty()) {
            unifiedDiff.setMode(mode)
        }
    }
    private static String extractChecksumBefore(String indexLine) {
        StrategyHelper.extractDataFromHeaderLine(indexLine, LineExpression.INDEX, 1)
    }
    private static String extractChecksumAfter(String indexLine) {
        StrategyHelper.extractDataFromHeaderLine(indexLine, LineExpression.INDEX, 2)
    }
    private static String extractMode(String indexLine) {
        StrategyHelper.extractDataFromHeaderLine(indexLine, LineExpression.INDEX, 3)
    }
}
