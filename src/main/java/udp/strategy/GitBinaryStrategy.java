package udp.strategy;

import rdglp.strategy.LineHandlingStrategy;
import udp.parse.UnifiedDiff;

public class GitBinaryStrategy implements LineHandlingStrategy {
    @Override
    public void handle(String line, Object model) {
        ((UnifiedDiff) model).setIsBinary(true);
    }

}
