package compiler;

import struct.CompileError;
import struct.Token;


import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 词法分析器
 */
public class Lexer {
    //待解析的内容
    private String content;

    //保存所有token
    private ArrayList<Token> tokenArrayList;
    //记录下一个字符位置
    private int charPos;
    //当前读取字符
    private char currentChar;
    //记录当前识别的行号和位置
    private int line;
    private int position;
    //字符缓冲
    private StringBuilder sb;
    //保存所有错误
    private ArrayList<CompileError> errorArrayList;

    public Lexer(String content) {
        this.content = content;

        tokenArrayList = new ArrayList<>();
        charPos = 0;
        line = 1;
        position = 1;
        sb = new StringBuilder();
        errorArrayList = new ArrayList<>();
    }

    /**
     * 执行内容解析
     */
    public void execute() {
        tokenArrayList.clear();
        errorArrayList.clear();

        while (charPos < content.length()) {
            //读取字符
            currentChar = content.charAt(charPos++);
            resolve();
        }
    }

    /**
     * 分析当前字符
     */
    private void resolve() {
        //分析标识符或保留字
        if (Character.isLetter(currentChar) || currentChar == '_') {
            resolveLetter();
        }
        //分析数字
        else if (Character.isDigit(currentChar) || currentChar == '.') {
            resolveDigit();
        }
        //分析斜杠
        else if (currentChar == '/') {
            resolveSlash();
        }
        //换行
        else if (currentChar == '\n') {
            line++;
            position = 1;
        }
        //忽略空字符
        else if (currentChar == ' ' || currentChar == '\t') {
            return;
        }
        //其他字符
        else {
            resolveOther();
        }
    }

    /**
     * 分析字母字符
     */
    private void resolveLetter() {
        //不断读取直到读取到非字母数字或下划线
        do {
            sb.append(currentChar);
            if (charPos == content.length()) {
                //此时不需要再往下读字符了
                charPos++;
                break;
            }
            currentChar = content.charAt(charPos++);
        } while (Character.isLetter(currentChar) ||
                Character.isDigit(currentChar) ||
                currentChar == '_');
        //需要回退一个往后看的字符
        charPos--;

        String word = sb.toString();

        //TODO: TEST
        //System.out.println(word);

        //判断是否为保留字
        for (String reserved : Token.TYPE.RESERVEDSTR) {
            if (word.equals(reserved)) {
                tokenArrayList.add(buildToken(Token.TYPE.getType(word), word));
                //清空缓冲
                sb.delete(0, sb.length());
                return;
            }
        }

        //不是保留字则为标识符
        //若开头或结尾为下划线，标识符非法
        if (word.charAt(0) == '_' || word.charAt(word.length() - 1) == '_') {
            Token token = buildToken(Token.TYPE.ERROR, word);
            errorArrayList.add(CompileError.LexError(token, "错误的标识符"));
            tokenArrayList.add(token);
        } else {
            tokenArrayList.add(buildToken(Token.TYPE.ID, word));
        }
        //清空缓冲
        sb.delete(0, sb.length());
    }

    /**
     * 分析数字字符
     */
    private void resolveDigit() {
        //不断读取直到读取到非数字或点
        do {
            sb.append(currentChar);
            if (charPos == content.length()) {
                //此时不需要再往下读字符了
                charPos++;
                break;
            }
            currentChar = content.charAt(charPos++);
        } while (Character.isDigit(currentChar) ||
                currentChar == '.');
        //需要回退一个往后看的字符
        charPos--;

        String word = sb.toString();

        //TODO: TEST
        //System.out.println(word);

        //实数
        if (word.contains(".")) {
            if (!Pattern.matches("^(([1-9][0-9]*|0?).[0-9]+)$", word)) {
                Token token = buildToken(Token.TYPE.ERROR, word);
                errorArrayList.add(CompileError.LexError(token, "错误的实数"));
                tokenArrayList.add(token);
            } else {
                tokenArrayList.add(buildToken(Token.TYPE.NUM_REAL, word));
            }
        }
        //整数
        else {
            //若首位为0，出错
            if (word.charAt(0) == '0' && !word.equals("0")) {
                Token token = buildToken(Token.TYPE.ERROR, word);
                errorArrayList.add(CompileError.LexError(token, "错误的整数"));
                tokenArrayList.add(token);
            } else {
                tokenArrayList.add(buildToken(Token.TYPE.NUM_INT, word));
            }
        }

        //清空缓冲
        sb.delete(0, sb.length());
    }

