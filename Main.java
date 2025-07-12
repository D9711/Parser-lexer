import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String input = """
   % Ta 8 steg framåt
REP 2 REP 4 FORW 1.
REP% Repetition på gång
2% Två gånger
"%Snart kommer kommandon
DOWN% Kommentera mera
.% Avsluta down-kommando
FORW 1
LEFT 1. % Oj, glömde punkt efter FORW-kommando
"
         """;

        ArrayList<Token> tokens = Lexer.tokenize(input);

        Parser parser = new Parser(tokens);  
        Node tree = parser.parseProgram();

        Leona leona = new Leona();
        leona.execute(tree);

        System.out.println(tree);
    }
}
