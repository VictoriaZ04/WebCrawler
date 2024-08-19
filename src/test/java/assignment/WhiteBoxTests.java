package assignment;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class WhiteBoxTests {
    WebQueryEngine queryEngine = WebQueryEngine.fromIndex(
                (WebIndex) Index.load("index.db"));

    public WhiteBoxTests() throws IOException, ClassNotFoundException {
    }


    @Test
    public void simple(){
        assertEquals(2, queryEngine.query("word").size());
        assertEquals(2, queryEngine.query("words").size());
        assertEquals(1, queryEngine.query("potato").size());
        assertEquals(0, queryEngine.query("nonsense").size());

    }

    @Test
    public void and(){
        assertEquals(1, queryEngine.query("word&words").size());
        assertEquals(0, queryEngine.query("much &potato").size());
        assertEquals(1, queryEngine.query("(many & potato)").size());
    }

    @Test
    public void or(){
        assertEquals(3, queryEngine.query("word|words").size());
        assertEquals(3, queryEngine.query("much | loop").size());
        assertEquals(2, queryEngine.query("(much |text)").size());
    }

    @Test
    public void not(){
        assertEquals(2, queryEngine.query("!word").size());
        assertEquals(2, queryEngine.query("!words").size());
        assertEquals(3, queryEngine.query("!potato").size());
        assertEquals(4, queryEngine.query("!nonsense").size());
    }

    @Test
    public void phrase(){
        assertEquals(1, queryEngine.query("some text").size());
        assertEquals(1, queryEngine.query("loop word").size());

    }


}
