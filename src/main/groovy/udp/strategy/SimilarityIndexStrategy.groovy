package udp.strategy

import rdglp.strategy.LineHandlingStrategy
import rdglp.strategy.util.StrategyHelper
import udp.parse.LineExpression
import udp.parse.UnifiedDiff

class SimilarityIndexStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, Object model) {
        model = (UnifiedDiff) model;
        model.setSimilarityIndex(extractSimilarityIndex(line))
    }
    private static String extractSimilarityIndex(String similarityIndexLine) {
        StrategyHelper.extractDataFromLine(similarityIndexLine, LineExpression.SIMILARITY_INDEX, 1)
    }
}
