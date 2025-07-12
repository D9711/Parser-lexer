enum TokenType { 
    RIGHT,
    LEFT,
    FORW, 
    BACK,
    PERIOD,
    DECIMAL,
    HEX,
    COLOR,
    UP,
    DOWN,
    REP,
    QUOTE,
    ERROR, 
}

class Token {
    private TokenType type;
    private String value;
    private String row;

    public Token(TokenType type, String value, String row) {
        this.type = type;
        this.value = value;
        this.row = row;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getRow() {
        return row;
    }

    @Override
    public String toString() {
        return type + ": " + value + " rad: " + row;
    }
}
 
