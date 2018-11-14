package struct;

import java.util.ArrayList;

/**
 * 非终结符类
 * 储存非终结符的内容，位置和类型
 */
public class NonTerminal extends TreeNode {
    private TYPE type;
    private int line;
    private int position;

    public NonTerminal(TYPE type) {
        this.type = type;
    }

    public NonTerminal(TreeNode parent, ArrayList<TreeNode> children, TYPE type) {
        super(parent, children);
        this.type = type;
    }

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

    /**
     * 记录所有非终结符的类型
     */
    public enum TYPE {
        program("program", "程序"),
        sub_program("sub_program", "子程序"),
        statement("statement", "语句"),
        body("body", "语句块"),
        if_stm("if_stm", "IF语句"),
        if_stm_1("if_stm_1", "ELSE语句"),
        while_stm("while_stm", "WHILE语句"),
        for_stm("for_stm", "FOR语句"),
        for_stm_1("for_stm_1", "FOR子句"),
        read_stm("read_stm", "READ语句"),
        write_stm("write_stm", "WRITE语句"),
        assign_stm("assign_stm", "赋值语句"),
        declare_stm("declare_stm", "声明语句"),
        declare_stm_1("declare_stm_1", "声明子句"),
        comparison_op("comparison_op", "比较符号"),
        condition("condition", "判断式"),
        right_value("right_value", "右值"),
        right_value_1("right_value_1", "右值_1"),
        expression("expression", "表达式"),
        expression_1("expression_1", "表达式_1"),
        term("term", "子式"),
        term_1("term_1", "乘除子式_1"),
        am_op("am_op", "加减符号"),
        md_op("md_op", "乘除符号"),
        factor("factor", "值"),
        array("array", "数组"),
        array_num("array_num", "数组下标"),
        error("error", "错误");

        //同时给enum记录字面值是为了便于打印
        private String str;
        private String str_ch;

        private TYPE(String str, String str_ch) {
            this.str = str;
            this.str_ch = str_ch;
        }

        public String toString() {
            return str;
        }

        public String toCH() {
            return str_ch;
        }
    }
}
