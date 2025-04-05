package h5;

import java.util.*;

public class Node {
    private String name;
    private Node firstChild;
    private Node nextSibling;

    public Node(String n, Node d, Node r) {
        if (!isValidNodeName(n)) {
            throw new IllegalArgumentException("Invalid node name: '" + n +
                    "'. Node name must not be empty and must not contain round brackets, commas, or whitespace.");
        }
        this.name = n;
        this.firstChild = d;
        this.nextSibling = r;
    }

    /**
     * Check if a node name is valid.
     *
     * @param name The name to check.
     * @return true if the name is valid, false otherwise.
     */
    private static boolean isValidNodeName(String name) {
        return name != null && !name.isEmpty() &&
                !name.contains("(") && !name.contains(")") &&
                !name.contains(",") && !name.contains(" ") &&
                !name.contains("\t") && !name.contains("\n") &&
                !name.contains("\r");
    }

    public static Node parsePostfix(String s) {
        if (s == null) {
            throw new RuntimeException("Input string cannot be null");
        }

        s = s.trim();
        if (s.isEmpty()) {
            throw new RuntimeException("Input string cannot be empty");
        }

        if (s.contains("()")) {
            throw new RuntimeException("Subtree cannot be empty: '" + s + "'");
        }

        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '(' && chars[i + 1] == ',') {
                throw new RuntimeException("Invalid coma placement in input: '" + s + "'");
            }
        }

        int balance = 0;
        for (char c : chars) {

            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }

            if (balance < 0) {
                throw new RuntimeException("Unbalanced parentheses in input: '" + s + "'");
            }
        }

        if (balance != 0) {
            throw new RuntimeException("Unbalanced parentheses in input: '" + s + "'");
        }

        try {
            ParseResult result = parsePostfixRecursive(s);
            if (!result.remaining.isEmpty()) {
                throw new RuntimeException("Invalid format - unexpected characters after parsing: '" + result.remaining + "'");
            }
            return result.node;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Error parsing input: '" + s + "': " + e.getMessage());
        }
    }

    private static class ParseResult {
        Node node;
        String remaining;

        ParseResult(Node node, String remaining) {
            this.node = node;
            this.remaining = remaining;
        }
    }

    private static ParseResult parsePostfixRecursive(String s) {
        if (s.isEmpty()) {
            throw new RuntimeException("Unexpected empty string during parsing");
        }


        if (s.charAt(0) == '(') {
            int closeParenIndex = findMatchingCloseParen(s, 0);
            if (closeParenIndex == -1) {
                throw new RuntimeException("Unbalanced parentheses in input: '" + s + "'");
            }

            String childrenStr = s.substring(1, closeParenIndex);

            String rootName = "";
            int i = closeParenIndex + 1;
            while (i < s.length() && !isSpecialChar(s.charAt(i))) {
                rootName += s.charAt(i);
                i++;
            }

            if (rootName.isEmpty()) {
                throw new RuntimeException("Node name cannot be empty");
            }
            validateNodeName(rootName);

            List<Node> children = parseChildrenList(childrenStr);

            Node root = new Node(rootName, null, null);
            if (!children.isEmpty()) {
                root.firstChild = children.get(0);
                for (int j = 0; j < children.size() - 1; j++) {
                    children.get(j).nextSibling = children.get(j + 1);
                }
            }

            return new ParseResult(root, s.substring(i));
        } else {
            String nodeName = "";
            int i = 0;
            while (i < s.length() && !isSpecialChar(s.charAt(i))) {
                nodeName += s.charAt(i);
                i++;
            }

            if (nodeName.isEmpty()) {
                throw new RuntimeException("Node name cannot be empty");
            }
            validateNodeName(nodeName);

            return new ParseResult(new Node(nodeName, null, null), s.substring(i));
        }
    }

    private static boolean isSpecialChar(char c) {
        return c == '(' || c == ')' || c == ',';
    }

    private static void validateNodeName(String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Node name cannot be empty");
        }
        if (!isValidNodeName(name)) {
            throw new RuntimeException("Invalid node name: '" + name +
                    "'. Node name must not contain round brackets, commas, or whitespace.");
        }
    }

    private static int findMatchingCloseParen(String s, int openIndex) {
        int balance = 1;
        for (int i = openIndex + 1; i < s.length(); i++) {
            if (s.charAt(i) == '(') balance++;
            else if (s.charAt(i) == ')') {
                balance--;
                if (balance == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static List<Node> parseChildrenList(String childrenStr) {
        List<Node> children = new ArrayList<>();
        if (childrenStr.isEmpty()) {
            return children;
        }

        boolean expectComma = false;

        while (!childrenStr.isEmpty()) {
            if (expectComma && (childrenStr.isEmpty() || childrenStr.charAt(0) != ',')) {
                throw new RuntimeException("Sibling subtrees must be separated by a comma, input: '" + childrenStr + "'");
            }
            if (!childrenStr.isEmpty() && childrenStr.charAt(0) == ',') {
                childrenStr = childrenStr.substring(1);
            }

            ParseResult result = parsePostfixRecursive(childrenStr);
            children.add(result.node);
            childrenStr = result.remaining;
            expectComma = true;
        }

        return children;
    }

    public String leftParentheticRepresentation() {
        StringBuilder sb = new StringBuilder();
        buildLeftParentheticRepresentation(sb);
        return sb.toString();
    }

    private void buildLeftParentheticRepresentation(StringBuilder sb) {
        sb.append(name);

        if (firstChild != null) {
            sb.append("(");
            firstChild.buildLeftParentheticRepresentation(sb);

            Node sibling = firstChild.nextSibling;
            while (sibling != null) {
                sb.append(",");
                sibling.buildLeftParentheticRepresentation(sb);
                sibling = sibling.nextSibling;
            }

            sb.append(")");
        }
    }
}