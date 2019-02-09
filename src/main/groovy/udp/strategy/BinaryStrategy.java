package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class BinaryStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setIsBinary(true);
        ((UnifiedDiff) model).setFromFile(extractFromFileFromBinaryLine(line));
        ((UnifiedDiff) model).setToFile(extractToFileFromBinaryLine(line));
    }

    private static String extractFromFileFromBinaryLine(String binaryFileLine) {
        return StrategyHelper.extractDataFromLine(binaryFileLine, LineExpression.getBINARY(), 2);
    }

    private static String extractToFileFromBinaryLine(String binaryFileLine) {
        return StrategyHelper.extractDataFromLine(binaryFileLine, LineExpression.getBINARY(), 4);
    }

}
