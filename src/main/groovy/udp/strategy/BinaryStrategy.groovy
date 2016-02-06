package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class BinaryStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setIsBinary(true)
        model.setFromFile(extractFromFileFromBinaryLine(line))
        model.setToFile(extractToFileFromBinaryLine(line))
    }
    private static String extractFromFileFromBinaryLine(String binaryFileLine) {
        return StrategyHelper.extractDataFromLine(binaryFileLine, LineExpression.BINARY, 2)
    }
    private static String extractToFileFromBinaryLine(String binaryFileLine)  {
        return StrategyHelper.extractDataFromLine(binaryFileLine, LineExpression.BINARY, 4)
    }
}
