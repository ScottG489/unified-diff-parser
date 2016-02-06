package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class CopyFromStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setFileStatus(UnifiedDiff.FileStatus.Copied)
        model.setFromFile(extractCopyFrom(line))
    }
    private static String extractCopyFrom(String copyFromLine) {
        StrategyHelper.extractDataFromLine(copyFromLine, LineExpression.COPY_FROM, 1)
    }
}
