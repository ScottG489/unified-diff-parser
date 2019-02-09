package udp.parse.util;

import rdglp.config.ConfigParser;
import rdglp.node.ParserNode;
import udp.parse.UnifiedDiffParser;

import java.io.InputStream;

public class UnifiedDiffParserCreator {
    private final ParserNode defaultNodeGraph;

    UnifiedDiffParserCreator() {
        InputStream defaultDiffParserConfig = UnifiedDiffParserCreator.class.getResourceAsStream("diff_config.json");
        defaultNodeGraph = ConfigParser.generateParserConfig(defaultDiffParserConfig ).get("diffGitNode");
    }

    public UnifiedDiffParser create() {
        return new UnifiedDiffParser(defaultNodeGraph);
    }
}
