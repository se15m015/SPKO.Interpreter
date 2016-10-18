package at.technikum.wien.figl.winterhalder.interpreter;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by richie on 15/10/16.
 */
public class InterpreterTest {
    Interpreter interpreter;
    boolean errors;

    @Before
    public void begin(){
        interpreter = new Interpreter();
    }

    @Test
    public void AssignVariableAndPrintValue(){
        errors = interpreter.parseString("var x = 5; print x+3;");
        Assert.assertFalse(errors);
    }

    @Test(expected=IllegalArgumentException.class)
    public void UsingUndeclaredVariable(){
        errors = interpreter.parseString("x = 5;");
    }
    @Test
    public void DeclareVariableButUnassigned(){
        errors = interpreter.parseString("var x; print x;");
        Assert.assertFalse(errors);
    }

    @Test
    public void ParseComments(){
        errors = interpreter.parseString("//print 18;");
        Assert.assertFalse(errors);
        errors = interpreter.parseString("print 3;//print 17;");
        Assert.assertFalse(errors);
        errors = interpreter.parseString("print 3;var x;// KOMMENTAR \nx=3;//KOMMENTAR2\nprint x+1;");
        Assert.assertFalse(errors);
    }

    @Test
    public void ParserShouldPass(){
        try {
            errors = interpreter.parseFile("input_should_pass.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(errors);
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
    public void ParserShouldFail3(){
        errors = false;
        errors = interpreter.parseString("5 = 234;");
        Assert.assertTrue(errors);

    }

    @Test
    public void ParserCompare_ShouldPass(){
        errors = false;
        errors = interpreter.parseString("print 5 > 234;");
        errors = interpreter.parseString("print 5 < 234;");
        errors = interpreter.parseString("print 5 == 234;");
        errors = interpreter.parseString("print 234 == 234;");
        errors = interpreter.parseString("print 234 >= 234;");
        errors = interpreter.parseString("print 134 >= 234;");
        errors = interpreter.parseString("print 234 >< 234;");
        errors = interpreter.parseString("print 235 >< 234;");
        Assert.assertFalse(errors);
    }

    @Test
    public void ParserWhile(){
        try {
            errors = interpreter.parseFile("input_while.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(errors);
    }

}