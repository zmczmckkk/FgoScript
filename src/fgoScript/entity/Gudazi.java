package fgoScript.entity;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.FgoPanel;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.guda.EventGudazi;
import fgoScript.entity.guda.ExpApGudazi;
import fgoScript.entity.guda.QpApGudazi;
import fgoScript.entity.guda.TrainApGudazi;
import fgoScript.entity.guda.TrainApGudaziForMain;
import fgoScript.exception.FgoNeedNextException;
import fgoScript.exception.FgoNeedRestartException;
import fgoScript.service.AutoAct;
import fgoScript.service.ProcessDeal;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;

/**
 * 咕哒子类
 * 
 * @author zmczm
 *
 */
public class Gudazi extends TimerTask {
	
	private static final Logger LOGGER = LogManager.getLogger(Gudazi.class);
	private final int DELAY = GameConstant.DELAY;
	public static boolean ifRestart;
	private final Point DEAD_POINT = PointInfo.DEAD_POINT;
	public int[] getFgoArray() {
		return  GameUtil.strToIntArray(GameUtil.getValueFromConfig("FgoArray"),false);
	}

	public int[] getEventArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("EventArray"),false);
	}

	public int[] getApArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("apArray"),false);
	}
	public int[] getQpArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("qpArray"),false);
	}
	public int[] getExpArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("expArray"),false);
	}

	public int[] getFgoRewardArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("FgoRewardArray"),false);
	}
	public int[] getMainArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("mainArray"),false);
	}
	
	public static boolean isIfRestart() {
		return ifRestart;
	}

	public static void setIfRestart(boolean ifRestart) {
		Gudazi.ifRestart = ifRestart;
	}
	private final boolean IF_CLOSE = Boolean.parseBoolean(GameUtil.getValueFromConfig("IF_CLOSE"));
	private final String IMG_EXTEND = GameConstant.IMG_EXTEND;
	private String PREFIX = GameUtil.getPreFix();
	private Robot r;
	private int countNum;

	public Robot getR() {
		return r;
	}

	public void setR(Robot r) {
		this.r = r;
	}

	public void openTESTFGO() {
		// 打开窗口
		openWindow(8);
		// 登录签到
		try {
			open2Notice();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	public void onlyFight() throws Exception {
		String flag = PropertiesUtil.getValueFromColorFile("ifRestart");
		try {
			new EventGudazi().fightAndStop(StringUtils.isNoneBlank(flag) ? Boolean.valueOf(flag) : false, 0);
			autoClose();
			startBalanceForEvent(0);
		} catch (FgoNeedNextException e) {
			setIfRestart(true);
			onlyFight();
		} catch (FgoNeedRestartException e) {
			setIfRestart(true);
			onlyFight();
		} 
	}
	public void autoClose() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					LOGGER.info(GameUtil.getStackMsg(e));
				}
				GameUtil.setGO_FLAG(false);
				LOGGER.error("脚本自然死亡~");
			}
		}).start();
	}
	public void openAllFGO() throws Exception {
		new TrainApGudazi().startAllFgo(getFgoArray(), getApArray());
	}
	public void goAll() throws Exception{
		String[] qts = GameUtil.getValueFromConfig("QP_TRAIN_SELECTS").split("_");
		Calendar cd = Calendar.getInstance();
		int h = cd.get(Calendar.HOUR_OF_DAY);
		if (h > 4) {
			LOGGER.info("白天不执行登录检测，直接跳过");
		} else {
			if (Boolean.parseBoolean(GameUtil.getValueFromConfig("IF_SIGN"))) {
				signAllFGO();
			}
		}
		//小号刷资源
		switch(qts[0]){
		case "train" : {
			new TrainApGudazi().startAllFgo(getFgoArray(), getApArray());
			break;
		}
		case "qp" : {
			new QpApGudazi().startAllFgo(getFgoArray(), getQpArray());
			break;
		}
		default : {
			break;
		}
		}
		//主账号刷qp
		switch(qts[1]){
		case "train" : {
			new TrainApGudaziForMain().startAllFgo(getMainArray(), getApArray());
			break;
		}
		case "qp" : {
			new QpApGudazi().startAllFgo(getMainArray(), getQpArray());
			break;
		}
		case "exp" : {
			new ExpApGudazi().startAllFgo(getMainArray(), getExpArray());
			break;
		}
		case "event" : {
			new EventGudazi().startAllFgo(getEventArray(), getExpArray());
			break;
		}
		default : {
			break;
		}
		}
		allRewardAndRoll();
		if (IF_CLOSE) {
			closeComputer();
		}
	}
	public void startMainAccount() throws Exception{
		new QpApGudazi().startAllFgo(getMainArray(), getQpArray());
	}
	public void mainAccountQP40() throws Exception {
		int[] apArray = {40,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40};
		new QpApGudazi().startAllFgo(getMainArray(), apArray);
	}
	public void mainAccountEXP40() throws Exception {
		int[] expArray = {40,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40
				,40,40,40,40,40,40,40,40,40,40,40};
		new ExpApGudazi().startAllFgo(getMainArray(), expArray);
	}
	public void eventingFgo() throws Exception {
		new EventGudazi().startAllFgo(getEventArray(), getApArray());
	}

	public void openEvent() throws Exception {
		int account = FgoPanel.getAccount();
		countNum = FgoPanel.getAccount();;
		try {
			reNewRobot();
			// 打开窗口
			openWindow(account);
			// 检测loading
			beforeNotice(account);
			// 检测公告
			open2Notice();
			// 等待咕哒子(加号)页面
			waitForHomePage();
		} catch (FgoNeedNextException e) {
			openEvent();
		} catch (FgoNeedRestartException e) {
			openEvent();
		}
	}
	public void signAllFGO() throws Exception {
		int[] array = getFgoRewardArray();
		int tip = 0;
		int size = array.length;
		for (int i = 0; i < size; i++) {
			reNewRobot();
			tip = array[i];
			countNum = tip;
			// 单个账号行动开启
			try {
				signOneFGO(tip);
			} catch (FgoNeedNextException e) {
				LOGGER.info(e.getMessage());
				continue;
			} 
		}
	}
	/**
	 * 所有号领奖抽池
	 * @throws Exception 
	 */
	public void allRewardAndRoll() throws Exception {
		int[] array = getFgoRewardArray();
		int size = array.length;
		for (int i = 0; i < size; i++) {
			countNum = array[i];
			reNewRobot();
			// 单个账号行动开启
			try {
				oneRewardAndRoll(array[i]);
			} catch (FgoNeedNextException e) {
				LOGGER.info(e.getMessage());
				continue;
			}
		}
	}

	/**
	 * 一个号领奖抽池
	 * 
	 * @param tip
	 * @throws Exception
	 */
	public void oneRewardAndRoll(int tip) throws Exception {
		try {
			// 打开窗口
			openWindow(tip);
			// 检测loading
			beforeNotice(tip);
			// 检测公告
			open2Notice();
			// 等待咕哒子(加号)页面
			waitForHomePage();
			LOGGER.info("开始领取任务奖励");
			// 领取任务奖励
			getRewords();
			LOGGER.info("返回主页");
			// 等待咕哒子(加号)页面
			waitForHomePage();
			// 抽取免费池
			LOGGER.info("抽取免费池");
			pickFreeSummon();
			// 等3秒
			// 关闭游戏
			LOGGER.info("关闭当前游戏");
			closeFgoByForce();
		} catch (FgoNeedRestartException e) {
			LOGGER.info(e.getMessage());
			closeFgoByForce();
			reNewRobot();
			oneRewardAndRoll(tip);
		}
	}

	/**
	 * 获取任务奖励
	 * 
	 * @throws Exception
	 */
	private void getRewords() throws Exception {
		// 任务按钮
		Point p1 = new Point(572, 711);
		// 明咕哒子颜色
		Point p_guda = PointInfo.P_GUDA;
		Color c_guda = PointInfo.C_GUDA;
		// 暗咕哒子颜色
		Point p_guda_dark = PointInfo.P_GUDA_DARK;
		Color c_guda_dark = PointInfo.C_GUDA_DARK;
		
		List<PointColor> pcList = new ArrayList<PointColor>();
		pcList.add(new PointColor(p_guda, c_guda, p1, true));
		pcList.add(new PointColor(p_guda_dark, c_guda_dark, null, true));
		List<PointColor> finishPCList = new ArrayList<PointColor>();
		finishPCList.add(new PointColor(p_guda_dark, c_guda_dark, null, true));
		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
			}
		};
		ac.autoAct();
		// 点击周常按钮
		Point p4 = new Point(702, 194);// 周常按钮坐标
