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
    public void ParserShouldPass(){
        errors = interpreter.parseString("var x = 5; print x+3;");
        Assert.assertFalse(errors);
    }
}