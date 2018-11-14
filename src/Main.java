import compiler.Lexer;
import struct.NonTerminal;
import struct.Token;
import struct.TreeNode;
import test.LexerTest;
import test.ParserTest;
import util.FileManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //LexerTest.test();
        ParserTest.test();
//        ArrayList ch1 = new ArrayList<TreeNode>() {{
//            add(a1);
//            add(b1);
//            add(c1);
//        }};
//
//        root.setChildren(ch1);
//
//        TreeNode a2 = new NonTerminal(NonTerminal.TYPE.if_stm);
//        TreeNode b2 = new NonTerminal(NonTerminal.TYPE.while_stm);
//        ArrayList ch2 = new ArrayList<TreeNode>() {{
//            add(a2);
//            add(b2);
//        }};
//        a1.setChildren(ch2);
//        c1.setChildren(ch2);
//        TreeNode ab = new NonTerminal(NonTerminal.TYPE.md_op);
//        a2.addChildren(ab);
//        System.out.println(TreeNode.print(root));
//        System.out.println(b1.getRight() == null ? "true" : "false");
    }
}
