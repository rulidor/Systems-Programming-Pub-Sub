package bgu.spl;

import bgu.spl.mics.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    Future<String> futureResolved;
    Future<String> futureNotResolved;

    @BeforeEach
    public void setUp(){
        futureResolved=new Future<>();
        futureNotResolved=new Future<>();

    }

    @Test
    public void testGetNotBlocked()
    {
        futureResolved.resolve("result");

        assertEquals("result",futureResolved.get(100, TimeUnit.MILLISECONDS),"should return result");
        assertEquals(null,futureNotResolved.get(100,TimeUnit.MILLISECONDS),"should return null");
    }

    @Test
    public void testGet()
    {
        futureResolved.resolve("result");

        assertEquals("result",futureResolved.get(),"should return result");
        assertEquals(null,futureNotResolved.get(),"should return null");
    }

    @Test
    public void testIsDone()
    {
        assertTrue(futureResolved.isDone(),"should return true");
        assertFalse(futureNotResolved.isDone(),"should return false");
    }

    @Test
    public void testResolve()
    {
        assertFalse(futureNotResolved.isDone(),"should return false");

        futureNotResolved.resolve("result");
        assertTrue(futureNotResolved.isDone(),"should return true");
    }
    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
