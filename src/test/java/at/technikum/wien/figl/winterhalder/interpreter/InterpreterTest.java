package at.technikum.wien.figl.winterhalder.interpreter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by richie on 15/10/16.
 */
public class InterpreterTest {
    Interpreter interpreter;
    boolean errors;

    @Before
    public void begin() {
        interpreter = new Interpreter();
    }

    @Test
    public void ParseMathOperators() {
        errors = interpreter.parseString("print 17%3;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 5%2;");
        Assert.assertTrue(errors);
    }

    @Test
    public void AssignVariableAndPrintValue() {
        errors = interpreter.parseString("var x = 5; print x+3;");
        Assert.assertTrue(errors);
    }

    @Test
    public void UsingUndeclaredVariable() {
        errors = interpreter.parseString("x = 5;");
        Assert.assertFalse(errors);

    }

    @Test
    public void DeclareVariableButUnassigned() {
        errors = interpreter.parseString("var x; print x;");
        Assert.assertTrue(errors);
    }

    @Test
    public void ParseComments() {
        errors = interpreter.parseString("//print 18;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 3;//print 17;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 3;var x;// KOMMENTAR \nx=3;//KOMMENTAR2\nprint x+1;");
        Assert.assertTrue(errors);
    }

    @Test
    public void ParserShouldPass() {
        try {
            errors = interpreter.parseFile("testfiles\\input_should_pass.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }

    @Test
    public void ParserShouldFail1() {
        errors = false;
        errors = interpreter.parseString("var x+5 = 23;");
        Assert.assertTrue(errors);
    }

    @Test
    public void ParserShouldFail2() {
        errors = false;
        errors = interpreter.parseString("var x = ((4);");
        Assert.assertTrue(errors);
    }

    @Test
    public void ParserShouldFail3() {
        errors = false;
        errors = interpreter.parseString("5 = 234;");
        Assert.assertTrue(errors);

    }

    @Test
    public void ParserCompare_ShouldPass() {
        errors = false;
        errors = interpreter.parseString("print 5 > 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 5 < 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 5 == 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 234 == 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 234 >= 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 134 >= 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 234 >< 234;");
        Assert.assertTrue(errors);
        errors = interpreter.parseString("print 235 >< 234;");
        Assert.assertTrue(errors);
    }

    @Test
    public void ParserWhile() {
        try {
            errors = interpreter.parseFile("testfiles\\input_while.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }

    @Test
    public void ParserIf() {
        try {
            errors = interpreter.parseFile("testfiles\\input_ifelse.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }

    @Test
    public void ParseExprInStmt() {
        errors = interpreter.parseString("3+5;");
        Assert.assertTrue(errors);
    }

    @Test
    public void ParseInlineStatement() {
        try {
            errors = interpreter.parseFile("testfiles\\input_InlineStatement.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }

    @Test
    public void ParseFunc() {
        try {
            errors = interpreter.parseFile("testfiles\\input_funcdef.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }

    @Test
    public void ParseFunc2() {
        try {
            errors = interpreter.parseFile("testfiles\\input_funcdef2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }

    @Test
    public void ParseIfNotNull() {
        try {
            errors = interpreter.parseFile("testfiles\\input_ifNotNull.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(errors);
    }
}