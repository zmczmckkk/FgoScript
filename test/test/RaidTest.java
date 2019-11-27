package test;

import aoshiScript.entity.WuNa;
import commons.util.MySpringUtil;
import destinychild.Raid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        new WuNa("").alwaysClickForStrategy("filterClick", null, false, true);
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