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

    def "Diff of a file with changed permissions should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('mode_change.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('a')
        unifiedDiff.getToFile().equals('a')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Modified)
        unifiedDiff.getMode().equals('100755')
        unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of a renamed file with a similarity index should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('rename_similarity_index.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('a')
        unifiedDiff.getToFile().equals('b')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Renamed)
        unifiedDiff.getSimilarityIndex().equals('100%')
        unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of a copied file with a similarity index should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('copy_similarity_index.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('a')
        unifiedDiff.getToFile().equals('b')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Copied)
        unifiedDiff.getSimilarityIndex().equals('100%')
        unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of a modified file should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('modified.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('a')
        unifiedDiff.getToFile().equals('a')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Modified)
        unifiedDiff.getChecksumBefore().equals("5c31be7")
        unifiedDiff.getChecksumAfter().equals("45cfaf4")
        unifiedDiff.getMode().equals("100644")
        !unifiedDiff.getDiffBody().isEmpty()
    }

    def "Diff of a modified and mode changed file should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('modified_mode_change.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiffs.size() == 1
        unifiedDiff.getFromFile().equals('a')
        unifiedDiff.getToFile().equals('a')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Modified)
        unifiedDiff.getChecksumBefore().equals("5c31be7")
        unifiedDiff.getChecksumAfter().equals("38e4da5")
        unifiedDiff.getMode().equals("100755")
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
