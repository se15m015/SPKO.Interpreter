package at.technikum.wien.figl.winterhalder.interpreter;

import at.technikum.wien.figl.winterhalder.interpreter.gen.*;
import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by domin_000 on 21.10.2016.
 */
public class Interpreter {
    final GrammarVisitor<Integer> visitor = new GrammarBaseVisitor<Integer>() {
        final Map<String, Integer> symTable = new HashMap<String, Integer>();

        public Integer visitStatements(GrammarParser.StatementsContext ctx) {
            for (GrammarParser.StatementContext c : ctx.statement()) {
                visit(c);
            }

            return null;
        }

        public Integer visitPrintStmt(GrammarParser.PrintStmtContext ctx) {
            Integer value = visit(ctx.expr());
            System.out.println("Output: " + value);
            return value;
        }

        public Integer visitDefineStmt(GrammarParser.DefineStmtContext ctx) {
            String id = ctx.ID().getText();
            Integer value = null;
            if(ctx.e != null) value = visit(ctx.e);

            if (value == null) {
                symTable.put(id, 0);
            } else {
                symTable.put(id, value);
            }

            return value;
        }

        public Integer visitAssignStmt(GrammarParser.AssignStmtContext ctx) {
            String id = ctx.ID().getText();
            Integer value = visit(ctx.e);

            if (value == null) {
                System.err.println("expr was null!");
                throw new IllegalArgumentException("Expr was null");
            } else if (!symTable.containsKey(id)) {
                System.err.println("variable is undefined!");
                throw new IllegalArgumentException("variable is undefined");
            } else {
                symTable.put(id, value);
            }

            return value;
        }

        public Integer visitWhileStmt(GrammarParser.WhileStmtContext ctx) {
            while (true) {
                Integer b = visit(ctx.c);

                if (b == null) {
                    System.err.println("condition was null");
                    return null;
                }

                if (b == 0) return null;

                visit(ctx.s);
            }
        }

        public Integer visitIfStmt(GrammarParser.IfStmtContext ctx) {
            Integer c = visit(ctx.c);

            if (c == null) {
                System.err.println("condition was null");
                return null;
            }

            if (c == 1) {
                visit(ctx.sIf);
            }

            if (ctx.sElse != null) {
                if (c == 0) {
                    visit(ctx.sElse);
                }
            }

            return null;
        }

        public Integer visitBlockStmt(GrammarParser.BlockStmtContext ctx) {
            return visit(ctx.s);
        }


        public Integer visitNegStmt(GrammarParser.NegStmtContext ctx) {
            Integer arg = visit(ctx.e);
            if (arg == null) {
                System.err.println("expr was null");
                return null;
            }
            return -arg;
        }

        public Integer visitOpExpr(GrammarParser.OpExprContext ctx) {
            Integer left = visit(ctx.left);
            Integer right = visit(ctx.right);

            if (left == null || right == null) {
                System.err.println("expr was null");
                return null;
            }

            switch (ctx.op.getText()) {
                case "+":
                    return left + right;
                case "-":
                    return left - right;
                case "*":
                    return left * right;
                case "/":
                    return left / right;
                case "%":
                    return left % right;
                case "<":
                    return boolToInt(left < right);
                case "=<":
                    return boolToInt(left <= right);
                case "==":
                    return boolToInt(left == right);
                case "><":
                    return boolToInt(left != right);
                case ">=":
                    return boolToInt(left >= right);
                case ">":
                    return boolToInt(left > right);
                default:
                    assert false;
            }

            return null;
        }

        public Integer boolToInt(boolean b) {
            return (b) ? 1 : 0;
        }

        public Integer visitWrapperExpr(GrammarParser.WrapperExprContext ctx) {
            return visit(ctx.expr());
        }

        public Integer visitNumExpr(GrammarParser.NumExprContext ctx) {
            return Integer.parseInt(ctx.INT().getText());
        }

        public Integer visitIdExpr(GrammarParser.IdExprContext ctx) {
            String id = ctx.ID().getText();

            if (symTable.containsKey(id)) return symTable.get(id);

            System.err.println("unknown symbol");
            return null;
        }
    };

    public boolean interpretProgram(String source) {
        TokenStream toks = new CommonTokenStream(new GrammarLexer(new ANTLRInputStream(source)));

        GrammarParser parser = new GrammarParser(toks);

        this.visitor.visit(parser.statements());

        if(parser.getNumberOfSyntaxErrors() == 0){
            return false;
        }else {
            return true;
        }
    }

    public static void main(String... args) {

    }

    public boolean parseFile(String fileName) throws IOException {
        ANTLRFileStream input = new ANTLRFileStream(fileName);
        return parse(input);
    }

    public boolean parseString(String content){
        CharStream input = new ANTLRInputStream(content);
        return parse(input);
    }

    private boolean parse(CharStream input){

        try{
            interpretProgram(input.toString());
        }catch(Exception e){
            return  false;
        }
        return true;
    }
}
