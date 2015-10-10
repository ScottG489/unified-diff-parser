package udp.strategy

import java.util.regex.Matcher
import java.util.regex.Pattern

class StrategyHelper {
    static String extractDataFromHeaderLine(
            String headerLine,
            Pattern headerLineExpression,
            int groupNumber) {
        Matcher m = headerLineExpression.matcher(headerLine)
        m.find()
        return m.group(groupNumber)
    }
}
