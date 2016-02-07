package udp.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = ParserNodeConfigDeserializer.class)
// Dummy class so we can tie a custom deserializer to something.
// Along with being a dummy class this defines the schema for a valid config
public class ParserNodeConfig {
    String name;
    String applicabilityPattern;
    String lineHandlingStrategy;
    List<String> possibleNextNodes;
}
