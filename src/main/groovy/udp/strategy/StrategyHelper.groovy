package udp.strategy

import java.util.regex.Matcher
import java.util.regex.Pattern

class StrategyHelper {
    static String extractDataFromLine(
            String line,
            Pattern lineExpression,
            int groupNumber) {
        Matcher m = lineExpression.matcher(line)
        m.find()
        return m.group(groupNumber)
    }
}
