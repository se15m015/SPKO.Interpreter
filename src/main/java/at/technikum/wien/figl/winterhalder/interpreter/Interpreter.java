package at.technikum.wien.figl.winterhalder.interpreter;
import at.technikum.wien.figl.winterhalder.interpreter.gen.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

/**
 * Created by richie on 14/10/16.
 */
public class Interpreter {
    public static void main(String[] args) throws IOException {

// Create an input character stream from standard in
        //ANTLRFileStream input = new ANTLRFileStream("input"); // give path to the file input
        CharStream input = new ANTLRInputStream("x = --2;");
// Create an ExprLexer that feeds from that stream
        GrammarLexer lexer = new GrammarLexer(input);
// Create a stream of tokens fed by the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
// Create a parser that feeds off the token stream
        GrammarParser parser = new GrammarParser(tokens);
// Begin parsing at start rule
        //parser.prog();
        GrammarParser.StatementsContext sc = parser.statements();

    }

}
