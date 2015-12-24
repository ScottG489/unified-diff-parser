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
        unifiedDiff.isAddedFile()
        unifiedDiff.getMode().equals('100644')
    }

    // XXX: Failing because we are cutting off headers at the index line but there's more
    // XXX:     relevant info after such as 'GIT binary patch' line and 'Binary files...' line
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
        unifiedDiff.isAddedFile()
        unifiedDiff.isBinary()
        unifiedDiff.getMode().equals('100644')
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
        unifiedDiff.isAddedFile()
        unifiedDiff.getMode().equals('100644')
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
        unifiedDiff.isRemovedFile()
        unifiedDiff.getMode().equals('100644')
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
        unifiedDiff.isModifiedFile()
        unifiedDiff.getOldMode().equals('100644')
        unifiedDiff.getMode().equals('100755')
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
        unifiedDiff.isRenamed()
        unifiedDiff.getSimilarityIndex().equals('100%')
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
        unifiedDiff.isCopied()
        unifiedDiff.getSimilarityIndex().equals('100%')
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
        unifiedDiff.isModifiedFile()
        unifiedDiff.getChecksumBefore().equals("5c31be7")
        unifiedDiff.getChecksumAfter().equals("45cfaf4")
        unifiedDiff.getMode().equals("100644")
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
        unifiedDiff.isModifiedFile()
        unifiedDiff.getChecksumBefore().equals("5c31be7")
        unifiedDiff.getChecksumAfter().equals("38e4da5")
        unifiedDiff.getOldMode().equals("100644")
        unifiedDiff.getMode().equals("100755")
    }

    def "Diff with GIT binary patch line should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('added_binary_literal.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiff.getFromFile().equals('bar')
        unifiedDiff.getToFile().equals('bar')
        unifiedDiff.isBinary()
        unifiedDiff.getMode().equals('100755')
        unifiedDiff.getChecksumBefore().equals('78981922613b2afb6025042ff6bd878ac1994e85')
        unifiedDiff.getChecksumAfter().equals('0b8a5ce4e558f9bd5c6f5d1855ff2504a4df9e17')
    }

    def "Diff of a renamed binary file that was modified and had it's mode changed should have appropriate attributes"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('rename_binary_literal_mode_change.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
        UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
        unifiedDiff.getFromFile().equals('foo')
        unifiedDiff.getToFile().equals('boo')
        unifiedDiff.getFileStatus().equals(UnifiedDiff.FileStatus.Renamed)
        unifiedDiff.isRenamed()
        unifiedDiff.isBinary()
        unifiedDiff.getOldMode().equals('100644')
        unifiedDiff.getMode().equals('100755')
        unifiedDiff.getSimilarityIndex().equals('99%')
        unifiedDiff.getChecksumBefore().equals('b8bd059ec9968339eddf762411b39ece50f78e3e')
        unifiedDiff.getChecksumAfter().equals('ba300ede17a9e96ff8bbac6fb9250e18a9d69bea')
    }

    def "Diff with 3 files in it should have a size of 3"() {
        when:
        UnifiedDiffParser udp = getUdpFromResource('multi.patch')
        udp.parse()
        List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()

        then:
        unifiedDiffs.size() == 3
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
