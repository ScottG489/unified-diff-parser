package udp

import java.util.regex.Matcher
import java.util.regex.Pattern

class UnifiedDiffParser {
    private List<UnifiedDiff> unifiedDiffs
    private String rawUnifiedDiff;

    UnifiedDiffParser(String unifiedDiff) {
        this.rawUnifiedDiff = unifiedDiff
        this.unifiedDiffs = new ArrayList<UnifiedDiff>()
    }

    public void parse() {
        for (String diff : getRawDiffs()) {
            unifiedDiffs.add(fromRawDiff(diff))
        }
    }

    // TODO: This is 'diff' specific and may be something preventing this from becoming a
    // TODO:    general purpose line parsing util.
    ArrayList<String> getRawDiffs() {
        ArrayList<String> rawDiffs = new ArrayList<String>()
        String diffSplitExpression = '\ndiff --git'
        int offset = diffSplitExpression.length() - 1
        Pattern p = Pattern.compile(diffSplitExpression)
        Matcher m = p.matcher(rawUnifiedDiff)
        int pos = 0
        while (m.find()) {
            rawDiffs.add(rawUnifiedDiff.substring(pos, m.end() - offset))
            pos = m.end() - offset
        }

        // One at the end we still have to add
        rawDiffs.add(rawUnifiedDiff.substring(pos))

        return rawDiffs
    }

    static UnifiedDiff fromRawDiff(String rawDiff) {
        IndividualDiffParser individualDiffParser = new IndividualDiffParser(rawDiff)
        return individualDiffParser.parse()
    }

    public List<UnifiedDiff> getUnifiedDiffs() {
        return unifiedDiffs;
    }
}

