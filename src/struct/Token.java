package struct;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 单词类
 * 储存单词的内容，位置和类型
 */
public class Token extends TreeNode {
    private TYPE type;
    private int line;
    private int position;
    private String content;

    public Token(TYPE type) {
        this.type = type;
    }

    public Token(TYPE type, int line, int position, String content) {
        this.type = type;
        this.line = line;
        this.position = position;
        this.content = content;
    }

    public Token(TreeNode parent, TYPE type, int line, int position, String content) {
        super(parent, null);
        this.type = type;
        this.line = line;
        this.position = position;
        this.content = content;
    }

    /**
     * getters and setters
     */

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 记录所有token的类型
     */
    public enum TYPE {
        /* 运算符 */
        //  +
        ADD("ADD", "+"),
        //  -
        MINUS("MINUS", "-"),
        //  *
        MUL("MUL", "*"),
        //  /
        DIV("DIV", "/"),
        //  <
        LT("LT", "<"),
        //  >
        GT("GT", ">"),
        //  ==
        EQ("EQ", "=="),
        //  <>
        NEQ("NEQ", "<>"),
        //  =
        ASSIGN("ASSIGN", "="),

        /* 保留字 */
        READ("READ", "read"),
        WRITE("WRITE", "write"),
        WHILE("WHILE", "while"),
        IF("IF", "if"),
        ELSE("ELSE", "else"),
        INT("INT", "int"),
        REAL("REAL", "real"),
        BOOL("BOOL", "bool"),
        TRUE("TRUE", "true"),
        FALSE("FALSE", "false"),
        FOR("FOR", "for"),

        /* 分隔符*/
        //  {
        LBRACE("LBRACE", "{"),
        //  }
        RBRACE("RBRACE", "}"),
        //  [
        LBRACKET("LBRACKET", "["),
        //  ]
        RBRACKET("RBRACKET", "]"),
        //  (
        LPAREN("LPAREN", "("),
        //  )
        RPAREN("RPAREN", ")"),
        //  ;
        SEMI("SEMI", ";"),

        /* 注释符 */
        //  注释
        COM("COM", ""),

        /* 标识符与字面值 */
        ID("ID", "标识符"),
        NUM_INT("NUM_INT", "整数字面值"),
        NUM_REAL("NUM_REAL", "实数字面值"),

        /* 错误 */
        ERROR("ERROR", "错误"),

        /* 结束符 */
        END("END", "结束");

        //同时给enum记录字面值是为了便于打印
        private String str;
        private String con;

        //保留字列表
        public static final ArrayList<String> RESERVEDSTR =
                new ArrayList<>(Arrays.asList("if", "else", "while", "read", "write", "int", "real", "bool", "true", "false", "for"));

        TYPE(String str, String con) {
            this.str = str;
            this.con = con;
        }

        public String toString() {
            return str;
        }

        public String toContent() {
            return con;
        }

        public static TYPE getType(String str) {
            switch (str) {
                case "read":
                    return READ;
                case "write":
                    return WRITE;
                case "while":
                    return WHILE;
                case "if":
                    return IF;
                case "else":
                    return ELSE;
                case "int":
                    return INT;
                case "real":
                    return REAL;
                case "bool":
                    return BOOL;
                case "true":
                    return TRUE;
                case "false":
                    return FALSE;
                case "for":
                    return FOR;
                default:
                    return ERROR;
            }
        }
    }
}
