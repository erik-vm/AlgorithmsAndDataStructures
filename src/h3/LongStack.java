package h3;

import java.util.LinkedList;

public class LongStack {

    // pinu hoidmiseks
    private LinkedList<Long> stack;

    public LongStack() {
        stack = new LinkedList<>();
    }

    // loob koopia antud LongStack objektist
    @Override
    public Object clone() throws CloneNotSupportedException {
        LongStack clone = new LongStack();
        clone.stack = new LinkedList<>(this.stack);
        return clone;
    }

    // kontrollib kas pinu on tühi
    public boolean stEmpty() {
        return stack.isEmpty();
    }

    // lisab pinu tippu ehk viimaseks uue elemendi
    public void push(long a) {
        stack.add(a);
    }

    // eemaldab ja tagastab viimase elemendi pinust
    public long pop() {
        if (stEmpty()) {
            throw new RuntimeException("Error: Unable to pop from the stack! Stack is empty!");
        }
        return stack.removeLast();
    }

    // kontrollib kas pinust on piisavalt elemente ja kui on, siis teostab tehte kahe elemndi vahel
    public void op(String s) {
        if (stack.size() < 2) {
            throw new RuntimeException("Error: Not enough elements for " + s + " operation!");
        }
        long a = pop();
        long b = pop();
        switch (s) {
            case "+":
                push(a + b);
                break;
            case "-":
                push(b - a);
                break;
            case "*":
                push(a * b);
                break;
            case "/":
                if (a == 0) {
                    throw new RuntimeException("Error: Division by zero when processing '/' operation!");
                }
                push(b / a);
                break;
            default:
                throw new RuntimeException("Error: Unknown operator! Invalid operation: " + s);
        }
    }

    // tagastab viimase elemendi seda pinust eemaldamata
    public long tos() {
        if (stEmpty()) {
            throw new RuntimeException("Error: Cant get last element! Stack is empty!");
        }
        return stack.getLast();
    }

    // kontrollib kas kaks LongStack objekti on omavahel võrdsed. kontrollides ka nende pinu
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongStack)) return false;
        LongStack other = (LongStack) o;
        return this.stack.equals(other.stack);
    }

    // tagastab pinu sisu strigina
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Long val : stack) {
            sb.append(val).append(" ");
        }
        return sb.toString().trim();
    }

//    // kontrollib avaldist ja tagastab tulemuse, kui on viga, siis viskab erindi
//    public static long interpret(String pol) {
//        if (pol == null || pol.trim().isEmpty()) {
//            throw new RuntimeException("Error: Expression is null or empty! Input: " + pol);
//        }
//
//        LongStack longStack;
//        try {
//            longStack = getLongStack(pol);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Error while processing expression: " + pol + ". " + e.getMessage());
//        }
//
//        if (longStack.stEmpty()) {
//            throw new RuntimeException("Error: Expression leaves too many operands! Input: " + pol);
//        }
//
//        long result = longStack.pop();
//
//        if (!longStack.stEmpty()) {
//            throw new RuntimeException("Error: Expression leaves redundant elements on the stack! Input: " + pol);
//        }
//        return result;
//    }

    public static long interpret(String pol) {
        if (pol == null || pol.trim().isEmpty()) {
            throw new RuntimeException("The expression is empty or missing: '" + pol + "'.");
        }
        LongStack stack = new LongStack();
        String[] tokens = pol.trim().split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (token.matches("-?\\d+")) {
                stack.push(Long.parseLong(token));
            } else if (token.matches("[+\\-*/]")) {
                try {
                    stack.op(token);
                } catch (RuntimeException e) {
                    throw new RuntimeException("Error in expression: '" + pol + "'. " + e.getMessage());
                }
            } else if (token.equals("SWAP")) {
                if (stack.stack.size() < 2) {
                    throw new RuntimeException("Not enough elements for SWAP: '" + pol + "'.");
                }
                long b = stack.pop();
                long a = stack.pop();
                stack.push(b);
                stack.push(a);
            } else if (token.equals("ROT")) {
                if (stack.stack.size() < 3) {
                    throw new RuntimeException("Not enough elements for ROT: '" + pol + "'.");
                }
                long c = stack.pop();
                long b = stack.pop();
                long a = stack.pop();
                stack.push(b);
                stack.push(c);
                stack.push(a);
            } else if (token.equals("DUP")) {
                if (stack.stack.size() < 1) {
                    throw new RuntimeException("Not enough elements for DUP: '" + pol + "'.");
                }
                long a = stack.tos();
                stack.push(a);
            } else if (token.equals("DROP")) {
                if (stack.stack.size() < 1) {
                    throw new RuntimeException("Not enough elements for DROP: '" + pol + "'.");
                }
                stack.pop();
            } else {
                throw new RuntimeException("Invalid term in expression: '" + pol + "'. Found: '" + token + "'.");
            }
        }
        if (stack.stack.size() > 1) {
            throw new RuntimeException("The expression contains too many numbers: '" + pol + "'.");
        }
        return stack.pop();
    }


    // tagastab Lonstack objekti postfiks kujust
    private static LongStack getLongStack(String pol) {
        LongStack longStack = new LongStack();
        String[] tokens = pol.trim().split("\\s+");

        for (String token : tokens) {
            try {
                long val = Long.parseLong(token);
                longStack.push(val);
            } catch (NumberFormatException e) {
                if ("+-*/".contains(token)) {
                    longStack.op(token);
                } else {
                    throw new RuntimeException("Illegal symbol in expression: '" + token + "' in input: " + pol);
                }
            }
        }
        return longStack;
    }

    public static void main(String[] args) {
        System.out.println(interpret("4  DUP DROP DROP"));
    }
}