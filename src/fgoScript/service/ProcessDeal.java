package fgoScript.service;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;

public class ProcessDeal {
	private static final Logger LOGGER = LogManager.getLogger(ProcessDeal.class);

	/**
	 * @desc 启动进程
	 * @author zp
	 * @date 2018-3-29
	 */
	public static void startProc(String processName) {
		LOGGER.info("启动应用程序：" + processName);
		if (StringUtils.isNotBlank(processName)) {
			try {
				Desktop.getDesktop().open(new File(processName));
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("应用程序：" + processName + "不存在！");
			}
		}
	}
	/**
	 * @desc 杀死进程
	 * @author zp
	 * @throws IOException
	 * @date 2018-3-29
	 */
	public static void killProc(String processName) throws IOException {
		LOGGER.info("关闭应用程序：" + processName);
		if (StringUtils.isNotBlank(processName)) {
			executeCmd("taskkill /F /IM " + processName,true);
		}
	}

	/**
	 * @desc 执行cmd命令
	 * @author zp
	 * @date 2018-3-29
	 */
	public static void executeCmd(String command, boolean waitFlag) throws IOException {
		LOGGER.info("Execute command : " + command);
		Runtime runtime = Runtime.getRuntime();
		Process p = runtime.exec("cmd /c " + command);
		//等待执行完成
		if (waitFlag) {
			try {
				long before = System.currentTimeMillis();
				p.waitFor();
				long now = System.currentTimeMillis();
				LOGGER.info("命令执行了：" + (now - before) + " ms");
			} catch (InterruptedException e) {
				LOGGER.error(GameUtil.getStackMsg(e));
			}
		}
	}

	/**
	 * @desc 判断进程是否开启
	 * @author zp
	 * @date 2018-3-29
	 */
	public static boolean findProcess(String processName) {
		BufferedReader bufferedReader = null;
		try {
			Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName + '"');
			bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains(processName)) {
					return true;
				}
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ignored) {
				}
			}
		}
	}

	public static void startTianTian(int account) {

		String url = GameUtil.getValueFromConfig("EXE_PATH");
		String line = url + "/TianTian.exe  -n " + account + " -t 天天二次元" + account + " -i " + url
				+ "/apps/com.aniplex.fategrandorder.apk";
		killAllTianTian();
		String procName = "TianTian.exe";
		boolean exist = findProcess(procName);
		try {
			if (exist) {
				// 存在，那么就先杀死该进程
				killProc(procName);
				// 在启动
				executeCmd(line,false);
			} else {
				executeCmd(line,false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("重启/杀死提取程序失败。。。");
		}
	}
	public static void closeComputer() {
		String line= "C:\\Windows\\System32\\shutdown.exe -s -t 120";
		try {
			executeCmd(line,false);
		} catch (IOException e) {
			LOGGER.error("关机失败");
		}
	}
	public static void abordCloseComputer() {
		String line= "C:\\Windows\\System32\\shutdown.exe -a";
		try {
			executeCmd(line,false);
		} catch (IOException e) {
			LOGGER.error("关机失败");
		}
	}

	public static void killAllTianTian() {
		String procName = "TianTian.exe";
		boolean exist = findProcess(procName);
		try {
			if (exist) {
				// 存在，那么就先杀死该进程
				killProc(procName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("重启/杀死提取程序失败。。。");
		}
		killAllWerFault();
	}

	public static void killAllWerFault() {
		String procName = "WerFault.exe";
		boolean exist = findProcess(procName);
		try {
			if (exist) {
				// 存在，那么就先杀死该进程
				killProc(procName);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("重启/杀死提取程序失败。。。");
		}
	}

	public static void main(String[] args) {
		abordCloseComputer();
	}
}
