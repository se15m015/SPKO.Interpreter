package at.technikum.wien.figl.winterhalder.interpreter;

import at.technikum.wien.figl.winterhalder.interpreter.gen.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.util.*;

/**
 * Created by domin_000 on 21.10.2016.
 */
public class Interpreter {

    public class FuncHelp {
        GrammarParser.StatementContext ctx;
        List<String> params;
    }

    final GrammarVisitor<Integer> visitor = new GrammarBaseVisitor<Integer>() {
        final Map<String, Integer> symTable = new HashMap<String, Integer>();

        final Map<String, FuncHelp> funcDefinitions = new HashMap<String, FuncHelp>();
        final Stack<Map<String, Integer>> scopeStack = new Stack<Map<String, Integer>>();

        @Override
        public Integer visitStatements(GrammarParser.StatementsContext ctx) {
            for (GrammarParser.StatementContext c : ctx.statement()) {
                visit(c);
            }

            return null;
        }

        @Override
        public Integer visitPrintStmt(GrammarParser.PrintStmtContext ctx) {

            Integer value = visit(ctx.expr());
            System.out.println("Output: " + value);
            return value;
        }

        @Override
        public Integer visitFuncdefStmt(GrammarParser.FuncdefStmtContext ctx) {

            String id = ctx.ID().getText();
            GrammarParser.StatementContext _stc = ctx.statement();

            FuncHelp _fh = new FuncHelp();
            _fh.ctx = _stc;
            _fh.params = new ArrayList<>();

            for (TerminalNode t:ctx.idlist().ID())
            {
                _fh.params.add(t.getText());
            }

            funcDefinitions.put(id,_fh);

            return null;
        }

        @Override
        public Integer visitFunccallStmt(GrammarParser.FunccallStmtContext ctx) {
            String id = ctx.ID().getText();

            FuncHelp _fh = funcDefinitions.get(id);

            if(_fh == null) {
                System.err.println("Call to undefined function '" + id + "'");
                throw new IllegalArgumentException("Call to undefined function '" + id + "'");
            }

            Map<String, Integer> scopeForFunction = new HashMap<>();

            for (int i = 0; i < ctx.arglist().expr().size(); i++)
            {
                Integer _value = visit(ctx.arglist().expr().get(i));
                scopeForFunction.put(_fh.params.get(i),_value);
            }

            scopeStack.push(scopeForFunction);
            Integer result = visit(_fh.ctx);
            scopeStack.pop();
            return result;
        }

        @Override
        public Integer visitDefineStmt(GrammarParser.DefineStmtContext ctx) {
            Map<String, Integer> _scopeToUse = getScope();


            String id = ctx.ID().getText();
            Integer value = null;
            if(ctx.e != null) value = visit(ctx.e);

            if (value == null) {
                _scopeToUse.put(id, 0);
            } else {
                _scopeToUse.put(id, value);
            }

            return value;
        }

        @Override
        public Integer visitAssignStmt(GrammarParser.AssignStmtContext ctx) {
            Map<String, Integer> _scopeToUse = getScope();

            String id = ctx.ID().getText();
            Integer value = visit(ctx.e);

            if (value == null) {
                System.err.println("expr was null!");
                throw new IllegalArgumentException("Expr was null");
            } else if (!_scopeToUse.containsKey(id)) {
                System.err.println("variable is undefined!");
                throw new IllegalArgumentException("variable is undefined");
            } else {
                _scopeToUse.put(id, value);
            }

            return value;
        }

        private Map<String, Integer> getScope() {
            Map<String,Integer> _scopeToUse=null;

            if(!scopeStack.empty())
                _scopeToUse = scopeStack.peek();
            else
                _scopeToUse = symTable;
            return _scopeToUse;
        }

        @Override
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

        @Override
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

        @Override
        public Integer visitBlockStmt(GrammarParser.BlockStmtContext ctx) {
            return visit(ctx.s);
        }


        @Override
        public Integer visitNegStmt(GrammarParser.NegStmtContext ctx) {
            Integer arg = visit(ctx.e);
            if (arg == null) {
                System.err.println("expr was null");
                return null;
            }
            return -arg;
        }

        @Override
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

        @Override
        public Integer visitWrapperExpr(GrammarParser.WrapperExprContext ctx) {
            return visit(ctx.expr());
        }

        @Override
        public Integer visitNumExpr(GrammarParser.NumExprContext ctx) {
            return Integer.parseInt(ctx.INT().getText());
        }

        @Override
        public Integer visitIdExpr(GrammarParser.IdExprContext ctx) {
            Map<String, Integer> _scopeToUse = getScope();

            String id = ctx.ID().getText();

            if (_scopeToUse.containsKey(id)) return _scopeToUse.get(id);

            System.err.println(String.format("unknown symbol: %s",id));
            return null;
        }

        @Override
        public Integer visitInlineStmt(GrammarParser.InlineStmtContext ctx) {

            Map<String,Integer> localScope = new HashMap<>();
            scopeStack.push(localScope);

            Integer result = null;
            for (GrammarParser.StatementContext c : ctx.s.statement()) {
                result = visit(c);
            }

            scopeStack.pop();

            return result;

        }


        /*
        public Integer visitGlobalDefineStmt(GrammarParser.GlobalDefineStmtContext ctx)
        {
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
        public Integer visitGlobalAssignStmt(GrammarParser.GlobalAssignStmtContext ctx)
        {
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
        */
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
