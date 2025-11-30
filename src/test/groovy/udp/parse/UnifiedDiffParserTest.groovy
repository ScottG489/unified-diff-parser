package udp.parse

import rdglp.config.ConfigParser
import rdglp.node.ParserNode
import spock.lang.Specification
import udp.parse.UnifiedDiff.FileStatus

// TODO: Use a template diff instead of static files?
class UnifiedDiffParserTest extends Specification {
    def "UnifiedDiff generated from the file '#patchFileResourceName' should have the same fields as the expected UnifiedDiff"(
            String patchFileResourceName,
            int numberOfDiffs,
            UnifiedDiff expectedUnifiedDiff) {
        given:
            String text = getTestResourceText(patchFileResourceName)
            UnifiedDiffParser udp = getUdpFromResource()

        when:
            udp.parse(text)
            List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
            UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
            unifiedDiffs.size() == numberOfDiffs
            unifiedDiff.getFromFile().equals(expectedUnifiedDiff.getFromFile())
            unifiedDiff.getToFile().equals(expectedUnifiedDiff.getToFile())
            unifiedDiff.getFileStatus().equals(expectedUnifiedDiff.getFileStatus())
            unifiedDiff.isAddedFile() == expectedUnifiedDiff.isAddedFile()
            unifiedDiff.isRemovedFile() == expectedUnifiedDiff.isRemovedFile()
            unifiedDiff.isModifiedFile() == expectedUnifiedDiff.isModifiedFile()
            unifiedDiff.isCopied() == expectedUnifiedDiff.isCopied()
            unifiedDiff.isRenamed() == expectedUnifiedDiff.isRenamed()
            unifiedDiff.isBinary() == expectedUnifiedDiff.isBinary()
            unifiedDiff.getChecksumBefore().equals(expectedUnifiedDiff.getChecksumBefore())
            unifiedDiff.getChecksumAfter().equals(expectedUnifiedDiff.getChecksumAfter())
            unifiedDiff.getOldMode().equals(expectedUnifiedDiff.getOldMode())
            unifiedDiff.getMode().equals(expectedUnifiedDiff.getMode())
            unifiedDiff.getSimilarityIndex().equals(expectedUnifiedDiff.getSimilarityIndex())
            unifiedDiff.getDissimilarityIndex().equals(expectedUnifiedDiff.getDissimilarityIndex())

        where:
            patchFileResourceName                     | numberOfDiffs | expectedUnifiedDiff
            'added.patch'                             | 1             | getExpectedAddedUnifiedDiff()
            'added_binary.patch'                      | 1             | getExpectedAddedBinaryUnifiedDiff()
            'added_empty.patch'                       | 1             | getExpectedAddedEmptyUnifiedDiff()
            'removed.patch'                           | 1             | getExpectedRemovedUnifiedDiff()
            'mode_change.patch'                       | 1             | getExpectedModeChangeUnifiedDiff()
            'rename_similarity_index.patch'           | 1             | getExpectedRenameSimilarityIndexUnifiedDiff()
            'copy_similarity_index.patch'             | 1             | getExpectedCopySimilarityIndexUnifiedDiff()
            'modified.patch'                          | 1             | getExpectedModifiedUnifiedDiff()
            'modified_mode_change.patch'              | 1             | getExpectedModifiedModeChangeUnifiedDiff()
            'added_binary_literal.patch'              | 1             | getExpectedAddedBinaryLiteralUnifiedDiff()
            'rename_binary_literal_mode_change.patch' | 1             | getExpectedRenameBinaryLiteralModeChangeUnifiedDiff()
            'dissimilarity_index.patch'               | 1             | getExpectedDissimilarityIndexUnifiedDiff()
            'multi.patch'                             | 3             | getExpectedMultiUnifiedDiff()
    }

