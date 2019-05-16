package test;

import destinyChild.Entrance;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntranceTest {

    @Test
    public void getAllGuestSaves() {
        Entrance et = new Entrance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                et.setFlag(false);
            }
        }).start();
        et.getAllGuestSaves();
    }
}