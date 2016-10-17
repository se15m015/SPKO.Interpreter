package at.technikum.wien.figl.winterhalder.interpreter;

import at.technikum.wien.figl.winterhalder.interpreter.gen.GrammarSimpleLexer;
import at.technikum.wien.figl.winterhalder.interpreter.gen.GrammarSimpleParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richie on 14/10/16.
 */
public class SimpleInterpreter {
    public static void main(String[] args) throws IOException {

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
        // Create an ExprLexer that feeds from that stream
        GrammarSimpleLexer lexer = new GrammarSimpleLexer(input);
        // Create a stream of tokens fed by the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Create a parser that feeds off the token stream
        GrammarSimpleParser parser = new GrammarSimpleParser(tokens);
        // Begin parsing at start rule
        //GrammarSimpleParser.StatementsContext sc = parser.statements();

        System.out.println(parser.statements());

        if(parser.getNumberOfSyntaxErrors() == 0){
            return false;
        }else {
            return true;
        }
    }




}
