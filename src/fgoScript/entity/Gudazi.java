package fgoScript.entity;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fgoScript.entity.panel.FgoFrame;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.guda.*;
import fgoScript.exception.FgoNeedNextException;
import fgoScript.exception.FgoNeedRestartException;
import fgoScript.service.AutoAct;
import fgoScript.service.CommonMethods;
import commons.util.ProcessDealUtil;
import commons.util.ClipBoardUtil;
import commons.util.GameUtil;
import commons.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;

/**
 * 咕哒子类
 *
 * @author zmczm
 *
 */
public class Gudazi extends TimerTask {

	private static final Logger LOGGER = LogManager.getLogger(Gudazi.class);
	private final int DELAY = GameConstant.DELAY;
	private static boolean ifRestart;
	private final Point DEAD_POINT = PointInfo.DEAD_POINT;

	public static boolean isIfRestart() {
		return ifRestart;
	}

	private static void setIfRestart(boolean ifRestart) {
		Gudazi.ifRestart = ifRestart;
	}
	private final boolean IF_CLOSE = Boolean.parseBoolean(GameUtil.getValueFromConfig("IF_CLOSE"));
	private final String IMG_EXTEND = GameConstant.IMG_EXTEND;
	private final String PREFIX = GameUtil.getPreFix();
	private Robot r;
	private int countNum;
	private int[] getFgoRewardArray() {
		return GameUtil.strToIntArray(GameUtil.getValueFromConfig("FgoRewardArray"),false);
	}
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
			CommonMethods.open2GudaOrRestart();
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
		} catch (FgoNeedNextException | FgoNeedRestartException e) {
			setIfRestart(true);
			onlyFight();
		}
	}
	private void autoClose() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat("demo-pool-%d").build();
		ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		singleThreadPool.execute(()-> {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				LOGGER.info(GameUtil.getStackMsg(e));
			}
			GameUtil.setGO_FLAG(false);
			LOGGER.error("脚本自然死亡~");
		});
		singleThreadPool.shutdown();
	}
	public void trainSamllFgo() throws Exception {
		ApGudaziFactory.getInstance("train", "small", null).startAllFgo();
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
		ApGudaziFactory.getInstance(qts[0],"small", null).startAllFgo();
		//主账号刷qp
		ApGudaziFactory.getInstance(qts[0],"big", null).startAllFgo();
		//抽奖
		allRewardAndRoll();
		//是否关机
		if (IF_CLOSE) {
			closeComputer();
		}
	}
	public void startMainAccount() throws Exception{
		ApGudaziFactory.getInstance("qp", "big", null).startAllFgo();
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
		String[] qts = GameUtil.getValueFromConfig("QP_TRAIN_SELECTS").split("_");
		ApGudaziFactory.getInstance(qts[1], "big", apArray).startAllFgo();
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
		String[] qts = GameUtil.getValueFromConfig("QP_TRAIN_SELECTS").split("_");
		ApGudaziFactory.getInstance(qts[1], "big", expArray).startAllFgo();

	}
	public void eventingFgo() throws Exception {
		ApGudaziFactory.getInstance("event", "event", null).startAllFgo();
	}

	public void openEvent() throws Exception {
		int account = FgoFrame.getAccount();
		countNum = FgoFrame.getAccount();
		try {
			reNewRobot();
			// 打开窗口
			openWindow(account);
			// 检测loading
			beforeNotice();
			// 检测公告
			CommonMethods.open2GudaOrRestart();
			// 等待咕哒子(加号)页面
			waitForHomePage();
		} catch (FgoNeedNextException | FgoNeedRestartException e) {
			openEvent();
		}
	}
	public void signAllFGO() throws Exception {
		int[] array = getFgoRewardArray();
		int tip;
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
			}
		}
	}

	/**
	 * 一个号领奖抽池
	 *
	 * @param tip 账号
	 * @throws Exception 异常
	 */
	private void oneRewardAndRoll(int tip) throws Exception {
		try {
			// 打开窗口
			openWindow(tip);
			// 检测loading
			beforeNotice();
			// 检测公告
			CommonMethods.open2GudaOrRestart();
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
	 * @throws Exception //
	 */
	private void getRewords() throws Exception {
		// 任务按钮
		Point p1 = new Point(572, 711);
		GameUtil.mouseMoveByPoint(p1);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
		List<PointColor> pcList = new ArrayList<>();
		// 点击周常按钮
		Point p4 = new Point(702, 194);// 周常按钮坐标
//		Point p5 = new Point(1060, 195);// 限定按钮坐标
		// 获取任务过滤按钮的1个坐标1个颜色 （可领取的颜色）
		Point pRewardGet = PointInfo.P_REWARD_GET;
		Color cRewardGet = PointInfo.C_REWARD_GET;
		GameUtil.mouseMoveByPoint(p4);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 循环7次尝试获取
		Color temp;
		boolean flag;
		int missionNum = 10;
		int statusMum = 5;

		Point p3 = new Point(757, 323);// 可获取奖励状态坐标
		Color c3 = new Color(198, 154, 57);
		Point p6 = new Point(923, 392);// 领取任务坐标

		Point p7 = new Point(650, 580);// 颜色：30;30;30 也是关闭按钮
		Color c7 = new Color(30, 30, 30);
		Point p8 = new Point(722, 579);// 颜色：45;45;45
		Color c8 = new Color(45, 45, 45);

		PointColor pc7 = new PointColor(p7, c7, true);
		PointColor pc8 = new PointColor(p8, c8, true);
		for (int i = 0; i < missionNum; i++) {
			// 循环4次点击获取按钮，直到可获取状态，判断是否可以获取奖励
			for (int j = 0; j < statusMum; j++) {
				GameUtil.mouseMoveByPoint(pRewardGet);
				GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
				GameUtil.mouseMoveByPoint(p4);
				temp = GameUtil.getScreenPixel(pRewardGet);
				flag = GameUtil.likeEqualColor(cRewardGet, temp);
				if (flag) {
					break;
				}
			}
			temp = GameUtil.getScreenPixel(p3);
			if (GameUtil.likeEqualColor(c3, temp)) {
				GameUtil.mouseMoveByPoint(p6);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK,pc7);
				pcList = new ArrayList<>();
				pcList.add(pc7);
				pcList.add(pc8);
				GameUtil.waitUntilAllColor(pcList, DELAY);
				// 关闭领取弹窗
				GameUtil.mouseMoveByPoint(p7);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
			}else {
				break;
			}
		}
		GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + countNum + "_奖励领取情况页面.");
		// 返回按钮
		Point pReturn = new Point(138, 97);// 颜色：109;122;150 Color c = new Color(109, 122, 150);
		GameUtil.mouseMoveByPoint(pReturn);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
	}

	/**
	 * 抽取免费池子
	 *
	 * @throws Exception 异常
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
		List<PointColor> pocoList = new ArrayList<>();
		pocoList.add(new PointColor(p2, c2, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY);
		// 切换左箭头
		Point p3 = new Point(85, 410);// 颜色：248;244;248 Color c = new Color(248, 244, 248);
		GameUtil.mouseMoveByPoint(p3);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 等待小手图标
		pocoList = new ArrayList<>();
		pocoList.add(new PointColor(PointInfo.P_HAND, PointInfo.C_HAND, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY);
		// 判断 免费召唤图标 并点击确定按钮抽取
		Point p5 = new Point(677, 603);// 颜色：31;167;202
		Color c5 = new Color(31, 167, 202);
		Color c5temp = GameUtil.getScreenPixel(p5);
		for (int i = 0; i < 5; i++) {
			if (GameUtil.likeEqualColor(c5, c5temp)) {
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
	 * @throws Exception 异常
	 */
	private void signOneFGO(int tip) throws Exception {
		try {
			// 打开窗口
			openWindow(tip);
			// 检测loading
			beforeNotice();
			// 检测公告
			CommonMethods.open2GudaOrRestart();
			waitForHomePage();
			// 关闭游戏
			closeFgoByForce();
		} catch (FgoNeedRestartException e) {
			closeFgoByForce();
			reNewRobot();
			signOneFGO(tip);
		}
	}
	private void beforeNotice() throws Exception {
		// 检测异动前的FGO游戏图标
		Point pLeftTop = PointInfo.P_LEFT_TOP;
		Color cLeftTop = PointInfo.C_LEFT_TOP;
		List<PointColor> pocoList = new ArrayList<>();
		pocoList.add(new PointColor(pLeftTop, cLeftTop, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY);

		// 移动窗口至左上角
		GameUtil.moveToLeftTop();

		// 检测loading是否完毕
		Point pLoading = PointInfo.P_LOADING;
		Color cLoading = PointInfo.C_LOADING;

		Point pTransfer = PointInfo.P_TRANSFER;
		Color cTransfer = PointInfo.C_TRANSFER;

		List<PointColor> pcList = new ArrayList<>();
		pcList.add(new PointColor(pLoading, cLoading, PointInfo.DEAD_POINT, true));
		pcList.add(new PointColor(pTransfer, cTransfer, PointInfo.DEAD_POINT, true));
		List<PointColor> finishPCList = new ArrayList<>();
		finishPCList.add(new PointColor(pTransfer, cTransfer, PointInfo.DEAD_POINT, true));
		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
				Color cWait = this.getPcWait().getColor();
				if (GameUtil.likeEqualColor(cTransfer, cWait)) {
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
		Point deadPoint = PointInfo.DEAD_POINT;
		// 复位点
		Point pReset = PointInfo.P_RESET;
		// 羁绊三角1
		Point pFetter01 = PointInfo.P_FETTER01;
		Color cFetter01 = PointInfo.C_FETTER01;
		// 羁绊三角2
		Point pFetter02 = PointInfo.P_FETTER02;
		Color cFetter02 = PointInfo.C_FETTER02;
		// 羁绊升级
		Point pFetterUp = PointInfo.P_FETTER_UP;
		Color cFetterUp = PointInfo.C_FETTER_UP;
		// 确认点
		Point pConfirmRd = PointInfo.P_CONFIRM_RD;
		Color cConfirmRd = PointInfo.C_CONFIRM_RD;
		//好友申请拒绝点
		Point pGetFriendNo = PointInfo.P_GET_FRIEND_NO;
		Color cGetFriendNo = PointInfo.C_GET_FRIEND_NO;
		// 咕哒子
		Point pGuda = PointInfo.P_GUDA;
		Color cGuda = PointInfo.C_GUDA;
		// 获取奖励动态坐标颜色
		Point pRewardAction = PointInfo.P_REWARD_ACTION;
		Color cRewardAction = PointInfo.C_REWARD_ACTION;

		List<PointColor> pcList = new ArrayList<>();
		pcList.add(new PointColor(pFetter01, cFetter01, pReset, true));
		pcList.add(new PointColor(pFetter02, cFetter02, pReset, true));
		pcList.add(new PointColor(pFetterUp, cFetterUp, pReset, true));
		pcList.add(new PointColor(pConfirmRd, cConfirmRd, pConfirmRd, true));
		pcList.add(new PointColor(pGetFriendNo, cGetFriendNo, pGetFriendNo, true));
		pcList.add(new PointColor(pGuda, cGuda, deadPoint, true));
		pcList.add(new PointColor(pRewardAction, cRewardAction, pRewardAction, true));
		List<PointColor> finishPCList = new ArrayList<>();
		finishPCList.add(new PointColor(pGuda, cGuda, deadPoint, true));
		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
				Color cWait = this.getPcWait().getColor();
				if (GameUtil.likeEqualColor(cConfirmRd, cWait)) {
					GameUtil.img2file(IMG_EXTEND, PREFIX + "账号" + countNum + "_第" +  count + "战斗奖励页面.");
				}
			}
		};
		ac.autoAct();
	}


	/**
	 * 打开窗口方法
	 */
	private void openWindow(int location) {
		ProcessDealUtil.startFgo(location);
	}

	/**
	 * 将字符串复制到剪切板。
	 */
	private void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}

	/**
	 * 召唤 决定 space 一直按，每按一次 检测一次 颜色 如果锁颜色匹配，点退出按钮 如果menu颜色匹配，继续召唤
	 */
	private void summon() {
		int result;
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
		Map<String,String> ShowAndJsonMap =  getShowAndJsonContentByPointAndColor(p, color);
		String jsonText =ShowAndJsonMap.get("jsonText");
		String showText =ShowAndJsonMap.get("showText");
		setSysClipboardText(jsonText);
		JOptionPane.showMessageDialog(null, showText, "坐标颜色", JOptionPane.WARNING_MESSAGE);

	}
	public void moveToPositionByClipBoard() throws Exception {
		String text = ClipBoardUtil.getSysClipboardText();
		if (StringUtils.isNotBlank(text)) {
			PointColor pc = JSONObject.parseObject(text, PointColor.class);
			// 获取新颜色
			pc.setColor(GameUtil.getScreenPixel(pc.getPoint()));
			// 移动
			GameUtil.mouseMoveByPoint(pc.getPoint());
			// 将json存入text中
			Map<String,String> ShowAndJsonMap =  getShowAndJsonContentByPointAndColor(pc.getPoint(), pc.getColor());
			setSysClipboardText(ShowAndJsonMap.get("jsonText"));
		} else {
			throw new Exception("the json String in clipBord is empty !");
		}

	}

	/**
	 * 根据类型获取字符串
	 * @return Map<String,String> (jsonText/showText)
	 */
	private Map<String,String> getShowAndJsonContentByPointAndColor(Point p, Color color){
		Map<String,String> map = new HashMap<>();
		String jsonText;
		String showText;
		String rgbStr;
		String pointCode;
		String rgbCode;
		rgbStr = color.getRed() + ";" + color.getGreen() + ";" + color.getBlue();
		pointCode = "{\n" +
				"\t\t\"point\" : {\"x\" : " + (int) p.getX() + ", \"y\" : " + (int) p.getY() + "},\n";
		rgbCode =        "\t\t\"color\" : {\"r\" : " + color.getRed() + ", \"g\" : " + color.getGreen() + ", \"b\" : " + color.getBlue()+"}\n"
				+"}";
		jsonText = pointCode + rgbCode;
		rgbStr = color.getRed() + ";" + color.getGreen() + ";" + color.getBlue();
		showText = "坐标: " + p.getX() + ":" + p.getY() + " 颜色: " + rgbStr;
		map.put("jsonText",jsonText);
		map.put("showText",showText);
		return map;
	}
	public void moveToZero() {
		r.mouseMove(0, 0);
	}

	/***
	 * 强制关闭fgo
	 */
	private void closeFgoByForce() {
		ProcessDealUtil.killAllTianTian();
	}
	private void closeComputer() {
		ProcessDealUtil.closeComputer();
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

		List<PointColor> pcList = new ArrayList<>();
		pcList.add(new PointColor(p_guda, c_guda, dead_point, true));
		pcList.add(new PointColor(p_notice_exit, c_notice_exit, p_notice_exit, true));
		pcList.add(new PointColor(p_notice_exit_dark, c_notice_exit_dark, p_notice_exit_dark, true));
		List<PointColor> finishPCList = new ArrayList<>();
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
		Point pLoc;
		Point pColor;
		Point pWeak;
		Color cColor;
		CommonCard cc;
		List<CommonCard> ccList = new ArrayList<>();
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
			ColorVo tempVo;
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
		ccList.sort(comp);
		 Map<String, List<CommonCard>> scMap = new HashMap<>();
		 List<CommonCard> trueList = new ArrayList<>();
		 List<CommonCard> falseList = new ArrayList<>();
		int num = ccList.size();
		for (int i = 0; i < num; i++) {
			CommonCard commonCard = ccList.get(i);
			if (commonCard.isWeak()) {
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
		BaseZButton btTemp = FgoFrame.instance().getBts()[3];
		btTemp.setEnabled(btTemp.isEnableStatus());
		btTemp.setExcuteColor();
		btTemp.setExcutebleText();
		btTemp.run();
		if (GameUtil.isSTOP_SCRIPT()) {
			LOGGER.info(">>>>>>>>>>  当前计划已经终止！     <<<<<<<<<<<");
		}else {
			if (h > 9 ) {
				closeComputer();
				LOGGER.info(">>>>>>>>>>  关机中，请勿操作电脑     <<<<<<<<<<<");
			}
		}
	}
}
