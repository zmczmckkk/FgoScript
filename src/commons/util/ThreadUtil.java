package commons.util;

import destinychild.Raid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName ThreadUtil.java
 * @Description 线程工具类
 * @createTime 2019年11月06日 17:11:00
 */
public class ThreadUtil {
    private static final Logger LOGGER = LogManager.getLogger(ThreadUtil.class);
    /**
     * @Description: 当线程池无活动时，执行下一步
     * @return: void
     * @throw:
     * @Author: RENZHEHAO
     * @Date: 2019/6/7
     */
    public static void waitUntilNoneThread(ThreadPoolTaskExecutor threadPoolTaskExecutor){
        do{
            GameUtil.delay(3000);
            LOGGER.info("线程个数: " + threadPoolTaskExecutor.getActiveCount());
        } while (threadPoolTaskExecutor.getActiveCount() != 0);
    }
}
