package udp.parse

import rdglp.config.ConfigParser
import rdglp.node.ParserNode
import spock.lang.Specification
import spock.lang.Unroll
import udp.parse.UnifiedDiff.FileStatus

// TODO: Use a template diff instead of static files?
class UnifiedDiffParserTest extends Specification {
    @Unroll
    def "UnifiedDiff generated from the file '#patchFileResourceName' should have the specified properties"(
            String patchFileResourceName,
            int numberOfDiffs,
            String fromFile,
            String toFile,
            FileStatus fileStatus,
            boolean isBinary,
            String checksumBefore,
            String checksumAfter,
            String oldMode,
            String mode,
            String similarityIndex) {
        given:
            String text = getTestResourceText(patchFileResourceName)
            UnifiedDiffParser udp = getUdpFromResource()

        when:
            udp.parse(text)
            List<UnifiedDiff> unifiedDiffs = udp.getUnifiedDiffs()
            UnifiedDiff unifiedDiff = unifiedDiffs.first()

        then:
            unifiedDiffs.size() == numberOfDiffs
            unifiedDiff.getFromFile().equals(fromFile)
            unifiedDiff.getToFile().equals(toFile)
            unifiedDiff.getFileStatus().equals(fileStatus)
            isStatusOnlyTrueFor(fileStatus, unifiedDiff)
            unifiedDiff.isBinary() == isBinary
            unifiedDiff.getChecksumBefore().equals(checksumBefore)
            unifiedDiff.getChecksumAfter().equals(checksumAfter)
            unifiedDiff.getOldMode() == oldMode
            unifiedDiff.getMode().equals(mode)
            unifiedDiff.getSimilarityIndex().equals(similarityIndex)

            // TODO: How do I address the excessive width of this data table?
            // TODO: Do we want oldMode to be null if it hasn't changed or be the same as mode?
        where:
            patchFileResourceName                     | numberOfDiffs | fromFile             | toFile                      | fileStatus          | isBinary | checksumBefore                             | checksumAfter                              | oldMode  | mode     | similarityIndex
            'added.patch'                             | 1             | '/dev/null'          | '.gitignore'                | FileStatus.Added    | false    | '0000000'                                  | 'd490e8e'                                  | null     | '100644' | null
            'added_binary.patch'                      | 1             | '/dev/null'          | 'doc/doxygen/html/bc_s.png' | FileStatus.Added    | true     | '0000000'                                  | 'e401862'                                  | null     | '100644' | null
            'added_empty.patch'                       | 1             | 'doc/sphinx/keyfile' | 'doc/sphinx/keyfile'        | FileStatus.Added    | false    | '0000000'                                  | 'e69de29'                                  | null     | '100644' | null
            'removed.patch'                           | 1             | 'task.py'            | '/dev/null'                 | FileStatus.Removed  | false    | '70e053b'                                  | '0000000'                                  | null     | '100644' | null
            'mode_change.patch'                       | 1             | 'a'                  | 'a'                         | FileStatus.Modified | false    | null                                       | null                                       | '100644' | '100755' | null
            'rename_similarity_index.patch'           | 1             | 'a'                  | 'b'                         | FileStatus.Renamed  | false    | null                                       | null                                       | null     | null     | '100%'
            'copy_similarity_index.patch'             | 1             | 'a'                  | 'b'                         | FileStatus.Copied   | false    | null                                       | null                                       | null     | null     | '100%'
            'modified.patch'                          | 1             | 'a'                  | 'a'                         | FileStatus.Modified | false    | '5c31be7'                                  | '45cfaf4'                                  | null     | '100644' | null
            'modified_mode_change.patch'              | 1             | 'a'                  | 'a'                         | FileStatus.Modified | false    | '5c31be7'                                  | '38e4da5'                                  | '100644' | '100755' | null
            'added_binary_literal.patch'              | 1             | 'bar'                | 'bar'                       | FileStatus.Modified | true     | '78981922613b2afb6025042ff6bd878ac1994e85' | '0b8a5ce4e558f9bd5c6f5d1855ff2504a4df9e17' | null     | '100755' | null
            'rename_binary_literal_mode_change.patch' | 1             | 'foo'                | 'boo'                       | FileStatus.Renamed  | true     | 'b8bd059ec9968339eddf762411b39ece50f78e3e' | 'ba300ede17a9e96ff8bbac6fb9250e18a9d69bea' | '100644' | '100755' | '99%'
            'multi.patch'                             | 3             | '/dev/null'          | '.gitignore'                | FileStatus.Added    | false    | '0000000'                                  | 'd490e8e'                                  | null     | '100644' | null
    }

    private static UnifiedDiffParser getUdpFromResource() {
        ConfigParser configParser = ConfigParser.getInstance()
        // TODO: Bad hard coded values
        InputStream config = new FileInputStream("src/test/resources/udp/parse/diff_test.json")
        ParserNode firstNode = configParser.generateParserConfig(config).get('diffGitNode')
        return new UnifiedDiffParser(firstNode)
    }

    private static String getTestResourceText(String fileName) {
        return UnifiedDiffParser.getResource(fileName).getText()
    }

    // TODO: This is a result of me not wanting to make the above data table too wide
    // TODO:    and wanting to get coverage on is<Status>File method calls. The data
    // TODO:    table even now is too long. Is there a way to make it shorter?
    private static boolean isStatusOnlyTrueFor(
            FileStatus onlyTrueStatus, UnifiedDiff unifiedDiff) {
        HashMap<FileStatus, Boolean> statuses = getTrueStatusFor(onlyTrueStatus)
        for (Map.Entry<FileStatus, Boolean> entry : statuses) {
            switch (entry.getKey()) {
                case FileStatus.Added:
                    if (unifiedDiff.isAddedFile() != entry.getValue()) {
                        return false
                    }
                    break
                case FileStatus.Removed:
                    if (unifiedDiff.isRemovedFile() != entry.getValue()) {
                        return false
                    }
                    break
                case FileStatus.Modified:
                    if (unifiedDiff.isModifiedFile() != entry.getValue()) {
                        return false
                    }
                    break
                case FileStatus.Copied:
                    if (unifiedDiff.isCopied() != entry.getValue()) {
                        return false
                    }
                    break
                case FileStatus.Renamed:
                    if (unifiedDiff.isRenamed() != entry.getValue()) {
                        return false
                    }
                    break
            }
        }

        return true
    }

    private static HashMap getTrueStatusFor(FileStatus status) {
        HashMap<FileStatus, Boolean> statusMap = new HashMap<>()
        statusMap.put(FileStatus.Added, false)
        statusMap.put(FileStatus.Removed, false)
        statusMap.put(FileStatus.Modified, false)
        statusMap.put(FileStatus.Copied, false)
        statusMap.put(FileStatus.Renamed, false)
        statusMap.put(status, true)

        return statusMap
    }
}
