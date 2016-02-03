package udp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType

class ConfigParser {
    static ParserConfig generateParserConfig(InputStream config) {
        ObjectMapper mapper = new ObjectMapper();
        final CollectionType javaType =
                mapper.getTypeFactory().constructCollectionType(
                        List.class, ParserNodeConfig.class
                );
        mapper.readValue(config, javaType)

        return ParserConfig.getInstance()
    }
}
