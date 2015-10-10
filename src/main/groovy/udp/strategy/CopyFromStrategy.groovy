package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class CopyFromStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Copied)
        unifiedDiff.setFromFile(extractCopyFrom(line))
    }
    private static String extractCopyFrom(String copyFromLine) {
        StrategyHelper.extractDataFromHeaderLine(copyFromLine, LineExpression.COPY_FROM, 1)
    }
}
