package org.wpy.interpreter;

import com.sun.deploy.util.StringUtils;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    private Map<String, String> vars = new HashMap<>();
    private Map<String, Map<String, Object>> functions = new HashMap<>();

    public String parseInput(String input) {
        Token tokens = new Token(input);
        if (tokens.length() == 0) {
            return "";
        }
        String token = tokens.getToken();
        if ("fn".equalsIgnoreCase(token)) {
            this.parseFunction(tokens);
            return "";
        }
        tokens.ungetToken();
        String val = this.parseExpression(tokens, null);
        if (tokens.length() != 0) {
            throwRuntimeException("Error: Illegal Expression %s", input);
        }
        return val;
    }

    private String parseExpression(Token tokens, Map<String, String> env) {
        if (this.isAssignment(tokens)) {
            return this.parseAssignment(tokens);
        }
        String v1 = this.parseTerm(tokens, env);
        if (Pattern.compile("[a-zA-Z]*").matcher(v1).matches()) {
            throwRuntimeException("Error: Undefined variable %s", v1);
        }
        Integer ret = Integer.valueOf(v1);
        for (; ; ) {
            String token = tokens.getToken();
            if (!"+".equals(token) && !"-".equals(token)) {
                tokens.ungetToken();
                break;
            }

            String v2 = this.parseTerm(tokens, env); // 2
            if (Pattern.compile("[a-zA-Z]*").matcher(v2).matches()) {
                throwRuntimeException("Error: Undefined variable %s", v2);
            }

            if ("+".equals(token)) {
                ret += Integer.valueOf(v2);
            } else if ("-".equals(token)) {
                ret -= Integer.valueOf(v2);
            } else {
                tokens.ungetToken();
            }
        }
        return ret.toString();
    }

    private String parseTerm(Token tokens, Map<String, String> env) {
        String v1 = this.parsePrimaryExpression(tokens, env); // 1
        Integer ret = null;
        if (isInteger(v1)) {
            ret = Integer.valueOf(v1);
        }
        for (; ; ) {
            String token = tokens.getToken();
            if (!"*".equals(token) && !"/".equals(token) && !"%".equals(token)) {
                tokens.ungetToken();
                break;
            }

            String v2 = this.parsePrimaryExpression(tokens, env);

            if ("*".equals(token)) {
                ret *= Integer.valueOf(v2);
            } else if ("/".equals(token)) {
                ret /= Integer.valueOf(v2);
            } else if ("%".equals(token)) {
                ret %= Integer.valueOf(v2);
            }
        }

        return ret != null ? ret.toString() : v1;
    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches();
    }

    private String parsePrimaryExpression(Token tokens, Map<String, String> env) {
        String token = tokens.getToken();

        boolean minusFlag = false;
        if ("-".equals(token)) {
            minusFlag = true;
        } else {
            tokens.ungetToken();
        }

        token = tokens.getToken();

        String v = "";
        if (Pattern.compile("\\d").matcher(token).matches()) {
            Integer integer = Integer.valueOf(token);
            if (minusFlag) {
                integer = -integer;
            }
            v = integer.toString();
        } else if (Pattern.compile("[a-zA-Z]*").matcher(token).matches()) {
            if (this.functions.containsKey(token)) {
                Map<String, Object> fn = this.functions.get(token);
                v = this.executeFunction(fn, tokens);
            } else if (null != env && env.containsKey(token)) {
                v = this.parsePrimaryExpression(new Token(env.get(token)), null);
            } else if (this.vars.containsKey(token)) {
                v = this.vars.get(token);
            } else {
                v = token;
            }
        } else if ("(".equals(token)) {
            v = this.parseExpression(tokens, env);
            tokens.getToken(); // pass `)`
        } else {
            tokens.ungetToken();
        }
        return v;
    }

    private String executeFunction(Map<String, Object> fn, Token tokens) {
        List<String> params = (List<String>) fn.get("params");
        String[] block = (String[]) fn.get("block");
        Map<String, String> paramObj = new HashMap<>();
        params.stream().forEach(p -> {
            paramObj.put(p, this.parseExpression(tokens, null));
        });
        return this.parseExpression(new Token(StringUtils.join(Arrays.asList(block), "")), paramObj);
    }

    private String parseAssignment(Token tokens) {
        String name = tokens.getToken();
        if (this.functions.containsKey(name)) {
            throwRuntimeException("Error: %s is a function", name);
        }
        tokens.getToken(); // pass `=`
        String v = this.parseExpression(tokens, null);
        this.vars.put(name, v);
        return v;
    }

    private boolean isAssignment(Token tokens) {
        if (tokens.length() <= 2) {
            return false;
        }
        tokens.getToken();
        String token = tokens.getToken();
        tokens.ungetToken();
        tokens.ungetToken();
        return "=".equals(token);
    }

    private void parseFunction(Token tokens) {
        String fn = tokens.getToken();
        if (this.vars.containsKey(fn)) {
            throwRuntimeException("Error: %s is a variable", fn);
        }
        List<String> params = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        String token = tokens.getToken();
        while (!"=".equals(token)) {
            if (paramMap.containsKey(token)) {
                throwRuntimeException("Error: params conflict %s", token);
            }
            paramMap.put(token, "1");
            params.add(token);
            token = tokens.getToken();
        }
        token = tokens.getToken();
        if (!">".equals(token)) {
            throwRuntimeException("Error: illegal function definition");
        }
        String[] block = tokens.copy();
        if (block.length == 0) {
            throwRuntimeException("Error: illegal function definition");
        }
        for (int i = 0; i < block.length; i++) {
            token = block[i];
            if (Pattern.compile("[a-zA-Z]*").matcher(token).matches()) {
                if (paramMap.get(token) == null) {
                    throwRuntimeException("ERROR: Unknown identifier '%s'", token);
                }
            }
        }
        this.functions.put(fn, new HashMap<String, Object>() {{
            put("params", params);
            put("block", block);
        }});
    }

    private void throwRuntimeException(String mes, String... param) {
        throw new RuntimeException(String.format(mes, param));
    }

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();

        for (; ; ) {
            System.out.println(">>>");
            Scanner scan = new Scanner(System.in);
            String input = scan.nextLine();

            if ("exit".equals(input) || "exit()".equals(input)) {
                System.out.println("bye bye!");
                break;
            }
            String ret = interpreter.parseInput(input);
            System.out.println(ret);
        }

    }

}

class Token {
    private String[] tokens;
    private int idx;

    public Token(String token) {
        if (token == null) {
            token = "";
        }
        String regex = "\\s*([-+*\\/%=\\(\\)\\>]|[A-Za-z_][A-Za-z0-9_]*|[0-9]*\\.?[0-9]+)\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(token);
        String[] array = new String[0];
        while (matcher.find()) {
            String val = matcher.group().trim();
            if (Pattern.compile("\\s*").matcher(val).matches()) {
                continue;
            }
            String[] cloned = new String[array.length + 1];
            System.arraycopy(array, 0, cloned, 0, cloned.length - 1);
            cloned[cloned.length - 1] = val;
            array = cloned;
        }
        this.tokens = array;
    }

    public String getToken() {
        if (this.idx > this.tokens.length) {
            throw new RuntimeException("Wrong Expression");
        }
        if (this.idx == this.tokens.length) {
            this.idx++;
            return null;
        }
        return tokens[this.idx++];
    }

    public void ungetToken() {
        this.idx--;
    }

    public String[] copy() {
        return Arrays.copyOfRange(this.tokens, idx, this.tokens.length);
    }

    public int length() {
        return this.tokens.length - this.idx;
    }

}