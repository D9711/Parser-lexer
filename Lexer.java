import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static ArrayList<Token> tokenize(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        String[] lines = input.split("\n");

        StringBuilder cleanedInput = new StringBuilder();
        List<Integer> positionToLine = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNumber = i + 1;

            int commentIndex = line.indexOf('%');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }

            line = line.strip();
            if (line.isEmpty()) continue;

            cleanedInput.append(line).append("\n");

            for (int j = 0; j < line.length() + 1; j++) {
                positionToLine.add(lineNumber);
            }
        }

        String cleaned = cleanedInput.toString();

    Pattern tokenPattern = Pattern.compile(
        "(?<FORW>\\bforw(?=\\s))|" +
        "(?<BACK>\\bback(?=\\s))|" +
        "(?<LEFT>\\bleft(?=\\s))|" +
        "(?<RIGHT>\\bright(?=\\s))|" +
        "(?<UP>\\bup\\b)|" +
        "(?<DOWN>\\bdown\\b)|" +
        "(?<COLOR>\\bcolor(?=\\s))|" +
        "(?<REP>\\brep\\b)|" +
        "(?<HEX>#[0-9A-Fa-f]{6})|" +
        "(?<DECIMAL>\\d+(?=\\s|\\.|$))|" +
        "(?<PERIOD>\\.)|" +
        "(?<QUOTE>\")|" +
        "(?<ERROR>[^\\s]+)",
        Pattern.CASE_INSENSITIVE
    );

    Matcher matcher = tokenPattern.matcher(cleaned);
    while (matcher.find()) {
        String lexeme = matcher.group();
        TokenType type = null;

        if (matcher.group("FORW") != null) type = TokenType.FORW;
        else if (matcher.group("BACK") != null) type = TokenType.BACK;
        else if (matcher.group("LEFT") != null) type = TokenType.LEFT;
        else if (matcher.group("RIGHT") != null) type = TokenType.RIGHT;
        else if (matcher.group("UP") != null) type = TokenType.UP;
        else if (matcher.group("DOWN") != null) type = TokenType.DOWN;
        else if (matcher.group("COLOR") != null) type = TokenType.COLOR;
        else if (matcher.group("REP") != null) type = TokenType.REP;
        else if (matcher.group("HEX") != null) type = TokenType.HEX;
        else if (matcher.group("DECIMAL") != null) type = TokenType.DECIMAL;
        else if (matcher.group("PERIOD") != null) type = TokenType.PERIOD;
        else if (matcher.group("QUOTE") != null) type = TokenType.QUOTE;
        else type = TokenType.ERROR;

        int start = matcher.start();
        int lineNumber = positionToLine.get(start);
        tokens.add(new Token(type, lexeme, Integer.toString(lineNumber)));
    }

    return tokens;
}

}
