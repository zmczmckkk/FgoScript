package fgoScript.entity;

import aoshiScript.entity.WuNa;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import commons.entity.Constant;
import commons.util.*;
import destinychild.DaillyMission;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.guda.ApGudaziFactory;
import fgoScript.entity.guda.EventGudazi;
import fgoScript.entity.panel.FgoFrame;
import fgoScript.exception.AppNeedNextException;
import fgoScript.exception.AppNeedRestartException;
import fgoScript.exception.AppNeedUpdateException;
import fgoScript.service.AutoAct;
import fgoScript.service.CommonMethods;
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
	private static final PointInfo POINT_INFO = PointInfo.getSpringBean();

	private static final Logger LOGGER = LogManager.getLogger(Gudazi.class);
	private final int DELAY = GameConstant.DELAY;
	private static boolean ifRestart;

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
		String flag = PropertiesUtil.getValueFromSkillsFile("ifRestart");
		boolean stopFlag = false;
		GameUtil.setSTOP_SCRIPT(false);
		WuNa wuna = new WuNa("none");
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat("demo-pool-%d").build();
		ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


			for (int i = 0; i < GameConstant.BATTLE_COUNT; i++) {
				singleThreadPool.execute(()-> {
					wuna.alwaysClick();
				});
				Color tempColor;
				do {
					tempColor = GameUtil.getScreenPixel(POINT_INFO.getpBlueAttack());
					if(GameUtil.isSTOP_SCRIPT()){
						stopFlag=true;
						break;
					}
				} while (!GameUtil.likeEqualColor(POINT_INFO.getcBlueAttack(), tempColor));
				if(stopFlag){
					wuna.setGO(false);
					break;
				}
				wuna.setGO(false);
				int n = 0;
				while ((n = singleThreadPool.getActiveCount())!=0){
					LOGGER.info("线程个数" + singleThreadPool.getActiveCount());
					GameUtil.delay(1500);
				}
				try {
					new EventGudazi().fightAndStop(i == 0 ? false : true, 0);
				} catch (AppNeedNextException | AppNeedRestartException e) {
					LOGGER.info("无所谓！接着走~");
				}
				LOGGER.info("进入下一个循环！");
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
		//执行DC挂机任务
		DaillyMission.getSpringBean().toggle(false);
		//小号刷资源
		ApGudaziFactory.getInstance(qts[0],"small", null).startAllFgo();
		//主账号刷qp
		ApGudaziFactory.getInstance(qts[1],"big", null).startAllFgo();
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
		ApGudaziFactory.getInstance("exp", "big", expArray).startAllFgo();

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
			ProcessDealUtil.startApp(countNum);
			moveWindowToLeftTop();
			// 检测loading
			checkLoading();
			// 检测公告
			CommonMethods.open2GudaOrRestart();
			// 等待咕哒子(加号)页面
			waitForHomePage();
		} catch (AppNeedNextException | AppNeedRestartException e) {
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
			} catch (AppNeedNextException e) {
				LOGGER.info(e.getMessage());
			} catch (AppNeedUpdateException e) {
				LOGGER.info(e.getMessage());
				installAllFGO();
				break;
			}
		}
	}
	public void installAllFGO() throws Exception {
		int[] array = getFgoRewardArray();
		int tip;
		int size = array.length;
		for (int i = 0; i < size; i++) {
			reNewRobot();
			tip = array[i];
			countNum = tip;
			// 单个账号行动开启
			try {
				updateAndSignOneFGO(tip);
			} catch (AppNeedNextException e) {
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
			} catch (AppNeedNextException e) {
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
			moveWindowToLeftTop();
			checkLoading();
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
		} catch (AppNeedRestartException e) {
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
		Point pRewardGet = POINT_INFO.getpRewardGet();
		Color cRewardGet = POINT_INFO.getcRewardGet();
		GameUtil.mouseMoveByPoint(p4);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 循环7次尝试获取
		Color temp;
		boolean flag;
		int missionNum = 10;
		int statusMum = 5;

		Point pCompleteLabel = new Point(733, 325);// 完成标签
		Color cCompleteLabel = new Color(199, 148, 46);

		Point pGetRewardClick = new Point(923, 392);// 领取任务点击

		Point pStoneGetAndClose = new Point(641, 582);// 石头领取关闭
		Color cStoneGetAndClose = new Color(115, 115, 115);

		PointColor pcStoneGetAndClose = new PointColor(pStoneGetAndClose, cStoneGetAndClose, true);
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
			temp = GameUtil.getScreenPixel(pCompleteLabel);
			if (GameUtil.likeEqualColor(cCompleteLabel, temp)) {
				GameUtil.mouseMoveByPoint(pGetRewardClick);
				GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK,pcStoneGetAndClose);
				pcList = new ArrayList<>();
				pcList.add(pcStoneGetAndClose);
				GameUtil.waitUntilAllColor(pcList, DELAY, Constant.FGOMonitor);
				// 关闭领取弹窗
				GameUtil.mouseMoveByPoint(pStoneGetAndClose);
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
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK, new PointColor(POINT_INFO.getpDownPanelClose(),
				POINT_INFO.getcDownPanelClose(), false));
		// 等待石头图标
		List<PointColor> pocoList = new ArrayList<>();
		pocoList.add(new PointColor(POINT_INFO.getpSummonStone(), POINT_INFO.getcSummonStone(), true));
		GameUtil.waitUntilAllColor(pocoList, DELAY, Constant.FGOMonitor);
		// 切换左箭头
		Point p3 = new Point(85, 410);// 颜色：248;244;248 Color c = new Color(248, 244, 248);
		GameUtil.mouseMoveByPoint(p3);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 等待小手图标
		pocoList = new ArrayList<>();
		pocoList.add(new PointColor(POINT_INFO.getpHand(), POINT_INFO.getcHand(), true));
		GameUtil.waitUntilAllColor(pocoList, DELAY, Constant.FGOMonitor);
		try {
			// 点击确定按钮抽取
			Point p5 = new Point(677, 603);// 颜色：31;167;202
			GameUtil.mouseMoveByPoint(p5);
			GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
			Point p6 = new Point(886, 609);
			GameUtil.mouseMoveByPoint(p6);
			GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
		} catch (AppNeedRestartException e) {
			LOGGER.info("没有免费池，跳过！");
		}
		// 等待3秒
		r.delay(10 * DELAY);

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
			moveWindowToLeftTop();
			checkLoading();
			// 检测公告
			CommonMethods.open2GudaOrRestart();
			waitForHomePage();
			// 关闭游戏
			closeFgoByForce();
		} catch (AppNeedRestartException e) {
			closeFgoByForce();
			reNewRobot();
			signOneFGO(tip);
		}
	}
	/***
	 * 签到一个FGO
	 *
	 * @throws Exception 异常
	 */
	private void updateAndSignOneFGO(int index) throws Exception {
		try {
			// 打开窗口
			ProcessDealUtil.startLd(index);
			moveWindowToLeftTop();
			// 安装更新好的apk
			readyUpdateApk(index);
			// 检测loading
			checkLoading();
			// 检测公告
			CommonMethods.open2GudaOrRestart();
			waitForHomePage();
			// 关闭游戏
			closeFgoByForce();
		} catch (AppNeedRestartException e) {
			closeFgoByForce();
			reNewRobot();
			signOneFGO(index);
		}
	}
	private void moveWindowToLeftTop() throws Exception {
		// 移动窗口至左上角
		Point pLeftTop = POINT_INFO.getpLeftTop();
		Color cLeftTop = POINT_INFO.getcLeftTop();
		List<PointColor> pocoList = new ArrayList<>();
		pocoList.add(new PointColor(pLeftTop, cLeftTop, true));
		GameUtil.waitUntilAllColor(pocoList, DELAY, Constant.FGOMonitor);
		GameUtil.moveToLeftTop();
	}
	private void readyUpdateApk(int index) throws Exception {
		List<PointColor> pocoList = new ArrayList<>();
		pocoList = new ArrayList<>();
		pocoList.add(new PointColor(POINT_INFO.getpLdIsOpen(), POINT_INFO.getcLdIsOpen(), true, "install"));
		GameUtil.waitUntilOneColor(pocoList, Constant.FGOMonitor);
		ProcessDealUtil.installApp(index);
	}
	private void checkLoading() throws Exception {
		// 检测loading是否完毕
		Point pLoading = POINT_INFO.getpLoading();
		Color cLoading = POINT_INFO.getcLoading();

		Point pTransfer = POINT_INFO.getpTransfer();
		Color cTransfer = POINT_INFO.getcTransfer();

		List<PointColor> pcList = new ArrayList<>();
		pcList.add(new PointColor(pLoading, cLoading, POINT_INFO.getDeadPoint(), true));
		pcList.add(new PointColor(pTransfer, cTransfer, POINT_INFO.getDeadPoint(), true));
		List<PointColor> finishPCList = new ArrayList<>();
		finishPCList.add(new PointColor(pTransfer, cTransfer, POINT_INFO.getDeadPoint(), true));
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
		Point deadPoint = POINT_INFO.getDeadPoint();
		// 复位点
		Point pReset = POINT_INFO.getpReset();
		// 羁绊三角1
		Point pFetter01 = POINT_INFO.getpFetter01();
		Color cFetter01 = POINT_INFO.getcFetter01();
		// 羁绊三角2
		Point pFetter02 = POINT_INFO.getpFetter02();
		Color cFetter02 = POINT_INFO.getcFetter02();
		// 羁绊升级
		Point pFetterUp = POINT_INFO.getpFetterUp();
		Color cFetterUp = POINT_INFO.getcFetterUp();
		// 确认点
		Point pConfirmRd = POINT_INFO.getpConfirmRd();
		Color cConfirmRd = POINT_INFO.getcConfirmRd();
		//好友申请拒绝点
		Point pGetFriendNo = POINT_INFO.getpGetFriendNo();
		Color cGetFriendNo = POINT_INFO.getcGetFriendNo();
		// 咕哒子
		Point pGuda = POINT_INFO.getpGuda();
		Color cGuda = POINT_INFO.getcGuda();
		// 获取奖励动态坐标颜色
		Point pRewardAction = POINT_INFO.getpRewardAction();
		Color cRewardAction = POINT_INFO.getcRewardAction();

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
	protected void openWindow(int location) {
		ProcessDealUtil.startApp(location);
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
		ProcessDealUtil.killAllDnPlayer();
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
		Point p_notice_exit = POINT_INFO.getpNoticeExit();
		Color c_notice_exit = POINT_INFO.getcNoticeExit();
		// 公告×点
		Point p_notice_exit_dark = POINT_INFO.getpNoticeExitDark();
		Color c_notice_exit_dark = POINT_INFO.getcNoticeExitDark();
		// 盲点
		Point dead_point = POINT_INFO.getDeadPoint();
		// 咕哒子
		Point p_guda = POINT_INFO.getpGuda();
		Color c_guda = POINT_INFO.getcGuda();

		List<PointColor> pcList = new ArrayList<>();
		pcList.add(new PointColor(p_guda, c_guda, dead_point, true));
		pcList.add(new PointColor(POINT_INFO.getpBlueAttack(), POINT_INFO.getcBlueAttack(), dead_point, true));
		pcList.add(new PointColor(p_notice_exit, c_notice_exit, p_notice_exit, true));
		pcList.add(new PointColor(p_notice_exit_dark, c_notice_exit_dark, p_notice_exit_dark, true));
		List<PointColor> finishPCList = new ArrayList<>();
		finishPCList.add(new PointColor(p_guda, c_guda, dead_point, true));
		finishPCList.add(new PointColor(POINT_INFO.getpBlueAttack(), POINT_INFO.getcBlueAttack(), dead_point, true));

		AutoAct ac = new AutoAct(pcList, finishPCList) {
			@Override
			public void doSomeThing() {
			}
		};
		ac.autoAct();
	}

	public Map<String, List<CommonCard>> getWeakCommondCards(Comparator<CommonCard> comp){
		Point p_card_click = POINT_INFO.getpCardClick();
		Point p_card_color = POINT_INFO.getpCardColor();
		Point p_card_weak = POINT_INFO.getpCardWeak();
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

	public static void main(String[] args) {
		try {
			new Gudazi().getRewords();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
