package compiler;

import struct.*;

import java.util.ArrayList;

public class Parser {
    //待解析的单词序列
    private ArrayList<Token> tokenArrayList;
    //根节点
    private TreeNode root;

    //记录当前token位置位置
    private int tokenPos;
    //当前节点
    private TreeNode currentNode;
    //保存所有错误
    private ArrayList<CompileError> errorArrayList;

    public Parser(ArrayList<Token> tokenArrayList) {
        this.tokenArrayList = tokenArrayList;
        tokenPos = 0;
        errorArrayList = new ArrayList<>();
        if (tokenArrayList.isEmpty()) return;

        tokenArrayList.add(new Token(Token.TYPE.END,
                tokenArrayList.get(tokenArrayList.size() - 1).getLine(), tokenArrayList.get(tokenArrayList.size() - 1).getPosition() + 1,
                "END"));
    }

    /**
     * 执行内容解析
     */
    public void execute() {
        errorArrayList.clear();
        root = currentNode = new NonTerminal(NonTerminal.TYPE.program);

        while (tokenPos < tokenArrayList.size()) {
            if (currentNode == null)
                return;
            parse();
        }
    }

    /**
     * 对当前节点进行解析
     */
    private void parse() {
        //TODO:TEST
//        System.out.println(TreeNode.print(root));
        ArrayList<TreeNode> result;
        Token currentToken;
        //当前节点若为非终结符
        if (currentNode.getClass() == NonTerminal.class) {
            NonTerminal node = (NonTerminal) currentNode;
            //获取扩展子树
            currentToken = tokenArrayList.get(tokenPos);
            result = PredictTable.predict(node.getType(), currentToken.getType());
            //若找不到匹配，报错并处理错误
            if (result == null) {
                nonTerminalError();
                return;
            }
            //进行扩展
            currentNode = extend(result);
        }
        //当前节点若为终结符
        else {
            currentToken = tokenArrayList.get(tokenPos);
            Token node = (Token) currentNode;
            //匹配消除，移进单词序列位置
            if (node.getType() == currentToken.getType()) {
                tokenPos++;
            }
            //若无法匹配，报错并处理错误
            else {
                terminalError();
                return;
            }
            if (node.getType() == Token.TYPE.END) {
                currentNode.getParent().removeChildren(currentNode);
                return;
            }
            //取下一个节点为当前节点，结束本轮分析
            currentNode = nextNode(currentNode);
        }
    }

    /**
     * 扩展子树
     * 若当前类型转换为空，则在父节点中去掉此类型，取下一节点为当前节点
     * 若当前类型为中间类型，则将中间类型进行替换
     */
    private TreeNode extend(ArrayList<TreeNode> result) {
        //当前类型转换为空
        if (result.isEmpty()) {
            TreeNode next = nextNode(currentNode);
            currentNode.getParent().removeChildren(currentNode);
            return next;
        }

        NonTerminal.TYPE type = ((NonTerminal) currentNode).getType();
        //当前类型为中间类型，可以直接替换
        if (type == NonTerminal.TYPE.if_stm_1 ||
                type == NonTerminal.TYPE.for_stm_1 ||
                type == NonTerminal.TYPE.declare_stm_1 ||
                type == NonTerminal.TYPE.right_value_1 ||
                type == NonTerminal.TYPE.expression_1 ||
                type == NonTerminal.TYPE.term_1 ||
                type == NonTerminal.TYPE.am_op ||
                type == NonTerminal.TYPE.md_op ||
                type == NonTerminal.TYPE.array_num ||
                type == NonTerminal.TYPE.sub_program ||
                type == NonTerminal.TYPE.body ||
                (type == NonTerminal.TYPE.program && currentNode != root)) {
            currentNode.getParent().replaceChildren(currentNode, result);
        } else {
            currentNode.setChildren(result);
        }
        //取左子节点为当前节点
        return result.get(0);
    }

    /**
     * 当前节点为终结符时
     * 成功识别终结符并移进识别位置后取右节点为当前节点
     * 若当前节点没有右节点
     * 取父节点右节点为当前节点
     *
     * @return 下一个节点
     */
    private TreeNode nextNode(TreeNode node) {
        TreeNode next = node.getRight();
        if (next == null) {
            if (node.getParent() == null)
                return null;
            next = nextNode(node.getParent());
        }
        return next;
    }

