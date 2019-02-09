package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.parse.LineExpression
import udp.parse.UnifiedDiff

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
