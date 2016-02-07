package udp.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import rdglp.strategy.LineHandlingStrategy
import udp.node.GenericParserNode
import udp.node.ParserNode

class ParserNodeConfigDeserializer extends JsonDeserializer<ParserNodeConfig> {
    @Override
    ParserNodeConfig deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String parserNodeConfigName = node.get("name").asText()
        Class<?> clazz = Class.forName(node.get("lineHandlingStrategy").asText());
        LineHandlingStrategy lineHandlingStrategy = (LineHandlingStrategy) clazz.newInstance();
        String applicabilityPattern = node.get("applicabilityPattern").asText()
        List<String> possibleNextNodeNames = node.get("possibleNextNodes").asList()*.asText()
        Set<ParserNode> possibleNextNodes = getPossibleNextNodes(
                possibleNextNodeNames
        )

        ParserNode parserNode = new GenericParserNode(
                applicabilityPattern,
                lineHandlingStrategy,
                possibleNextNodes
        )

        ParserConfig parserConfig = ParserConfig.getInstance()
        parserConfig.put(parserNodeConfigName, parserNode)

        // TODO: This is kinda weird how we're 'done' once parsing (don't need to
        // TODO:    handle returned parsed object)
        return null
    }

    private static Set<ParserNode> getPossibleNextNodes(List<String> possibleNextNodeNames) {
        Set<ParserNode> possibleNextNodes = new HashSet<ParserNode>();
        ParserConfig parserConfig = ParserConfig.getInstance()
        for (String nodeName: possibleNextNodeNames) {
            // TODO: Throw error here if nodeName isn't in parserConfig
            possibleNextNodes.add(parserConfig.get(nodeName))
        }

        return possibleNextNodes
    }
}
