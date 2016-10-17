package at.technikum.wien.figl.winterhalder.interpreter;

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
    public void ParserShouldPass2(){
        errors = interpreter.parseString("var x = 5; print x+3;");
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
    public void ParserShouldPassAll(){
        errors = true;
        errors = interpreter.parseString("x = --2;");
        Assert.assertFalse(errors);

        errors = true;
        errors = interpreter.parseString("var x = 3+5*4;");
        Assert.assertFalse(errors);

        errors = true;
        errors = interpreter.parseString("print x;");
        Assert.assertFalse(errors);
    }

    @Test
    public void ParserShouldFail(){

        errors = false;
        errors = interpreter.parseString("var x+5 = 23;");
        Assert.assertTrue(errors);

        errors = false;
        errors = interpreter.parseString("x = ((4);");
        Assert.assertTrue(errors);

        errors = false;
        errors = interpreter.parseString("5 = 234;");
        Assert.assertTrue(errors);

    }
}