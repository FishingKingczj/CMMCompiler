package test;

import compiler.Lexer;
import compiler.Parser;
import util.FileManager;

import java.io.File;

public class ParserTest {
    private static final String DICT = "test/parse";
    private static final String RST = "test/parse/result";

    public static void test() {
        File dict = new File(DICT);
        File[] fileList = dict.listFiles();
        //清空文件
        File[] resultList = new File(RST).listFiles();
        for (File file : resultList) {
            file.delete();
        }

        for (File file : fileList) {
            if (!file.isDirectory()) {
                String out = RST + "/r_" + file.getName();
                String content = FileManager.read(file.getPath());
                //语法分析
                Lexer lexer1 = new Lexer(content);
                lexer1.execute();

                if (!lexer1.hasError()) {
                    Parser parser1 = new Parser(lexer1.getParserArrayList());
                    parser1.execute();
                    String result = parser1.result();
                    System.out.println(result);
                    FileManager.write(out, result);
                } else {
                    System.out.println("Lex error");
                    System.out.println(lexer1.result());
                }
            }
        }
    }
}
