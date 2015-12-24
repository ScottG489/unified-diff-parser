package udp.strategy

import udp.LineExpression
import udp.UnifiedDiff

class SimilarityIndexStrategy implements LineHandlingStrategy {
    @Override
    void handle(String line, UnifiedDiff unifiedDiff) {
        unifiedDiff.setSimilarityIndex(extractSimilarityIndex(line))
    }
    private static String extractSimilarityIndex(String similarityIndexLine) {
        StrategyHelper.extractDataFromLine(similarityIndexLine, LineExpression.SIMILARITY_INDEX, 1)
    }
}
