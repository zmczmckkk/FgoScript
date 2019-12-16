package fgoScript;

import commons.util.MySpringUtil;
import fgoScript.entity.panel.FgoFrame;
import fgoScript.service.TimerManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 程序执行入口
 * @author: RENZHEHAO
 * @create: 2019-05-22 04:13
 **/
public class Entrance {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = MySpringUtil.getApplicationContext();
        FgoFrame f = (FgoFrame) applicationContext.getBean("fgoFrame");
        f.init();
        // 启动定时任务
        new TimerManager().startTasks();
    }
}
