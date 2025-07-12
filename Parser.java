import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;
    private Token current;
    private String lastRow = "?"; 

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        next();
    }

    private Token next() {
        if (position < tokens.size()) {
            current = tokens.get(position++);
            lastRow = current.getRow();
            return current;
        } else {
            current = null;
            return null;
        }
    }

    private void eat(TokenType expected) {
        if (current != null && current.getType() == expected) {
            next();
        } else {
            printSyntaxError();
        }
    }

    public Node parseProgram() {
        Node root = new Node("program");
        root.addChild(parseCommandList());

        if (current != null) {
            printSyntaxError();
        }

        return root;
    }

    public Node parseCommandList() {
        Node node = new Node("command_list");
        if (current != null && (
            current.getType() == TokenType.FORW || current.getType() == TokenType.BACK ||
            current.getType() == TokenType.LEFT || current.getType() == TokenType.RIGHT ||
            current.getType() == TokenType.UP || current.getType() == TokenType.DOWN ||
            current.getType() == TokenType.COLOR || current.getType() == TokenType.REP)) {

            node.addChild(parseCommand());
            node.addChild(parseCommandList());
        }
        return node;
    }

    public Node parseCommand() {
        Node node = new Node("command");

        if (current == null) {
            printSyntaxError();
        }

        if (current.getType() == TokenType.FORW || current.getType() == TokenType.BACK ||
            current.getType() == TokenType.LEFT || current.getType() == TokenType.RIGHT) {

            node.addChild(parseMoveCommand());
            eat(TokenType.PERIOD);

        } else if (current.getType() == TokenType.UP || current.getType() == TokenType.DOWN ||
                   current.getType() == TokenType.COLOR) {

            node.addChild(parsePenCommand());
            eat(TokenType.PERIOD);

        } else if (current.getType() == TokenType.REP) {
            node.addChild(parseRepCommand());

        } else {
            printSyntaxError();
        }

        return node;
    }

    public Node parseMoveCommand() {
        Node node = new Node("move_command");
        TokenType type = current.getType();

        node.addChild(new Node(current));
        eat(type);

        if (current != null && current.getType() == TokenType.DECIMAL) {
            checkDecimalIsPositive(current); 
            node.addChild(new Node(current));
            eat(TokenType.DECIMAL);
        } else {
            printSyntaxError();
        }

        return node;
    }

    public Node parsePenCommand() {
        Node node = new Node("pen_command");

        if (current.getType() == TokenType.UP || current.getType() == TokenType.DOWN) {
            node.addChild(new Node(current));
            eat(current.getType());
        } else if (current.getType() == TokenType.COLOR) {
            node.addChild(new Node(current));
            eat(TokenType.COLOR);

            if (current != null && current.getType() == TokenType.HEX) {
                node.addChild(new Node(current));
                eat(TokenType.HEX);
            } else {
                printSyntaxError();
            }
        } else {
            printSyntaxError();
        }

        return node;
    }

    public Node parseRepCommand() {
        Node node = new Node("rep_command");
        eat(TokenType.REP);

        if (current != null && current.getType() == TokenType.DECIMAL) {
            checkDecimalIsPositive(current); 
            node.addChild(new Node(current));
            eat(TokenType.DECIMAL);
        } else {
            printSyntaxError();
        }

        node.addChild(parseRepBody());
        return node;
    }

    public Node parseRepBody() {
        Node node = new Node("rep_body");
    
        if (current == null) {
            printSyntaxError();
        }
    
        if (current.getType() == TokenType.QUOTE) {
            eat(TokenType.QUOTE);
    
            if (current != null && current.getType() == TokenType.QUOTE) {
                printSyntaxError();
            }
    
            node.addChild(parseCommandList());
    
            if (current == null || current.getType() != TokenType.QUOTE) {
                printSyntaxError();
            }
    
            eat(TokenType.QUOTE);
        } else {
            node.addChild(parseCommand());
        }
    
        return node;
    }
    
    private void checkDecimalIsPositive(Token token) {
        try {
            int value = Integer.parseInt(token.getValue());
            if (value == 0) {
                System.out.println("Syntaxfel på rad " + token.getRow());
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            System.out.println("Syntaxfel på rad " + token.getRow());
            System.exit(0);
        }
    }

    private void printSyntaxError() {
        String row = current != null ? current.getRow() : lastRow;
        System.out.println("Syntaxfel på rad " + row);
        System.exit(0);
    }
}
