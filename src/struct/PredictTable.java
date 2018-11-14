package struct;

import java.util.ArrayList;

public class PredictTable {

    /**
     * 根据文法的左部和匹配符号返回生成式
     * 如果没有匹配则返回null
     *
     * @param from   文法的左部
     * @param select 匹配符号
     * @return 文法生成式
     */
    public static ArrayList<TreeNode> predict(NonTerminal.TYPE from, Token.TYPE select) {
        ArrayList<TreeNode> result = new ArrayList<>();
        switch (from) {
            case program:
                switch (select) {
                    case IF:
                    case WHILE:
                    case FOR:
                    case READ:
                    case WRITE:
                    case ID:
                    case INT:
                    case REAL:
                    case BOOL:
                        result.add(gen(NonTerminal.TYPE.statement));
                        break;
                    case END:
                        result.add(gen(select));
                        return result;
                    default:
                        return null;
                }
                result.add(gen(NonTerminal.TYPE.program));
                return result;
            case sub_program:
                switch (select) {
                    case IF:
                    case WHILE:
                    case FOR:
                    case READ:
                    case WRITE:
                    case ID:
                    case INT:
                    case REAL:
                    case BOOL:
                        result.add(gen(NonTerminal.TYPE.statement));
                        break;
                    case RBRACE:
                        return result;
                    default:
                        return null;
                }
                result.add(gen(NonTerminal.TYPE.sub_program));
                return result;
            case statement:
                switch (select) {
                    case IF:
                        result.add(gen(NonTerminal.TYPE.if_stm));
                        break;
                    case WHILE:
                        result.add(gen(NonTerminal.TYPE.while_stm));
                        break;
                    case FOR:
                        result.add(gen(NonTerminal.TYPE.for_stm));
                        break;
                    case READ:
                        result.add(gen(NonTerminal.TYPE.read_stm));
                        break;
                    case WRITE:
                        result.add(gen(NonTerminal.TYPE.write_stm));
                        break;
                    case ID:
                        result.add(gen(NonTerminal.TYPE.assign_stm));
                        break;
                    case INT:
                    case REAL:
                    case BOOL:
                        result.add(gen(NonTerminal.TYPE.declare_stm));
                        break;
                    default:
                        return null;
                }
                return result;
            case if_stm:
                if (select == Token.TYPE.IF) {
                    result.add(gen(Token.TYPE.IF));
                    result.add(gen(Token.TYPE.LPAREN));
                    result.add(gen(NonTerminal.TYPE.condition));
                    result.add(gen(Token.TYPE.RPAREN));
                    result.add(gen(NonTerminal.TYPE.body));
                    result.add(gen(NonTerminal.TYPE.if_stm_1));
                    return result;
                }
                return null;
            case if_stm_1:
                if (select == Token.TYPE.ELSE) {
                    result.add(gen(Token.TYPE.ELSE));
                    result.add(gen(NonTerminal.TYPE.body));
                    return result;
                }
                return result;
            case while_stm:
                if (select == Token.TYPE.WHILE) {
                    result.add(gen(Token.TYPE.WHILE));
                    result.add(gen(Token.TYPE.LPAREN));
                    result.add(gen(NonTerminal.TYPE.condition));
                    result.add(gen(Token.TYPE.RPAREN));
                    result.add(gen(NonTerminal.TYPE.body));
                    return result;
                }
                return null;
            case for_stm:
                if (select == Token.TYPE.FOR) {
                    result.add(gen(Token.TYPE.FOR));
                    result.add(gen(Token.TYPE.LPAREN));
                    result.add(gen(NonTerminal.TYPE.for_stm_1));
                    result.add(gen(NonTerminal.TYPE.condition));
                    result.add(gen(Token.TYPE.SEMI));
                    result.add(gen(Token.TYPE.ID));
                    result.add(gen(NonTerminal.TYPE.array));
                    result.add(gen(Token.TYPE.ASSIGN));
                    result.add(gen(NonTerminal.TYPE.right_value));
                    result.add(gen(Token.TYPE.RPAREN));
                    result.add(gen(NonTerminal.TYPE.body));
                    return result;
                }
                return null;
            case for_stm_1:
                if (select == Token.TYPE.ID) {
                    result.add(gen(NonTerminal.TYPE.assign_stm));
                    return result;
                } else if (select == Token.TYPE.INT ||
                        select == Token.TYPE.REAL ||
                        select == Token.TYPE.BOOL) {
                    result.add(gen(NonTerminal.TYPE.declare_stm));
                    return result;
                }
                return null;
            case body:
                switch (select) {
                    case IF:
                    case WHILE:
                    case FOR:
                    case READ:
                    case WRITE:
                    case ID:
                    case INT:
                    case REAL:
                    case BOOL:
                        result.add(gen(NonTerminal.TYPE.statement));
                        return result;
                    case LBRACE:
                        result.add(gen(Token.TYPE.LBRACE));
                        result.add(gen(NonTerminal.TYPE.sub_program));
                        result.add(gen(Token.TYPE.RBRACE));
                        return result;
                    default:
                        return null;
                }
            case read_stm:
                if (select == Token.TYPE.READ) {
                    result.add(gen(Token.TYPE.READ));
                    result.add(gen(Token.TYPE.LPAREN));
                    result.add(gen(Token.TYPE.ID));
                    result.add(gen(NonTerminal.TYPE.array));
                    result.add(gen(Token.TYPE.RPAREN));
                    result.add(gen(Token.TYPE.SEMI));
                    return result;
                }
                return null;
            case write_stm:
                if (select == Token.TYPE.WRITE) {
                    result.add(gen(Token.TYPE.WRITE));
                    result.add(gen(Token.TYPE.LPAREN));
                    result.add(gen(NonTerminal.TYPE.right_value));
                    result.add(gen(Token.TYPE.RPAREN));
                    result.add(gen(Token.TYPE.SEMI));
                    return result;
                }
                return null;
            case assign_stm:
                if (select == Token.TYPE.ID) {
                    result.add(gen(Token.TYPE.ID));
                    result.add(gen(NonTerminal.TYPE.array));
                    result.add(gen(Token.TYPE.ASSIGN));
                    result.add(gen(NonTerminal.TYPE.right_value));
                    result.add(gen(Token.TYPE.SEMI));
                    return result;
                }
                return null;
            case declare_stm:
                if (select == Token.TYPE.INT ||
                        select == Token.TYPE.REAL ||
                        select == Token.TYPE.BOOL) {
                    result.add(gen(select));
                    result.add(gen(Token.TYPE.ID));
                    result.add(gen(NonTerminal.TYPE.array));
                    result.add(gen(NonTerminal.TYPE.declare_stm_1));
                    result.add(gen(Token.TYPE.SEMI));
                    return result;
                }
                return null;
            case declare_stm_1:
                if (select == Token.TYPE.ASSIGN) {
                    result.add(gen(Token.TYPE.ASSIGN));
                    result.add(gen(NonTerminal.TYPE.right_value));
                    return result;
                }
                return result;
            case condition:
                switch (select) {
                    case NUM_INT:
                    case NUM_REAL:
                    case ID:
                    case LPAREN:
                        result.add(gen(NonTerminal.TYPE.expression));
                        result.add(gen(NonTerminal.TYPE.comparison_op));
                        result.add(gen(NonTerminal.TYPE.expression));
                        return result;
                    case TRUE:
                    case FALSE:
                        result.add(gen(select));
                        return result;
                    default:
                        return null;
                }
            case comparison_op:
                if (select == Token.TYPE.LT ||
                        select == Token.TYPE.GT ||
                        select == Token.TYPE.EQ ||
                        select == Token.TYPE.NEQ) {
                    result.add(gen(select));
                    return result;
                }
                return null;
            case right_value:
                if (select == Token.TYPE.NUM_INT ||
                        select == Token.TYPE.NUM_REAL ||
                        select == Token.TYPE.ID ||
                        select == Token.TYPE.LPAREN ||
                        select == Token.TYPE.TRUE ||
                        select == Token.TYPE.FALSE ||
                        select == Token.TYPE.MINUS) {
                    result.add(gen(NonTerminal.TYPE.expression));
                    result.add(gen(NonTerminal.TYPE.right_value_1));
                    return result;
                }
                return null;
            case right_value_1:
                if (select == Token.TYPE.LT ||
                        select == Token.TYPE.GT ||
                        select == Token.TYPE.EQ ||
                        select == Token.TYPE.NEQ) {
                    result.add(gen(NonTerminal.TYPE.comparison_op));
                    result.add(gen(NonTerminal.TYPE.expression));
                    return result;
                } else if (select == Token.TYPE.SEMI ||
                        select == Token.TYPE.RPAREN) {
                    return result;
                }
                return null;
            case expression:
                if (select == Token.TYPE.NUM_INT ||
                        select == Token.TYPE.NUM_REAL ||
                        select == Token.TYPE.ID ||
                        select == Token.TYPE.LPAREN ||
                        select == Token.TYPE.MINUS) {
                    result.add(gen(NonTerminal.TYPE.term));
                    result.add(gen(NonTerminal.TYPE.expression_1));
                    return result;
                } else if (select == Token.TYPE.TRUE ||
                        select == Token.TYPE.FALSE) {
                    result.add(gen(select));
                    return result;
                }
                return null;
            case expression_1:
                if (select == Token.TYPE.ADD ||
                        select == Token.TYPE.MINUS) {
                    result.add(gen(NonTerminal.TYPE.am_op));
                    result.add(gen(NonTerminal.TYPE.term));
                    result.add(gen(NonTerminal.TYPE.expression_1));
                    return result;
                } else {
                    Token.TYPE types[] = {Token.TYPE.RPAREN, Token.TYPE.SEMI,
                            Token.TYPE.LT, Token.TYPE.GT,
                            Token.TYPE.EQ, Token.TYPE.NEQ};
                    for (Token.TYPE type : types) {
                        if (select == type) {
                            return result;
                        }
                    }
                }
                return null;
            case term:
                if (select == Token.TYPE.NUM_INT ||
                        select == Token.TYPE.NUM_REAL ||
                        select == Token.TYPE.ID ||
                        select == Token.TYPE.LPAREN ||
                        select == Token.TYPE.MINUS) {
                    result.add(gen(NonTerminal.TYPE.factor));
                    result.add(gen(NonTerminal.TYPE.term_1));
                    return result;
                }
                return null;
            case term_1:
                if (select == Token.TYPE.MUL ||
                        select == Token.TYPE.DIV) {
                    result.add(gen(NonTerminal.TYPE.md_op));
                    result.add(gen(NonTerminal.TYPE.factor));
                    result.add(gen(NonTerminal.TYPE.term_1));
                    return result;
                } else {
                    Token.TYPE types[] = {Token.TYPE.NUM_INT, Token.TYPE.NUM_REAL,
                            Token.TYPE.ID, Token.TYPE.LPAREN,
                            Token.TYPE.RPAREN, Token.TYPE.SEMI,
                            Token.TYPE.ADD, Token.TYPE.MINUS,
                            Token.TYPE.LT, Token.TYPE.GT,
                            Token.TYPE.EQ, Token.TYPE.NEQ};
                    for (Token.TYPE type : types) {
                        if (select == type) {
                            return result;
                        }
                    }
                }
                return null;
            case am_op:
                if (select == Token.TYPE.ADD ||
                        select == Token.TYPE.MINUS) {
                    result.add(gen(select));
                    return result;
                }
                return null;
            case md_op:
                if (select == Token.TYPE.MUL ||
                        select == Token.TYPE.DIV) {
                    result.add(gen(select));
                    return result;
                }
                return null;
            case factor:
                switch (select) {
                    case NUM_INT:
                    case NUM_REAL:
                        result.add(gen(select));
                        return result;
                    case LPAREN:
                        result.add(gen(Token.TYPE.LPAREN));
                        result.add(gen(NonTerminal.TYPE.expression));
                        result.add(gen(Token.TYPE.RPAREN));
                        return result;
                    case ID:
                        result.add(gen(Token.TYPE.ID));
                        result.add(gen(NonTerminal.TYPE.array));
                        return result;
                    case MINUS:
                        result.add(gen(Token.TYPE.MINUS));
                        result.add(gen(NonTerminal.TYPE.factor));
                        return result;
                    default:
                        return null;
                }
            case array:
                if (select == Token.TYPE.LBRACKET) {
                    result.add(gen(Token.TYPE.LBRACKET));
                    result.add(gen(NonTerminal.TYPE.array_num));
                    result.add(gen(Token.TYPE.RBRACKET));
                    return result;
                } else {
                    Token.TYPE types[] = {Token.TYPE.ASSIGN,
                            Token.TYPE.MUL, Token.TYPE.DIV,
                            Token.TYPE.ADD, Token.TYPE.MINUS,
                            Token.TYPE.RPAREN, Token.TYPE.SEMI,
                            Token.TYPE.LT, Token.TYPE.GT,
                            Token.TYPE.EQ, Token.TYPE.NEQ};
                    for (Token.TYPE type : types) {
                        if (select == type) {
                            return result;
                        }
                    }
                }
                return null;
            case array_num:
                if (select == Token.TYPE.NUM_INT ||
                        select == Token.TYPE.ID) {
                    result.add(gen(select));
                    return result;
                }
                return null;
            default:
                return null;
        }
    }

    private static NonTerminal gen(NonTerminal.TYPE type) {
        return new NonTerminal(type);
    }

    private static Token gen(Token.TYPE type) {
        return new Token(type);
    }
}
