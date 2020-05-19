package test;

import aoshiScript.entity.WuNa;
import commons.entity.Constant;
import commons.util.MySpringUtil;
import destinychild.DaillyMission;
import destinychild.Raid;
import destinychild.entity.DcTask;
import destinychild.entity.TaskInfo;
import fgoScript.exception.AppNeedNextException;
import fgoScript.exception.AppNeedRestartException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

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
//        DaillyMission ds = (DaillyMission) applicationContext.getBean("daillyMission");
//        DcTask dcTask = ds.getDcTask(true, 2);
//        List<TaskInfo> taskInfoList = dcTask.getTasklist();
//        try {
//            ds.startOneMission(2,2,false);
//        } catch (AppNeedRestartException e) {
//            e.printStackTrace();
//            e.printStackTrace();
//        } catch (AppNeedNextException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void raidBattleStopTest() {
        try {
            new WuNa("").alwaysClickForStrategy("filterClick", null, false, true, Constant.DC + "/");
        } catch (AppNeedRestartException e) {
            e.printStackTrace();
        }
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