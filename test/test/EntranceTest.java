package test;

import destinyChild.DestinyEntrance;
import org.junit.Test;

public class EntranceTest {

    @Test
    public void getAllGuestSaves() {
        DestinyEntrance et = new DestinyEntrance();
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