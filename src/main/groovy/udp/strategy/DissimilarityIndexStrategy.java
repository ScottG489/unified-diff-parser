package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class DissimilarityIndexStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setDissimilarityIndex(extractDissimilarityIndex(line));
    }

    private static String extractDissimilarityIndex(String dissimilarityIndexLine) {
        return StrategyHelper.extractDataFromLine(dissimilarityIndexLine, LineExpression.DISSIMILARITY_INDEX, 1);
    }
}
