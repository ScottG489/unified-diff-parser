package udp.parse.util;

import rdglp.config.ConfigParser;
import rdglp.node.ParserNode;
import udp.parse.UnifiedDiffParser;

import java.io.InputStream;

public class UnifiedDiffParserCreator {
    UnifiedDiffParserCreator() {
        ConfigParser configParser = ConfigParser.getInstance();
        DEFAULT_DIFF_PARSER_CONFIG = UnifiedDiffParserCreator.class.getResourceAsStream("diff_config.json");
        DEFAULT_NODE_GRAPH = configParser.generateParserConfig(DEFAULT_DIFF_PARSER_CONFIG).get("diffGitNode");
    }

    public UnifiedDiffParser create() {
        return new UnifiedDiffParser(DEFAULT_NODE_GRAPH);
    }

    private final InputStream DEFAULT_DIFF_PARSER_CONFIG;
    private final ParserNode DEFAULT_NODE_GRAPH;
}
