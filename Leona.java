public class Leona {
    private double x = 0.0;
    private double y = 0.0;
    private double angle = 0.0; 
    private boolean penDown = false;
    private String penColor = "#0000FF";

    public void execute(Node node) {
        switch (node.label) {
            case "program", "command_list", "rep_body" -> {
                for (Node child : node.children) {
                    execute(child);
                }
            }
            case "command" -> execute(node.children.get(0));
            case "move_command" -> handleMoveCommand(node);
            case "pen_command" -> handlePenCommand(node);
            case "rep_command" -> handleRepCommand(node);
        }
    }

    private void handleMoveCommand(Node node) {
        String command = getTokenType(node.children.get(0));
        int value = getTokenValue(node.children.get(1));
        switch (command) {
            case "FORW" -> move(value);
            case "BACK" -> move(-value);
            case "LEFT" -> turn(value);
            case "RIGHT" -> turn(-value);
        }
    }

    private void handlePenCommand(Node node) {
        String command = getTokenType(node.children.get(0));
        switch (command) {
            case "DOWN" -> penDown = true;
            case "UP" -> penDown = false;
            case "COLOR" -> {
                String hex = getTokenValueAsString(node.children.get(1)); 
                penColor = hex.toUpperCase();
            }
        }
    }

    private void handleRepCommand(Node node) {
        int count = getTokenValue(node.children.get(0));
        Node body = node.children.get(1); 
        for (int i = 0; i < count; i++) {
            execute(body);
        }
    }

    private void move(int distance) {
        double radians = Math.toRadians(angle);
        double newX = x + distance * Math.cos(radians);
        double newY = y + distance * Math.sin(radians);

        if (penDown) {
            System.out.printf("%s %.4f %.4f %.4f %.4f%n", penColor, x, y, newX, newY);
        }

        x = newX;
        y = newY;
    }

    private void turn(double degrees) {
        angle = (angle + degrees) % 360;
        if (angle < 0) angle += 360;
    }

    private String getTokenType(Node tokenNode) {
        return tokenNode.label.split("\\(")[0];
    }

    private int getTokenValue(Node tokenNode) {
        String raw = tokenNode.label.replaceAll(".*\\('", "").replaceAll("'\\)", "");
        return Integer.parseInt(raw);
    }

    private String getTokenValueAsString(Node tokenNode) {
        return tokenNode.label.replaceAll(".*\\('", "").replaceAll("'\\)", "");
    }
}
