package at.technikum.wien.figl.winterhalder.interpreter;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by domin_000 on 17.10.2016.
 */
public class SimpleInterpreterTest {

    SimpleInterpreter interpreter;
    boolean errors;

    @Before
    public void setUp() throws Exception {
        interpreter = new SimpleInterpreter();
    }

    @Test
    public void AssigneVariableAndPrintValue(){
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
}