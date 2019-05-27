package fgoScript.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @description: 开启一个线程来执行这个监控方法。
 * before 和 add 方法 可以执行开关方法
 * @author: RENZHEHAO
 * @create: 2019-05-22 03:45
 **/
public abstract class AbstractWaitForOutTime implements Runnable{
	private static final Logger LOGGER = LogManager.getLogger(AbstractWaitForOutTime.class);
	public abstract void beforeDo();
	public abstract void afterDo();
	@Override
	public void run() {
		beforeDo();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		afterDo();
		LOGGER.info("监控超时线程正常结束~！");
	}
	
}
