package fgoScript.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.entity.Gudazi;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;

public class TimerManager {
	private static final Logger LOGGER = LogManager.getLogger(TimerManager.class);
	// 时间间隔
	private static final long PERIOD_TIME = 12 * 60 * 60 * 1000;

	public TimerManager() {
		String path = GameUtil.getValueFromConfig("EXE_PATH");
		path = path.replace("^", "");
		if (!new File(path).exists()) {
			LOGGER.info("游戏程序不存在,路径错误！,不执行定时任务");
		}else {
			Calendar calendar = Calendar.getInstance();
			int HOUR_OF_DAY = Integer.valueOf(GameUtil.getValueFromConfig("HOUR_OF_DAY"));
			int MINUTE = Integer.valueOf(GameUtil.getValueFromConfig("MINUTE"));
			int SECOND = Integer.valueOf(GameUtil.getValueFromConfig("SECOND"));
			
			/*** 定制每日3:10执行方法 ***/
			calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
			calendar.set(Calendar.MINUTE, MINUTE);
			calendar.set(Calendar.SECOND, SECOND);
			
			Date date = calendar.getTime(); // 第一次执行定时任务的时间
			
			// 如果第一次执行定时任务的时间 小于 当前的时间
			// 此时要在 第一次执行定时任务的时间 加周期 直到大于为止。
			Date nowDate = new Date();
			date = modifyTime(date, nowDate, PERIOD_TIME);
			//删除过期图片
			deletePics();
			Timer timer = new Timer();
			Gudazi task = new Gudazi();
			// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
			timer.schedule(task, date, PERIOD_TIME);
			String time = HOUR_OF_DAY + " : " + MINUTE + " : " + SECOND;
			LOGGER.info("定时任务已启动！启动时间：" + time);
		}
	}

	// 增加或减少天数
	public Date modifyTime(Date date, Date nowDate, long period) {
		if (date.before(nowDate)) {
			date = new Date(date.getTime()+period);
			if (date.before(nowDate)) {
				date = modifyTime(date, nowDate, period);
			}
		}
		return date;
	}
	public static void deleteDirs(File f){
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
			String ROOT_PATH = PropertiesUtil.getValueFromConfig("ROOT_PATH");
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
	public static void main(String[] args) {
		deletePics();
	}
}
