package udp.parse.util

import spock.lang.Specification
import udp.parse.UnifiedDiffParser

class UnifiedDiffParserCreatorTest extends Specification {
    def "A unified diff parser created from a creator should have a node tree"() {
        given:
            UnifiedDiffParserCreator unifiedDiffParserCreator =
                    new UnifiedDiffParserCreator()

        when:
            UnifiedDiffParser unifiedDiffParser =
                    unifiedDiffParserCreator.create()

        then:
            unifiedDiffParser.getIndividualDiffParser().getNodeTree() != null
    }
}
