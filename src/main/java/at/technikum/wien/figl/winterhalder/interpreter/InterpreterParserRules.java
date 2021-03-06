package at.technikum.wien.figl.winterhalder.interpreter;

import at.technikum.wien.figl.winterhalder.interpreter.gen.GrammarParserRulesLexer;
import at.technikum.wien.figl.winterhalder.interpreter.gen.GrammarParserRulesParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

/**
 * Created by richie on 14/10/16.
 */
public class InterpreterParserRules {
    public static void main(String[] args) throws IOException {


//        for(int i = 0; i < tokens.getNumberOfOnChannelTokens(); i++){
//            System.out.println(tokens.get(i));
//        }

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
        GrammarParserRulesLexer lexer = new GrammarParserRulesLexer(input);
        // Create a stream of tokens fed by the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Create a parser that feeds off the token stream
        GrammarParserRulesParser parser = new GrammarParserRulesParser(tokens);
        // Begin parsing at start rule
        GrammarParserRulesParser.StatementsContext sc = parser.statements();

        if(parser.getNumberOfSyntaxErrors() == 0){
            return false;
        }else {
            return true;
        }
    }




}
