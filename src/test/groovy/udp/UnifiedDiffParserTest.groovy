package udp

import spock.lang.Specification

// TODO: Use a template diff instead of static files?
class UnifiedDiffParserTest extends Specification {
    def "Diff of an added file should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('added.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('/dev/null')
        unifiedDiff.getToFile().equals('.gitignore')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Added)
        unifiedDiff.getMode().equals('100644')
        !unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of an added binary file should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('added_binary.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('/dev/null')
        unifiedDiff.getToFile().equals('doc/doxygen/html/bc_s.png')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Added)
        unifiedDiff.getMode().equals('100644')
        unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of an added empty file should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('added_empty.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('doc/sphinx/keyfile')
        unifiedDiff.getToFile().equals('doc/sphinx/keyfile')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Added)
        unifiedDiff.getMode().equals('100644')
        unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of a removed file should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('removed.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('task.py')
        unifiedDiff.getToFile().equals('/dev/null')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Removed)
        unifiedDiff.getMode().equals('100644')
        !unifiedDiff.getDiffBody().isEmpty()
    }

    private static UnifiedDiffParser getUdpFromResource(String resourceName) {
        return new UnifiedDiffParser(
                getTestResourceText(resourceName)
        )
    }

    private static String getTestResourceText(String fileName) {
        return UnifiedDiffParser.getResource(fileName).getText()
    }
}
