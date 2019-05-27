package test;

import destinyChild.entity.SingleObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingleObjectTest {
    @Test
    public void myTest() {
        SingleObject.getInstance();
    }
}