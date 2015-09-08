package udp

import java.util.regex.Matcher
import java.util.regex.Pattern

class UnifiedDiffParser {
    private List<UnifiedDiff> unifiedDiffs
    private String rawUnifiedDiff;

    UnifiedDiffParser(String unifiedDiff){
        this.rawUnifiedDiff = unifiedDiff
        this.unifiedDiffs = new ArrayList<UnifiedDiff>()
    }

    public void parse() {
        for (String diff: getRawDiffs()) {
            unifiedDiffs.add(UnifiedDiff.fromRawDiff(diff))
        }
    }

    ArrayList<String> getRawDiffs() {
        ArrayList<String> rawDiffs = new ArrayList<String>()
        String diffSplitExpression = '\ndiff --git'
        // TODO: Not entirely sure why I need the - 1
        int offset = diffSplitExpression.length() - 1
        Pattern p = Pattern.compile(diffSplitExpression)
        Matcher m = p.matcher(rawUnifiedDiff)
        int pos = 0
        while (m.find()) {
            rawDiffs.add(rawUnifiedDiff.substring(pos, m.end() - offset))
            pos = m.end() - offset
        }

        return rawDiffs
    }
}

