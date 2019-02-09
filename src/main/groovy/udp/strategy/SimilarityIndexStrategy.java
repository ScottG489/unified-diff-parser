package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import rdglp.strategy.util.StrategyHelper;
import udp.parse.LineExpression;
import udp.parse.UnifiedDiff;

public class SimilarityIndexStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        ((UnifiedDiff) model).setSimilarityIndex(extractSimilarityIndex(line));
    }

    private static String extractSimilarityIndex(String similarityIndexLine) {
        return StrategyHelper.extractDataFromLine(similarityIndexLine, LineExpression.getSIMILARITY_INDEX(), 1);
    }

}
