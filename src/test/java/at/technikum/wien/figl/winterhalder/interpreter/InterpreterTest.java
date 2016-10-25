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
    Integer result;

    @Before
    public void begin() {
        interpreter = new Interpreter();
    }

    @Test
    public void ParseMathOperators() {
        result = interpreter.parseString("print 17%3;");
        Assert.assertEquals((long)2,(long)result);
        result = interpreter.parseString("print 5%2;");
        Assert.assertEquals((long)1,(long)result);
    }

    @Test
    public void AssignVariableAndPrintValue() {
        result = interpreter.parseString("var x = 5; print x+3;");
        Assert.assertEquals((long)8,(long)result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void UsingUndeclaredVariable() {
        result = interpreter.parseString("x = 5;");
    }

    @Test
    public void DeclareVariableButUnassigned() {
        result = interpreter.parseString("var x; print x;");
        Assert.assertEquals((long)0,(long)result);
    }

    @Test
    public void ParseComments() {
        result = interpreter.parseString("//print 18;");
        Assert.assertNull(result);
        result = interpreter.parseString("print 3;//print 17;");
        Assert.assertEquals((long)3,(long)result);
        result = interpreter.parseString("print 3;var x;// KOMMENTAR \nx=3;//KOMMENTAR2\nprint x+1;");
        Assert.assertEquals((long)4,(long)result);
    }

    @Test
    public void ParserShouldPass() {
        try {
            result = interpreter.parseFile("testfiles\\input_should_pass.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)2,(long)result);
    }

    @Test
    public void ParserShouldFail1() {
        result = interpreter.parseString("var x+5 = 23;");
        Assert.assertNull(result);
    }

    @Test
    public void ParserShouldFail2() {
        result = interpreter.parseString("var x = ((4);");
        Assert.assertNull(result);
    }

    @Test
    public void ParserShouldFail3() {
        result = interpreter.parseString("5 = 234;");
        Assert.assertNull(result);

    }

    @Test
    public void ParserCompare_ShouldPass() {
        result = interpreter.parseString("print 5 > 234;");
        Assert.assertEquals((long)0,(long)result);
        result = interpreter.parseString("print 5 < 234;");
        Assert.assertEquals((long)1,(long)result);
        result = interpreter.parseString("print 5 == 234;");
        Assert.assertEquals((long)0,(long)result);
        result = interpreter.parseString("print 234 == 234;");
        Assert.assertEquals((long)1,(long)result);
        result = interpreter.parseString("print 234 >= 234;");
        Assert.assertEquals((long)1,(long)result);
        result = interpreter.parseString("print 134 >= 234;");
        Assert.assertEquals((long)0,(long)result);
        result = interpreter.parseString("print 234 >< 234;");
        Assert.assertEquals((long)0,(long)result);
        result = interpreter.parseString("print 235 >< 234;");
        Assert.assertEquals((long)1,(long)result);
    }

    @Test
    public void ParserWhile() {
        try {
            result = interpreter.parseFile("testfiles\\input_while.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)4,(long)result);
    }

    @Test
    public void ParserIf() {
        try {
            result = interpreter.parseFile("testfiles\\input_ifelse.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)10,(long)result);
    }

    @Test
    public void ParseExprInStmt() {
        result = interpreter.parseString("3+5;");
        Assert.assertEquals((long)8,(long)result);

    }

    @Test
    public void ParseInlineStatement() {
        try {
            result = interpreter.parseFile("testfiles\\input_InlineStatement.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertEquals((long)10,(long)result);
    }

    @Test
    public void ParseFunc() {
        try {
            result = interpreter.parseFile("testfiles\\input_funcdef.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)7,(long)result);
    }

    @Test
    public void ParseFunc2() {
        try {
            result = interpreter.parseFile("testfiles\\input_funcdef2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)1,(long)result);
    }

    @Test
    public void ParseIfNotNull() {
        try {
            result = interpreter.parseFile("testfiles\\input_ifNotNull.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)10,(long)result);
    }

    @Test
    public void ParseWhileIf() {
        try {
            result = interpreter.parseFile("testfiles\\input_whileif.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals((long)1,(long)result);
    }
}