//		Point p5 = new Point(1060, 195);// 限定按钮坐标
		// 获取任务过滤按钮的1个坐标1个颜色 （可领取的颜色）
		Point p_reward_get = PointInfo.P_REWARD_GET;
		Color c_reward_get = PointInfo.C_REWARD_GET;
		GameUtil.mouseMoveByPoint(p4);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 循环7次尝试获取
		Color temp = null;
		boolean flag = false;
		int missonNum = 10;
		int statusMum = 5;
		for (int i = 0; i < missonNum; i++) {
			// 循环4次点击获取按钮，直到可获取状态，判断是否可以获取奖励
			for (int j = 0; j < statusMum; j++) {
				GameUtil.mouseMoveByPoint(p_reward_get);
				GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
				GameUtil.mouseMoveByPoint(p4);
				temp = GameUtil.getScreenPixel(p_reward_get);
				flag = GameUtil.isEqualColor(c_reward_get, temp);
				if (flag) {
					break;
				}
			}
			Point p3 = new Point(757, 323);// 可获取奖励状态坐标
			Color c3 = new Color(198, 154, 57);
			Point p6 = new Point(923, 392);// 领取任务坐标
			temp = GameUtil.getScreenPixel(p3);
			
			if (GameUtil.isEqualColor(c3, temp)) {
				GameUtil.mouseMoveByPoint(p6);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
				Point p7 = new Point(650, 580);// 颜色：30;30;30 也是关闭按钮
				Color c7 = new Color(30, 30, 30);
				Point p8 = new Point(722, 579);// 颜色：45;45;45
				Color c8 = new Color(45, 45, 45);

				PointColor pc7 = new PointColor(p7, c7, true);
				PointColor pc8 = new PointColor(p8, c8, true);
				pcList = new ArrayList<PointColor>();
				pcList.add(pc7);
				pcList.add(pc8);
				GameUtil.waitUntilAllColor(pcList, DELAY);
				// 关闭领取弹窗
				GameUtil.mouseMoveByPoint(p7);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);

				flag = true;
			}else {
				break;
			}
		}
		GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + countNum + "_奖励领取情况页面.");
		// 返回按钮
		Point p8 = new Point(138, 97);// 颜色：109;122;150 Color c = new Color(109, 122, 150);
		pcList = new ArrayList<PointColor>();
		pcList.add(new PointColor(p_guda, c_guda, null, true));
		pcList.add(new PointColor(p_guda_dark, c_guda_dark, p8, true));
		finishPCList = new ArrayList<PointColor>();
		finishPCList.add(new PointColor(p_guda, c_guda, null, true));
		ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
			}
		};
		ac.autoAct();
	}

	/**
	 * 抽取免费池子
	 * 
	 * @throws Exception
	 */
	private void pickFreeSummon() throws Exception {
		// 进入召唤池
		Point p = new Point(1228, 736);// 颜色：144;152;169 Color c = new Color(144, 152, 169);
		GameUtil.mouseMoveByPoint(p);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
		Point p1 = new Point(573, 701);// 颜色：46;65;178 Color c = new Color(46, 65, 178);
		GameUtil.mouseMoveByPoint(p1);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK, new PointColor(PointInfo.P_DOWN_PANEL_CLOSE, PointInfo.C_DOWN_PANEL_CLOSE, false));
		// 等待石头图标
		Point p2 = new Point(429, 89);// 颜色：167;231;209
		Color c2 = new Color(167, 231, 209);
		List<PointColor> pocoList = new ArrayList<PointColor>();
		pocoList.add(new PointColor(p2, c2, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY);
		// 切换左箭头
		Point p3 = new Point(85, 410);// 颜色：248;244;248 Color c = new Color(248, 244, 248);
		GameUtil.mouseMoveByPoint(p3);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 等待小手图标
		pocoList = new ArrayList<PointColor>();
		pocoList.add(new PointColor(PointInfo.P_HAND, PointInfo.C_HAND, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY);
		// 判断 免费召唤图标 并点击确定按钮抽取
		Point p5 = new Point(677, 603);// 颜色：31;167;202
		Color c5 = new Color(31, 167, 202);
		Color c5temp = GameUtil.getScreenPixel(p5);
		for (int i = 0; i < 5; i++) {
			if (GameUtil.isEqualColor(c5, c5temp)) {
				GameUtil.mouseMoveByPoint(p5);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
				Point p6 = new Point(886, 609);
				GameUtil.mouseMoveByPoint(p6);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
				// 等待3秒
				r.delay(10 * DELAY);
				break;
			}
		}
		
	}
	/***
	 * 签到一个FGO
	 * 
	 * @throws Exception
	 */
	public void signOneFGO(int tip) throws Exception {
		try {
			// 打开窗口
			openWindow(tip);
			// 检测loading
			beforeNotice(tip);
			// 检测公告
			open2Notice();
			waitForHomePage();
			// 关闭游戏
			closeFgoByForce();
		} catch (FgoNeedRestartException e) {
			closeFgoByForce();
			reNewRobot();
			signOneFGO(tip);
		}
	}
	private void beforeNotice(int tip) throws Exception {
		// 检测异动前的FGO游戏图标
		Point p_left_top = PointInfo.P_LEFT_TOP;
		Color c_left_top = PointInfo.C_LEFT_TOP;
		List<PointColor> pocoList = new ArrayList<PointColor>();
		pocoList.add(new PointColor(p_left_top, c_left_top, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY);

		// 移动窗口至左上角
		GameUtil.moveToLeftTop();

		// 检测loading是否完毕
		Point p_loading = new Point(516, 722);// 颜色：247;255;255
		Color c_loading = new Color(247, 255, 255);

		Point p_transfer = new Point(911, 670);// 颜色：0;60;165
		Color c_transfer = new Color(0, 60, 165);

		List<PointColor> pcList = new ArrayList<PointColor>();
		pcList.add(new PointColor(p_loading, c_loading, PointInfo.DEAD_POINT, true));
		pcList.add(new PointColor(p_transfer, c_transfer, PointInfo.DEAD_POINT, true));
		List<PointColor> finishPCList = new ArrayList<PointColor>();
		finishPCList.add(new PointColor(p_transfer, c_transfer, PointInfo.DEAD_POINT, true));
		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
				Color cWait = this.getPcWait().getColor();
				if (GameUtil.isEqualColor(c_transfer, cWait)) {
					GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + countNum + "_引继页面.");
				}
			}
		};
		ac.autoAct();
	}



	private void reNewRobot() {
		GameUtil.reNewRobot();
		r= GameUtil.getRb();
	}


	private void startBalanceForEvent(int count) throws Exception {
		LOGGER.info("结算侦测");
		// 死角点
		Point dead_point = PointInfo.DEAD_POINT;
		// 复位点
		Point p_reset = PointInfo.P_RESET;
		// 羁绊三角1
		Point p_fetter01 = PointInfo.P_FETTER01;
		Color c_fetter01 = PointInfo.C_FETTER01;
		// 羁绊三角2
		Point p_fetter02 = PointInfo.P_FETTER02;
		Color c_fetter02 = PointInfo.C_FETTER02;
		// 羁绊升级
		Point p_fetter_up = PointInfo.P_FETTER_UP;
		Color c_fetter_up = PointInfo.C_FETTER_UP;
		// 确认点
		Point p_confirm_rd = PointInfo.P_CONFIRM_RD;
		Color c_confirm_rd = PointInfo.C_CONFIRM_RD;
		//好友申请拒绝点
		Point p_get_friend_no = PointInfo.P_GET_FRIEND_NO;
		Color c_get_friend_no = PointInfo.C_GET_FRIEND_NO;
		// 咕哒子
		Point p_guda = PointInfo.P_GUDA;
		Color c_guda = PointInfo.C_GUDA;
		// 获取奖励动态坐标颜色
		Point p_reward_action = PointInfo.P_REWARD_ACTION;
		Color c_reward_action = PointInfo.C_REWARD_ACTION;
		
		List<PointColor> pcList = new ArrayList<PointColor>();
		pcList.add(new PointColor(p_fetter01, c_fetter01, p_reset, true));
		pcList.add(new PointColor(p_fetter02, c_fetter02, p_reset, true));
		pcList.add(new PointColor(p_fetter_up, c_fetter_up, p_reset, true));
		pcList.add(new PointColor(p_confirm_rd, c_confirm_rd, p_confirm_rd, true));
		pcList.add(new PointColor(p_get_friend_no, c_get_friend_no, p_get_friend_no, true));
		pcList.add(new PointColor(p_guda, c_guda, dead_point, true));
		pcList.add(new PointColor(p_reward_action, c_reward_action, p_reward_action, true));
		List<PointColor> finishPCList = new ArrayList<PointColor>();
		finishPCList.add(new PointColor(p_guda, c_guda, dead_point, true));
		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
				Color cWait = this.getPcWait().getColor();
				if (GameUtil.isEqualColor(c_confirm_rd, cWait)) {
					GameUtil.img2file(IMG_EXTEND, PREFIX + "账号" + countNum + "_第" +  count + "战斗奖励页面.");
				}
			}
		};
		ac.autoAct();
	}


	private PointColor open2Notice() throws Exception {
		// 公告×点
		Point p_notice_exit = PointInfo.P_NOTICE_EXIT;
		Color c_notice_exit = PointInfo.C_NOTICE_EXIT;
		// 公告×点
		Point p_notice_exit_dark = PointInfo.P_NOTICE_EXIT_DARK;
		Color c_notice_exit_dark = PointInfo.C_NOTICE_EXIT_DARK;
		// 盲点
		Point dead_point = PointInfo.DEAD_POINT;
		// 咕哒子
		Point p_guda = PointInfo.P_GUDA;
		Color c_guda = PointInfo.C_GUDA;
		// 引继页面
		Point p_transfer = new Point(911, 670);// 颜色：0;60;165
		Color c_transfer = new Color(0, 60, 165);
		// 再开按钮（是）
		Point p_button_restart_yes = PointInfo.P_BUTTON_RESTART_YES;
		Color c_button_restart_yes = PointInfo.C_BUTTON_RESTART_YES;

		List<PointColor> pcList = new ArrayList<PointColor>();
		pcList.add(new PointColor(p_guda, c_guda, dead_point, true));
		pcList.add(new PointColor(p_notice_exit, c_notice_exit, p_notice_exit, true));
		pcList.add(new PointColor(p_notice_exit_dark, c_notice_exit_dark, p_notice_exit_dark, true));
		pcList.add(new PointColor(p_transfer, c_transfer, PointInfo.DEAD_POINT, true));
		pcList.add(new PointColor(p_button_restart_yes, c_button_restart_yes, p_button_restart_yes, true));
		List<PointColor> finishPCList = new ArrayList<PointColor>();
		finishPCList.add(new PointColor(p_guda, c_guda, dead_point, true));
		finishPCList.add(new PointColor(p_button_restart_yes, c_button_restart_yes, p_button_restart_yes, true));

		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
			}
		};
		ac.autoAct();
		PointColor pc = ac.getPcWait();
		return pc;
	}



	public void doWaitUntilColor(Robot rb, List<PointColor> pocoList, int delay) {
		boolean flag = true;
		Point p;
		Color c0;
		Color c1;
		boolean isEqual;
		int count = 0;
		int size = pocoList.size();
		do {
			count = 0;

			rb.delay(delay);
			rb.delay(delay);
			rb.delay(delay);
			GameUtil.mouseMoveByPoint(DEAD_POINT);
			GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			for (int i = 0; i < size; i++) {
				PointColor pointColor = pocoList.get(i);
				p = pointColor.getPoint();
				c0 = pointColor.getColor();
				isEqual = pointColor.isEqual();
				c1 = GameUtil.getScreenPixel(p);
				flag = c1.getRed() == c0.getRed() && c1.getGreen() == c0.getGreen() && c1.getBlue() == c0.getBlue();
				if (!isEqual) {
					flag = !flag;
				}
				if (flag) {
					count++;
				}
			}
			rb.delay(delay);
			rb.delay(delay);
		} while (count != size);
	}
	
	/**
	 * 打开窗口方法
	 */
	private void openWindow(int location) {
		ProcessDeal.startTianTian(location);
	}

	/**
	 * 将字符串复制到剪切板。
	 */
	public void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}

	/**
	 * 召唤 决定 space 一直按，每按一次 检测一次 颜色 如果锁颜色匹配，点退出按钮 如果menu颜色匹配，继续召唤
	 */
	public void summon() {
		int result = 0;
		keyPressAndRelease(KeyEvent.VK_Z);
		r.delay(DELAY);
		keyPressAndRelease(KeyEvent.VK_V);
		do {
			keyPressAndRelease(KeyEvent.VK_SPACE);
			result = checkSummon();
		} while (result == 0);
		if (result == 1) {
			r.delay(DELAY);
			r.delay(DELAY);
			if (checkSummonGotShiJiang() == 0 && checkSummonGotHeiZhen() == 0) {
				keyPressAndRelease(KeyEvent.VK_Q);
				summon();
			}
		}
		if (result == 2) {
			summon();
		}
		if (result == 3) {
			keyPressAndRelease(KeyEvent.VK_Z);
			summon();
		}
		if (result == 4) {
			keyPressAndRelease(KeyEvent.VK_P);
			r.delay(DELAY);
			r.delay(DELAY);
			keyPressAndRelease(KeyEvent.VK_Q);
			summon();
		}
	}

	private int checkSummonGotShiJiang() {
		Color c1 = r.getPixelColor(653, 583);
		if (c1.getRed() == 111 && c1.getGreen() == 91 && c1.getBlue() == 119) {
			return 1;
		}
		return 0;
	}

	private int checkSummonGotHeiZhen() {
		Color c1 = r.getPixelColor(584, 563);
		if (c1.getRed() == 239 && c1.getGreen() == 231 && c1.getBlue() == 198) {
			return 1;
		}
		return 0;
	}

	private int checkSummon() {
		Color c1 = r.getPixelColor(383, 488);
		Color c2 = r.getPixelColor(1487, 889);
		Color c3 = r.getPixelColor(871, 769);
		Color c4 = r.getPixelColor(1622, 237);
		if (c1.getRed() == 229 && c1.getGreen() == 230 && c1.getBlue() == 246) {
			return 1;
		}
		if (c2.getRed() == 222 && c2.getGreen() == 219 && c2.getBlue() == 222) {
			return 2;
		}
		if (c3.getRed() == 215 && c3.getGreen() == 215 && c3.getBlue() == 215) {
			return 3;
		}
		if (c4.getRed() == 214 && c4.getGreen() == 215 && c4.getBlue() == 214) {
			return 4;
		}
		return 0;
	}
	private void keyPressAndRelease(int key) {
		r.keyPress(key);
		r.delay(DELAY);
		r.keyRelease(key);
		r.delay(DELAY);
	}
	public void showPositionAndColor() {
		Point p = GameUtil.getMousePosition();
		Color color = GameUtil.getScreenPixel(p);
		String rgbStr = color.getRed() + ";" + color.getGreen() + ";" + color.getBlue();
		String showText = "坐标: " + p.getX() + ":" + p.getY() + " 颜色: " + rgbStr;
		JOptionPane.showMessageDialog(null, showText, "坐标颜色", JOptionPane.WARNING_MESSAGE);
		String pointCode = "public static Point p = new Point(" + (int) p.getX() + "," + (int) p.getY() + ");";
		String rgbCode = "public static Color c = new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue()
				+ ");	";
		String clipText = pointCode + rgbCode;
		setSysClipboardText(clipText);
	}

	public void moveToZero() {
		r.mouseMove(0, 0);
	}

	/***
	 * 强制关闭fgo
	 */
	private void closeFgoByForce() {
		ProcessDeal.killAllTianTian();
	}
	private void closeComputer() {
		ProcessDeal.closeComputer();
	}
	public Gudazi() {
		r = GameUtil.getRb();
	}
	
	private void waitForHomePage() throws Exception {
		r.delay(DELAY*5);
		// 公告×点
		Point p_notice_exit = PointInfo.P_NOTICE_EXIT;
		Color c_notice_exit = PointInfo.C_NOTICE_EXIT;
		// 公告×点
		Point p_notice_exit_dark = PointInfo.P_NOTICE_EXIT_DARK;
		Color c_notice_exit_dark = PointInfo.C_NOTICE_EXIT_DARK;
		// 盲点
		Point dead_point = PointInfo.DEAD_POINT;
		// 咕哒子
		Point p_guda = PointInfo.P_GUDA;
		Color c_guda = PointInfo.C_GUDA;

		List<PointColor> pcList = new ArrayList<PointColor>();
		pcList.add(new PointColor(p_guda, c_guda, dead_point, true));
		pcList.add(new PointColor(p_notice_exit, c_notice_exit, p_notice_exit, true));
		pcList.add(new PointColor(p_notice_exit_dark, c_notice_exit_dark, p_notice_exit_dark, true));
		List<PointColor> finishPCList = new ArrayList<PointColor>();
		finishPCList.add(new PointColor(p_guda, c_guda, dead_point, true));
 
		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
			}
		};
		ac.autoAct();
	}

	public Map<String, List<CommonCard>> getWeakCommondCards(Comparator<CommonCard> comp){
		Point p_card_click = PointInfo.P_CARD_CLICK;
		Point p_card_color = PointInfo.P_CARD_COLOR;
		Point p_card_weak = PointInfo.P_CARD_WEAK;
		Point pLoc = null;
		Point pColor = null;
		Point pWeak = null;
		Color cColor = null;
		CommonCard cc = null;
		List<CommonCard> ccList = new ArrayList<CommonCard>();
		for (int i = 0; i < 5; i++) {
			pLoc = new Point((int) p_card_click.getX() + i * 260, (int) p_card_click.getY());
			pColor = new Point((int) p_card_color.getX() + i * 260, (int) p_card_color.getY());
			pWeak = new Point((int) p_card_weak.getX() + i * 260, (int) p_card_weak.getY());
			
			cc = new CommonCard();
			cc.setpLoc(pLoc);
			//判断颜色
			cColor = GameUtil.getScreenPixel(pColor);
//			LOGGER.info(cColor.toString());
			ColorVo[] voArray = {new ColorVo(GameConstant.RED, cColor.getRed())
					, new ColorVo(GameConstant.GREEN, cColor.getGreen()) 
					, new ColorVo(GameConstant.BLUE, cColor.getBlue()) 
					};
			ColorVo tempVo = null;
			//冒泡排序从小到大
			int num = voArray.length;
			for (int j = 0; j < num; j++) {
				for (int k = 0; k < voArray.length - j - 1; k++) {
					if (voArray[k].getValue() > voArray[k+1].getValue()) {
						tempVo=voArray[k+1];
						voArray[k+1] = voArray[k];
						voArray[k] = tempVo;
					}
				}
			}
			//设置排序最大的为颜色
//			LOGGER.info(voArray[voArray.length-1].getColor());
			cc.setCardColor(voArray[voArray.length-1].getColor());
			//判断克制
			cColor = GameUtil.getScreenPixel(pWeak);
//			LOGGER.info(cColor.toString());
			if (cColor.getGreen() <50 && cColor.getBlue()<50) {
				cc.setWeak(true);	
			}else {
				cc.setWeak(false);	
			}
//			LOGGER.info(cc.isWeak());
			ccList.add(cc);
		}
//		LOGGER.info("======================");
		Collections.sort(ccList, comp);
		 Map<String, List<CommonCard>> scMap = new HashMap<>();
		 List<CommonCard> trueList = new ArrayList<CommonCard>();
		 List<CommonCard> falseList = new ArrayList<CommonCard>();
		int num = ccList.size();
		for (int i = 0; i < num; i++) {
			CommonCard commonCard = ccList.get(i);
//			LOGGER.info(commonCard.getCardColor() + "_");
//			LOGGER.info(commonCard.isWeak());
			if (commonCard.isWeak()==true) {
				trueList.add(commonCard);
			}else {
				falseList.add(commonCard);
			}
		}
		scMap.put("trueList", trueList);
		scMap.put("falseList", falseList);
		return scMap;
	}
	@Override
	public void run() {
		Calendar cd = Calendar.getInstance();
		int h = cd.get(Calendar.HOUR_OF_DAY);
		if (h > 9) {
			this.cancel();
			LOGGER.info("9点以后执行刷本计划！");
			LOGGER.info("此次计划结束后将结束计划并关机！");
		} 
		ZButton btTemp = FgoPanel.instance().getBts()[3];
		btTemp.setEnabled(btTemp.isEnableStatus());
		btTemp.setExcuteColor();
		FgoPanel.setExcutebleText(btTemp);
		btTemp.run();
		if (GameUtil.isSTOP_SCRIPT()==true) {
			LOGGER.info(">>>>>>>>>>  当前计划已经终止！     <<<<<<<<<<<");
		}else {
			if (h > 9 ) {
				closeComputer();
				LOGGER.info(">>>>>>>>>>  关机中，请勿操作电脑     <<<<<<<<<<<");
			}
		}
	}
	public static void main(String[] args) {
	}
}
