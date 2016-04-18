package udp.parse.util

import spock.lang.Specification
import udp.parse.UnifiedDiffParser

// TODO: Rethink having UnifiedDiffParserCreator being a singleton. Singletons are
// TODO:    hard to test and may be a bit of an antipattern in general. Investigate.
class UnifiedDiffParserCreatorTest extends Specification {
    def "Create"() {
        given:
            UnifiedDiffParserCreator unifiedDiffParserCreator =
                    UnifiedDiffParserCreator.getInstance()

        when:
            UnifiedDiffParser unifiedDiffParser =
                    unifiedDiffParserCreator.create()

        then:
            unifiedDiffParser.getIndividualDiffParser().getNodeTree() != null
    }
}
