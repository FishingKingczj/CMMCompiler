package struct;

public class CompileError {
    private String message;

    public CompileError(String message) {
        this.message = message;
    }

    /**
     * 词法错误
     *
     * @param token   传入发生错误的token
     * @param message 传入错误信息
     */
    public static CompileError LexError(Token token, String message) {
        return new CompileError("LexerError: line " + token.getLine() + " position " + token.getPosition() +
                ", symbol \"" + token.getContent() + "\" :" + message);
    }

    /**
     * 语法错误
     *
     * @param token   传入发生错误时的token
     * @param message 传入错误信息
     */
    public static CompileError ParserError(Token token, String message) {
        if (token == null) {
            return new CompileError("ParserError: line 0 position 0 :" + message);
        }
        return new CompileError("ParserError: line " + token.getLine()
                + " position " + token.getPosition() + "，符号\"" + token.getContent() + "\"" + " :" + message);
    }

    public String getMessage() {
        return message;
    }
}