    UnifiedDiff getExpectedAddedUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('/dev/null')
        expectedUnifiedDiff.setToFile('.gitignore')
        expectedUnifiedDiff.setFileStatus(FileStatus.Added)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('0000000')
        expectedUnifiedDiff.setChecksumAfter('d490e8e')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedAddedBinaryUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('/dev/null')
        expectedUnifiedDiff.setToFile('doc/doxygen/html/bc_s.png')
        expectedUnifiedDiff.setFileStatus(FileStatus.Added)
        expectedUnifiedDiff.setIsBinary(true)
        expectedUnifiedDiff.setChecksumBefore('0000000')
        expectedUnifiedDiff.setChecksumAfter('e401862')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedAddedEmptyUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('doc/sphinx/keyfile')
        expectedUnifiedDiff.setToFile('doc/sphinx/keyfile')
        expectedUnifiedDiff.setFileStatus(FileStatus.Added)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('0000000')
        expectedUnifiedDiff.setChecksumAfter('e69de29')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedRemovedUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('task.py')
        expectedUnifiedDiff.setToFile('/dev/null')
        expectedUnifiedDiff.setFileStatus(FileStatus.Removed)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('70e053b')
        expectedUnifiedDiff.setChecksumAfter('0000000')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedModeChangeUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('a')
        expectedUnifiedDiff.setToFile('a')
        expectedUnifiedDiff.setFileStatus(FileStatus.Modified)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore(null)
        expectedUnifiedDiff.setChecksumAfter(null)
        expectedUnifiedDiff.setOldMode('100644')
        expectedUnifiedDiff.setMode('100755')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedRenameSimilarityIndexUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('a')
        expectedUnifiedDiff.setToFile('b')
        expectedUnifiedDiff.setFileStatus(FileStatus.Renamed)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore(null)
        expectedUnifiedDiff.setChecksumAfter(null)
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode(null)
        expectedUnifiedDiff.setSimilarityIndex('100%')
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedCopySimilarityIndexUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('a')
        expectedUnifiedDiff.setToFile('b')
        expectedUnifiedDiff.setFileStatus(FileStatus.Copied)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore(null)
        expectedUnifiedDiff.setChecksumAfter(null)
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode(null)
        expectedUnifiedDiff.setSimilarityIndex('100%')
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedModifiedUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('a')
        expectedUnifiedDiff.setToFile('a')
        expectedUnifiedDiff.setFileStatus(FileStatus.Modified)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('5c31be7')
        expectedUnifiedDiff.setChecksumAfter('45cfaf4')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedModifiedModeChangeUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('a')
        expectedUnifiedDiff.setToFile('a')
        expectedUnifiedDiff.setFileStatus(FileStatus.Modified)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('5c31be7')
        expectedUnifiedDiff.setChecksumAfter('38e4da5')
        expectedUnifiedDiff.setOldMode('100644')
        expectedUnifiedDiff.setMode('100755')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedAddedBinaryLiteralUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('bar')
        expectedUnifiedDiff.setToFile('bar')
        expectedUnifiedDiff.setFileStatus(FileStatus.Modified)
        expectedUnifiedDiff.setIsBinary(true)
        expectedUnifiedDiff.setChecksumBefore('78981922613b2afb6025042ff6bd878ac1994e85')
        expectedUnifiedDiff.setChecksumAfter('0b8a5ce4e558f9bd5c6f5d1855ff2504a4df9e17')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100755')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedRenameBinaryLiteralModeChangeUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('foo')
        expectedUnifiedDiff.setToFile('boo')
        expectedUnifiedDiff.setFileStatus(FileStatus.Renamed)
        expectedUnifiedDiff.setIsBinary(true)
        expectedUnifiedDiff.setChecksumBefore('b8bd059ec9968339eddf762411b39ece50f78e3e')
        expectedUnifiedDiff.setChecksumAfter('ba300ede17a9e96ff8bbac6fb9250e18a9d69bea')
        expectedUnifiedDiff.setOldMode('100644')
        expectedUnifiedDiff.setMode('100755')
        expectedUnifiedDiff.setSimilarityIndex('99%')
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedDissimilarityIndexUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('gradle.properties')
        expectedUnifiedDiff.setToFile('gradle.properties')
        expectedUnifiedDiff.setFileStatus(FileStatus.Modified)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('a2fdf3d')
        expectedUnifiedDiff.setChecksumAfter('c7acfeb')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        expectedUnifiedDiff.setDissimilarityIndex('32%')
        return expectedUnifiedDiff
    }

    UnifiedDiff getExpectedMultiUnifiedDiff() {
        UnifiedDiff expectedUnifiedDiff = new UnifiedDiff("")
        expectedUnifiedDiff.setFromFile('/dev/null')
        expectedUnifiedDiff.setToFile('.gitignore')
        expectedUnifiedDiff.setFileStatus(FileStatus.Added)
        expectedUnifiedDiff.setIsBinary(false)
        expectedUnifiedDiff.setChecksumBefore('0000000')
        expectedUnifiedDiff.setChecksumAfter('d490e8e')
        expectedUnifiedDiff.setOldMode(null)
        expectedUnifiedDiff.setMode('100644')
        expectedUnifiedDiff.setSimilarityIndex(null)
        return expectedUnifiedDiff
    }

    private static UnifiedDiffParser getUdpFromResource() {
        ConfigParser configParser = new ConfigParser()
        // TODO: Bad hard coded values
        InputStream config = new FileInputStream("src/test/resources/udp/parse/diff_test.json")
        ParserNode firstNode = configParser.generateParserConfig(config).get('diffGitNode')
        return new UnifiedDiffParser(firstNode)
    }

    private static String getTestResourceText(String fileName) {
        return UnifiedDiffParser.getResource(fileName).getText()
    }
}
