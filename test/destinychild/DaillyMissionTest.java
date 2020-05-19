package destinychild;

import commons.util.MySpringUtil;
import destinychild.entity.DcTask;
import destinychild.entity.TaskInfo;
import fgoScript.exception.AppNeedNextException;
import fgoScript.exception.AppNeedRestartException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

public class DaillyMissionTest extends DaillyMission {
    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext applicationContext = MySpringUtil.getApplicationContext();
        setApplicationContext(applicationContext);
    }

    @After
    public void tearDown() throws Exception {
    }
    private ClassPathXmlApplicationContext applicationContext;

    public void setApplicationContext(ClassPathXmlApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Test
    public void daillyMissionStart() {
        DaillyMission ds = (DaillyMission) applicationContext.getBean("daillyMission");
        DcTask dcTask = ds.getDcTask(true, 2, false);
        List<TaskInfo> taskInfoList = dcTask.getTasklist();
        try {
            ds.startOneMission(2,2, 0, false);
        } catch (AppNeedRestartException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (AppNeedNextException e) {
            e.printStackTrace();
        }
    }
}