package udp.parse;

import rdglp.node.ParserNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnifiedDiffParser {
    private List<UnifiedDiff> unifiedDiffs;
    private String rawUnifiedDiff;
    private IndividualDiffParser parser;

    public UnifiedDiffParser(ParserNode firstNode) {
        parser = new IndividualDiffParser(firstNode);
        this.unifiedDiffs = new ArrayList<UnifiedDiff>();
    }

    public List<UnifiedDiff> parse(String unifiedDiff) {
        this.rawUnifiedDiff = unifiedDiff;
        for (String diff : getRawDiffs()) {
            unifiedDiffs.add(parser.parse(diff));
        }


        return unifiedDiffs;
    }

    // TODO: This is 'diff' specific and may be something preventing this from becoming a
    // TODO:    general purpose line parsing util.
    public ArrayList<String> getRawDiffs() {
        ArrayList<String> rawDiffs = new ArrayList<String>();
        String diffSplitExpression = "\ndiff --git";
        int offset = diffSplitExpression.length() - 1;
        Pattern p = Pattern.compile(diffSplitExpression);
        Matcher m = p.matcher(rawUnifiedDiff);
        int pos = 0;
        while (m.find()) {
            rawDiffs.add(rawUnifiedDiff.substring(pos, m.end() - offset));
            pos = m.end() - offset;
        }


        // TODO: This incidentally adds single diffs as well. However, it will also add
        // TODO:    a invalid patch, empty file, etc.
        // One at the end we still have to add
        rawDiffs.add(rawUnifiedDiff.substring(pos));

        return rawDiffs;
    }

    public List<UnifiedDiff> getUnifiedDiffs() {
        return unifiedDiffs;
    }

    public IndividualDiffParser getIndividualDiffParser() {
        return parser;
    }
}