    /**
     * 当前节点为终结符时
     * 但是单词无法消除
     * 非终结符缺失或不匹配
     * 抛弃符号直到遇到下一个非终结符
     * 并进行恢复直到能够重新匹配当前节点
     */
    private void terminalError() {
        String message = ((NonTerminal) currentNode.getParent()).getType().toCH() + " 缺少: \"" + ((Token) currentNode).getType().toContent() + "\"";
        errorArrayList.add(CompileError.ParserError(tokenArrayList.get(tokenPos - 1), message));

        TreeNode testNode = currentNode;
        //抛弃所有终结符和中间符号
        while (true) {
            if (testNode.getClass() == NonTerminal.class) {
                NonTerminal.TYPE type = ((NonTerminal) testNode).getType();
                if (type == NonTerminal.TYPE.program ||
                        type == NonTerminal.TYPE.sub_program ||
                        type == NonTerminal.TYPE.statement ||
                        type == NonTerminal.TYPE.body ||
                        type == NonTerminal.TYPE.if_stm ||
                        type == NonTerminal.TYPE.while_stm ||
                        type == NonTerminal.TYPE.for_stm ||
                        type == NonTerminal.TYPE.read_stm ||
                        type == NonTerminal.TYPE.assign_stm ||
                        type == NonTerminal.TYPE.declare_stm ||
                        type == NonTerminal.TYPE.condition) {
                    break;
                }
            }
            //下一个节点，并把当前节点替换为ERROR
            TreeNode next = nextNode(testNode);
            testNode.getParent().replaceChildren(testNode, new ArrayList<TreeNode>() {{
                add(new Token(Token.TYPE.ERROR));
            }});
            testNode = next;
        }
        currentNode = testNode;
        recover();
    }

    /**
     * 当前节点为非终结符时
     * 如果非终结符为能转换为空的中间非终结符
     * 则删除能转为空的非终结符非终结符直到找到错误的终结符
     * 如果非终结符为普通终结符
     * 则跳过符号串中的符号直到能够重新匹配当前节点。
     */
    private void nonTerminalError() {
        NonTerminal node = (NonTerminal) currentNode;
        NonTerminal.TYPE type = node.getType();
        //删除能转为空的非终结符
        if (type == NonTerminal.TYPE.if_stm_1 ||
                type == NonTerminal.TYPE.declare_stm_1 ||
                type == NonTerminal.TYPE.right_value_1 ||
                type == NonTerminal.TYPE.expression_1 ||
                type == NonTerminal.TYPE.term_1 ||
                type == NonTerminal.TYPE.array ||
                type == NonTerminal.TYPE.sub_program) {
            currentNode = nextNode(currentNode);
            node.getParent().removeChildren(node);
        } else if (type == NonTerminal.TYPE.program) {
            errorArrayList.add(CompileError.ParserError(tokenArrayList.get(tokenPos),
                    "错误的开始符号： \"" + tokenArrayList.get(tokenPos).getType().toContent() + "\""));
            recover();
        } else {
            errorArrayList.add(CompileError.ParserError(tokenArrayList.get(tokenPos - 1),
                    ((NonTerminal) currentNode.getParent()).getType().toCH() +
                            " 缺少: " + ((NonTerminal) currentNode).getType().toCH() + ""));

            TreeNode tmpNode = currentNode;
            currentNode = nextNode(currentNode);
            tmpNode.getParent().replaceChildren(tmpNode, new ArrayList<TreeNode>() {{
                add(new Token(Token.TYPE.ERROR));
            }});
        }
    }

    /**
     * 出错之后的恢复函数
     */
    private void recover() {
        //TODO:TEST
//        System.out.println("RECOVER\n"+TreeNode.print(root));
//        System.out.println("CURR\n"+ (currentNode.getClass()==Token.class?((Token) currentNode).getType():((NonTerminal) currentNode).getType()));
//        System.out.println("POS\n"+tokenArrayList.get(tokenPos).getType());
        while (true) {
            //这里的问题是：如果tokenPos为END时就不应该继续恢复了
            if (tokenPos < tokenArrayList.size() - 1) {
//                if (currentNode.getClass() == NonTerminal.class) {
                //非终结符，找到能进行匹配的节点
                if (PredictTable.predict(((NonTerminal) currentNode).getType(), tokenArrayList.get(tokenPos).getType()) == null) {
                    tokenPos++;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * 生成分析结果
     *
     * @return 分析结果
     */
    public String result() {
        String result = "";
        result += TreeNode.print(root);

        //打印错误信息
        if (!errorArrayList.isEmpty()) {
            result += "\n\n------------------------------\nErrors:\n";
            for (CompileError error : errorArrayList) {
                result += error.getMessage() + "\n";
            }
        }
        return result;
    }

    public TreeNode getTree() {
        return root;
    }

    //词法是否有误
    public boolean hasError() {
        return errorArrayList.isEmpty();
    }
}
