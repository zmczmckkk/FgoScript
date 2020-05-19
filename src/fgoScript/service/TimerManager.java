package fgoScript.service;

import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.MySpringUtil;
import commons.util.PropertiesUtil;
import destinychild.DaillyMission;
import fgoScript.constant.PointInfo;
import fgoScript.entity.panel.FgoFrame;
import fgoScript.entity.Gudazi;
import commons.util.GameUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerManager {
	private static final Logger LOGGER = LogManager.getLogger(TimerManager.class);
	// 定时任务执行间隔 时，分，秒，毫秒
	private static final long PERIOD_TIME = 24 * 60 * 60 * 1000;
	//使用ScheduledThreadPoolExecutor线程池执行类调度任务
	private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2,
			new BasicThreadFactory.Builder().namingPattern("CommonExcute-pool-%d").daemon(false).build());
	private static Map<String, ScheduledFuture<?>> maps = new HashMap<String, ScheduledFuture<?>>();
	public TimerManager() {
	}
	/**
	 * @Description: 启动定时任务
	 * @return: void
	 * @throw: 
	 * @Author: RENZHEHAO
	 * @Date: 2019/5/22
	 */
	public void startTasks(){
		String time = "";
		Calendar calendar = Calendar.getInstance();
		int HOUR_OF_DAY = Integer.valueOf(GameUtil.getValueFromConfig("HOUR_OF_DAY"));
		int MINUTE = Integer.valueOf(GameUtil.getValueFromConfig("MINUTE"));
		int SECOND = Integer.valueOf(GameUtil.getValueFromConfig("SECOND"));
		boolean ifMinMissionStart = Boolean.parseBoolean(PropertiesUtil.getValueFromFileNameAndKey("IF_MIN_MISSION", "DCinit", Constant.DC + "/"));
		time = HOUR_OF_DAY + " : " + MINUTE + " : " + SECOND;
		/*** 定制每日3:10执行方法 ***/
		calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
		calendar.set(Calendar.MINUTE, MINUTE);
		calendar.set(Calendar.SECOND, SECOND);
		Date date = calendar.getTime(); // 第一次执行定时任务的时间
		// 获取第一次延迟执行时间
		date = modifyTime(date,new Date(),PERIOD_TIME);
		long initDelay  = date.getTime() - System.currentTimeMillis();
		initDelay = initDelay > 0 ? initDelay : PERIOD_TIME + initDelay;

		//使用ScheduledThreadPoolExecutor线程池执行类调度任务
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2,
				new BasicThreadFactory.Builder().namingPattern("FgoExcute-pool-%d").daemon(false).build());
		if (!"RZH-SERVER".equals(NativeCp.getUserName())) {
			LOGGER.info("用户名： " + NativeCp.getUserName() + "不启动fgo任务");
		}else {
			// 删除过期图片
			deletePics();
			//执行挂机任务
			executorService.scheduleAtFixedRate(new Gudazi(), initDelay, PERIOD_TIME, TimeUnit.MILLISECONDS);
			//执行精简日常任务
			if (ifMinMissionStart) {
				//执行挂机任务
				executorService.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						DaillyMission dm = (DaillyMission) MySpringUtil.getApplicationContext().getBean("daillyMission");
						dm.toggle(true);
					}
				}, 5000, PERIOD_TIME, TimeUnit.MILLISECONDS);
			}
			LOGGER.info("定时s任务已启动！启动时间：" + time);

		}
		//执行切换壁纸任务
		executorService.schedule(new Runnable() {
			@Override
			public void run() {
				do {
					int interval = Integer.parseInt(GameUtil.getValueFromConfig("PicChangeInterval"));
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						LOGGER.error(GameUtil.getStackMsg(e));
					}
					FgoFrame.instance().changeBackGround();
				} while (true);
			}
		}, 3000, TimeUnit.MILLISECONDS);
		LOGGER.info("壁纸切换已启动！" + time);
	}
	// 增加或减少天数
	public static Date modifyTime(Date date, Date nowDate, long period) {
		if (date.before(nowDate)) {
			date = new Date(date.getTime()+period);
			if (date.before(nowDate)) {
				date = modifyTime(date, nowDate, period);
			}
		}
		return date;
	}
	private static void deleteDirs(File f){
		if (f.isDirectory()) {
			File [] b = f.listFiles();//获取包含file对象对应的子目录或者文件
			File temp;
			for(int i = 0; i< (b != null ? b.length : 0); i++){
				temp = b[i];
				if(temp.isFile()){//判断是否为文件
					temp.delete();//如果是就删除
					LOGGER.info("删除文件:" + temp.getName());
				}else{
					deleteDirs(temp);//否则重新递归到方法中
				}
			}
			f.delete();//最后删除该目录中所有文件后就删除该目录
			LOGGER.info("删除文件夹:" + f.getName());
		}
    } 
	private static void deletePics() {
		try {
			String USERPROFILE = System.getenv("USERPROFILE");
			String ROOT_PATH = GameUtil.getValueFromConfig("ROOT_PATH");
			String path = USERPROFILE + ROOT_PATH;
			File file = new File(path);
			File[] files;
			boolean isDir = file.isDirectory();
			long nowTime = System.currentTimeMillis();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String nowString = sf.format(new Date(nowTime));
			if (isDir) {
				files  = file.listFiles();
				File temp;
				File inneFile;
				String fileString;
				String name;
				int loc;
				int tempFilesSize;
				File[] tempFiles;
				int rewardCounts;
				int filesLength = files != null ? files.length : 0;
				for (int i = 0; i < filesLength; i++) {
					temp = files[i];
					if (temp.isDirectory()) {
						name = temp.getName();
						tempFiles = temp.listFiles();
						tempFilesSize = tempFiles != null ? tempFiles.length : 0;
						loc = name.indexOf("_");
						if (loc != -1) {
							fileString= name.substring(0, name.indexOf("_"));
						}else {
							fileString = name;
						}
						if (nowString.equals(fileString)) {
							rewardCounts = 0;
							for (int j = 0; j < tempFilesSize; j++) {
								inneFile = tempFiles[j];
								if (inneFile.getName().contains("奖励")) {
									rewardCounts++;
								}
							}
							if (rewardCounts==0) {
								deleteDirs(temp);
							}
						}else {
							deleteDirs(temp);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(GameUtil.getStackMsg(e));
		}
	}
	public static void startTaskByTime(String time, Runnable runnable, String taskName){
		Calendar calendar = Calendar.getInstance();
		String[] times = time.split(":");
		int HOUR_OF_DAY = Integer.valueOf(times[0]);
		int MINUTE = Integer.valueOf(times[1]);
		time = HOUR_OF_DAY + " : " + MINUTE ;

		calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
		calendar.set(Calendar.MINUTE, MINUTE);
		Date date = calendar.getTime(); // 第一次执行定时任务的时间
		// 获取第一次延迟执行时间
		date = modifyTime(date,new Date(),PERIOD_TIME);
		long initDelay  = date.getTime() - System.currentTimeMillis();
		initDelay = initDelay > 0 ? initDelay : PERIOD_TIME + initDelay;

		LOGGER.info("用户名: "+NativeCp.getUserName()+" 启动任务");
		ScheduledFuture<?> future = executorService.schedule(runnable, initDelay, TimeUnit.MILLISECONDS);
		maps.put(taskName,future);
	}
	public static void stopTask(String taskName){
		LOGGER.info("用户名: "+NativeCp.getUserName()+" 关闭任务");
		ScheduledFuture<?> future = maps.get(taskName);
		future.cancel(false);
	}
}
