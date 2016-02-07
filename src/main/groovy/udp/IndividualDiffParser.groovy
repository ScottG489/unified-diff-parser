package udp

import rdglp.node.ParserNode
import rdglp.strategy.util.StrategyHelper

class IndividualDiffParser {
    private UnifiedDiff unifiedDiff
    private ParserNode firstNode

    IndividualDiffParser(ParserNode firstNode) {
        this.firstNode = firstNode
    }

    // TODO: Inconsistency between added/deleted text and binary files:
    // TODO:        Text: To/From file respectively is '/dev/null' along with file being added/deleted
    // TODO:        Binary: To/From file respectively are both the same
    UnifiedDiff parse(String parsableLines) {
        unifiedDiff = new UnifiedDiff(parsableLines)
        // TODO: Initialize as modified, will be overridden if otherwise.
        // TODO: Do we want to consider mode changes as modified?
        // TODO: This will likely be set incorrectly for certain binary file statuses
        // TODO:    since we don't currently have detection on all of them for binaries
        unifiedDiff.setFileStatus(UnifiedDiff.FileStatus.Modified)

        parseUnifiedDiff(parsableLines)

        setFileNamesIfNecessary(unifiedDiff)

        return unifiedDiff
    }

    private UnifiedDiff parseUnifiedDiff(String parsableLines) {
        LineParser<UnifiedDiff> lineParser =
                new LineParser<UnifiedDiff>(unifiedDiff, getNodeTree());
        unifiedDiff = lineParser.parse(parsableLines)
    }

    ParserNode getNodeTree() {
        return firstNode
    }

    // TODO: Get the to and from file names from the first line as a last ditch effort
    // TODO: This is 'diff' specific and may be something preventing this from becoming a
    // TODO:    general purpose line parsing util.
    private static void setFileNamesIfNecessary(UnifiedDiff unifiedDiff) {
        if (unifiedDiff.getFromFile() == null) {
            unifiedDiff.setFromFile(StrategyHelper.extractDataFromLine(
                    unifiedDiff.getRawDiff(), LineExpression.DIFF_GIT, 1
            ))
        }
        if (unifiedDiff.getToFile() == null) {
            unifiedDiff.setToFile(StrategyHelper.extractDataFromLine(
                    unifiedDiff.getRawDiff(), LineExpression.DIFF_GIT, 1
            ))
        }
    }
}

