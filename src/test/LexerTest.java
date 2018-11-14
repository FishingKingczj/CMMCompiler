package test;

import compiler.Lexer;
import util.FileManager;

public class LexerTest {
    private static final String inputFile = "test/lex/lex_test.txt";
    private static final String outFile = "test/lex/lex_result.txt";

    public static void test() {
        String content = FileManager.read(inputFile);
        Lexer lexer1 = new Lexer(content);
        lexer1.execute();
        String result = lexer1.result();
        System.out.println(result);
        FileManager.write(outFile, result);
    }
}
