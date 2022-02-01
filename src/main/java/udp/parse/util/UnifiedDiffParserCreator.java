package udp.parse.util;

import rdglp.config.ConfigParser;
import rdglp.node.ParserNode;
import udp.parse.UnifiedDiffParser;

import java.io.IOException;
import java.io.InputStream;

public class UnifiedDiffParserCreator {
    private final ParserNode defaultNodeGraph;

    public UnifiedDiffParserCreator() throws IOException {
        InputStream defaultDiffParserConfig = UnifiedDiffParserCreator.class.getResourceAsStream("diff_config.json");
        defaultNodeGraph = new ConfigParser().generateParserConfig(defaultDiffParserConfig).get("diffGitNode");
    }

    public UnifiedDiffParser create() {
        return new UnifiedDiffParser(defaultNodeGraph);
    }
}