    /**
     * 分析斜杠
     */
    private void resolveSlash() {
        switch (content.charAt(charPos)) {
            case '/':
                while (true) {
                    if (content.charAt(charPos++) == '\n') {
                        tokenArrayList.add(buildToken(Token.TYPE.COM, ""));
                        //注意换行
                        line++;
                        position = 1;
                        break;
                    }
                }
                break;
            case '*':
                while (true) {
                    char ch = content.charAt(charPos++);
                    if (ch == '*') {
                        if (content.charAt(charPos) == '/') {
                            tokenArrayList.add(buildToken(Token.TYPE.COM, ""));
                            charPos++;
                            break;
                        }
                    } else {
                        if (ch == '\n') {
                            tokenArrayList.add(buildToken(Token.TYPE.COM, ""));
                            line++;
                            position = 1;
                            position = 1;
                        }
                    }
                }
                break;
            default:
                tokenArrayList.add(buildToken(Token.TYPE.DIV, "/"));
        }
    }

    /**
     * 分析其他字符
     */
    private void resolveOther() {
        switch (currentChar) {
            //关系
            case '=':
                if (content.charAt(charPos) == '=') {
                    tokenArrayList.add(buildToken(Token.TYPE.EQ, "=="));
                    charPos++;
                } else {
                    tokenArrayList.add(buildToken(Token.TYPE.ASSIGN, "="));
                }
                break;
            case '<':
                if (content.charAt(charPos) == '>') {
                    tokenArrayList.add(buildToken(Token.TYPE.NEQ, "<>"));
                    charPos++;
                } else {
                    tokenArrayList.add(buildToken(Token.TYPE.LT, "<"));
                }
                break;
            case '>':
                tokenArrayList.add(buildToken(Token.TYPE.GT, ">"));
                break;

            //算术
            case '+':
                tokenArrayList.add(buildToken(Token.TYPE.ADD, "+"));
                break;
            case '-':
                tokenArrayList.add(buildToken(Token.TYPE.MINUS, "-"));
                break;
            case '*':
                tokenArrayList.add(buildToken(Token.TYPE.MUL, "*"));
                break;

            //括号分号
            case '(':
                tokenArrayList.add(buildToken(Token.TYPE.LPAREN, "("));
                break;
            case ')':
                tokenArrayList.add(buildToken(Token.TYPE.RPAREN, ")"));
                break;
            case '[':
                tokenArrayList.add(buildToken(Token.TYPE.LBRACKET, "["));
                break;
            case ']':
                tokenArrayList.add(buildToken(Token.TYPE.RBRACKET, "]"));
                break;
            case '{':
                tokenArrayList.add(buildToken(Token.TYPE.LBRACE, "{"));
                break;
            case '}':
                tokenArrayList.add(buildToken(Token.TYPE.RBRACE, "}"));
                break;
            case ';':
                tokenArrayList.add(buildToken(Token.TYPE.SEMI, ";"));
                break;

            //错误
            default:
                Token token = buildToken(Token.TYPE.ERROR, String.valueOf(currentChar));
                tokenArrayList.add(token);
                errorArrayList.add(CompileError.LexError(token, "无法识别的符号"));
        }
    }

    /**
     * 构建新的token
     */
    public Token buildToken(Token.TYPE type, String content) {
        Token token = new Token(type, line, position, content);
        position++;
        return token;
    }

    /**
     * 生成分析结果
     *
     * @return 分析结果
     */
    public String result() {
        String result = "";
        int printLine = 1;
        //打印词法表
        for (Token token : tokenArrayList) {
            while (token.getLine() != printLine) {
                result += "\n";
                printLine++;
            }
            result += token.getType().toString() + " ";
        }

        if (!errorArrayList.isEmpty()) {
            result += "\n\n------------------------------\nErrors:\n";
            //打印错误信息
            for (CompileError error : errorArrayList) {
                result += error.getMessage() + "\n";
            }
        }
        return result;
    }

    public ArrayList<Token> getParserArrayList() {
        ArrayList<Token> newArrayList = (ArrayList<Token>) tokenArrayList.clone();
        ArrayList<Token> delete = new ArrayList<>();
        for (Token token : newArrayList) {
            if (token.getType() == Token.TYPE.COM) {
                delete.add(token);
            }
        }
        newArrayList.removeAll(delete);
        return newArrayList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //词法是否有误
    public boolean hasError() {
        return !errorArrayList.isEmpty();
    }
}
