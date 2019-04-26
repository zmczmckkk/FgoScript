package test;

import fgoScript.entity.guda.ExpApGudaziForMainSpecial;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpApGudaziForMainSpecialTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insertIntoExpRoom() {
        try {
            new ExpApGudaziForMainSpecial().insertIntoExpRoom(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}