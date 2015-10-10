package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class BinaryStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setIsBinary(true)
        unifiedDiff.setFromFile(extractFromFileFromBinaryLine(line))
        unifiedDiff.setToFile(extractToFileFromBinaryLine(line))
    }
    private static String extractFromFileFromBinaryLine(String binaryFileLine) {
        return StrategyHelper.extractDataFromHeaderLine(binaryFileLine, LineExpression.BINARY, 2)
    }
    private static String extractToFileFromBinaryLine(String binaryFileLine)  {
        return StrategyHelper.extractDataFromHeaderLine(binaryFileLine, LineExpression.BINARY, 4)
    }
}
