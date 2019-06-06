package test;

import commons.util.MySpringUtil;
import destinyChild.Raid;
import fgoScript.entity.panel.FgoFrame;
import fgoScript.service.TimerManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class RaidTest extends Raid {
    private ClassPathXmlApplicationContext applicationContext;

    public void setApplicationContext(ClassPathXmlApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    public void getMenuTest() {
    }

    @Test
    public void raidBattleStartTest() {
        Raid r = (Raid) applicationContext.getBean("raid");
        r.raidBattleStart();
    }

    @Test
    public void raidBattleStopTest() {
    }

    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext applicationContext = MySpringUtil.getApplicationContext();
        setApplicationContext(applicationContext);
    }

    @After
    public void tearDown() throws Exception {
    }
}