package fgoScript.entity.guda;

import commons.util.GameUtil;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.PointColor;
import fgoScript.entity.ServantSelect;
import fgoScript.exception.AppNeedRestartException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class TrainApGudazi extends AbstractApGudazi {
	private static final Logger LOGGER = LogManager.getLogger(TrainApGudazi.class);
	private static final PointInfo POINT_INFO = PointInfo.getSpringBean();

	@Override
	public void intoAndSelect(int apNum, int acountNum) throws Exception {
		insertIntoTrainingRoom(apNum);
	}

	@Override
	public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
		fightAndBack(apNum);
	}

	@Override
	public Point getSuppotServant() {
		return ServantSelect.getPoint();
	}

	@Override
	public void fightOverMethod() {
	}


	private void insertIntoTrainingRoom(int apNum) throws Exception {
		// 拉滚动条至最下上
		GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestDown());
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestTop());
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 进入训练场
		// 周回进
		Point p4 = POINT_INFO.getP_WEEK_ENTRANCE();
		GameUtil.mouseMoveByPoint(p4);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
		
		GameUtil.delay(GameConstant.DELAY*5);
		
		// 拉滚动条至最下
		Point p16 = POINT_INFO.getpScrollRestDown();
		GameUtil.mouseMoveByPoint(p16);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 拖拽画面
		Point start = POINT_INFO.getpDailySliceStrat();
		Point end = POINT_INFO.getpDailySliceEnd();
		moveBySteps(start, end);

		// 点击日常
		Point p6 = POINT_INFO.getpDailyEntrance();
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
		// 拉滚动条至最上
		GameUtil.mouseMoveByPoint(p16);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 拉滚动条至中间
		Point p7 = POINT_INFO.getpTrainAll();
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		// 点击ap本
		Point p8 = POINT_INFO.getTrainingRoom(apNum);
		GameUtil.mouseMoveByPoint(p8);
		GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
	}
	
	private void fightAndBack(int apNum) throws Exception {
		int THRESHOLD = GameConstant.THRESHOLD;
		// 1回合数字高亮点
		Point ps1 = new Point(903, 79);
		Point ps2 = new Point(903, 69);
		Point ps3 = new Point(904, 74);
		List<Point> pointList = new ArrayList<>();
		pointList.add(ps1);
		pointList.add(ps2);
		pointList.add(ps3);
		int eveValue;
		int battleRounds = 0;
		int MaxRounds = 20;
		// 第一回合
		LOGGER.info("准备战斗，等待。。。。");
		waitToAttack(null);
		LOGGER.info("第一回合开始：");
		eveValue = GameUtil.getColorEveValue(pointList);
		while (eveValue > THRESHOLD && battleRounds < MaxRounds) {
			battleRounds++;
			// 平A
			attackAAA();
			// 等待
			waitToAttack("1");
			eveValue = GameUtil.getColorEveValue(pointList);
		}
		// 第二回合
		// 如果是阿比就保护自己放2技能
		ServantSelect ss = new ServantSelect();
		if (GameConstant.CLASSIFY_ALL == ss.getRoomClassify()) {
			// 等待
			waitToAttack("2");
			Point p33 = new Point(838, 625);// 颜色：121;65;79 Color c = new Color(121, 65, 79);
			GameUtil.mouseMoveByPoint(p33);
			GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
		}
		// 2回合数字高亮点
		Point ps5 = new Point(906, 70);// 颜色：209;209;209 Color c = new Color(209, 209, 209);
		Point ps6 = new Point(900, 66);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		Point ps7 = new Point(899, 81);// 颜色：247;247;247 Color c = new Color(247, 247, 247);

		pointList = new ArrayList<>();
		pointList.add(ps5);
		pointList.add(ps6);
		pointList.add(ps7);
		int mCount = 0;
		waitToAttack(null);
		LOGGER.info("第二回合开始：");
		eveValue = GameUtil.getColorEveValue(pointList);
		while (eveValue > THRESHOLD&& battleRounds < MaxRounds) {
			battleRounds++;
			// 选择第一个怪物
			if (mCount == 0) {
				Point pmm = new Point(124, 93);// 颜色：30;24;18 Color c = new Color(30, 24, 18);
				GameUtil.mouseMoveByPoint(pmm);
				GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
			}
			// 加血
			if (mCount == 1 && apNum > 30) {
				battleSkill1();
				waitToAttack("2");
			}
			// 平A
			attackAAA();
			// 等待
			waitToAttack("2");
			eveValue = GameUtil.getColorEveValue(pointList);
			mCount++;
		}

		// 3回合数字高亮点
		Point ps9  = new Point(900, 66);// 颜色：243;243;243 Color c = new Color(243, 243, 243);
		Point ps10 = new Point(903, 74);// 颜色：221;221;221 Color c = new Color(221, 221, 221);
		Point ps11 = new Point(907, 79);// 颜色：226;226;226 Color c = new Color(226, 226, 226);
		Point ps14 = new Point(897, 82);// 颜色：255;255;255 Color c = new Color(255, 255, 255);

		pointList = new ArrayList<>();
		pointList.add(ps9);
		pointList.add(ps10);
		pointList.add(ps11);
		pointList.add(ps14);
		// 第三回合
		// 等待
		waitToAttack(null);
		int bCount = 0;
		LOGGER.info("第三回合开始：");
		eveValue = GameUtil.getColorEveValue(pointList);
		boolean isReDo = false;
		while (eveValue > THRESHOLD&& battleRounds < MaxRounds) {
			battleRounds++;
			// 宝具平A
			isReDo = attackBAA(isReDo, apNum);
			// 等待
			waitToAttack("3");
			// 如果是羁绊结算就停止循环
			if (checkFinish()) {
				LOGGER.info("羁绊了，跳出来");
				break;
			}
			eveValue = GameUtil.getColorEveValue(pointList);
			bCount++;
		}
	}

	/**
	 * 平a攻击
	 * @throws AppNeedRestartException
	 */
	private void attackAAA() throws AppNeedRestartException {
		// 蓝色圆板选择
        blueAttackSelect();
        // 开始点击卡片
		List<Point> pList = getCommondCards();
		Point pTemp;
		// 大小大的攻击顺序
		pTemp = pList.get(4);
		GameUtil.mouseMoveByPoint(pTemp);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		pTemp = pList.get(0);
		GameUtil.mouseMoveByPoint(pTemp);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		pTemp = pList.get(3);
		GameUtil.mouseMoveByPoint(pTemp);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		checkExitCardSelect();
	}
	private List<Point> getCommondCards() {
		GameUtil.delay(GameConstant.DELAY*2);
		List<Point> p_supports = POINT_INFO.getpSupports();
		Color color;
		Point point;
		PointColor pc;
		List<PointColor> pcList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			point = p_supports.get(i);
			color = GameUtil.getScreenPixel(point);
			pc = new PointColor(point, color, true);
			pcList.add(pc);
		}
		pcList.sort((pc1, pc2) -> pc1.getColor().getRed() + pc1.getColor().getBlue() + pc1.getColor().getGreen()
				- pc2.getColor().getRed() - pc2.getColor().getBlue() - pc2.getColor().getGreen());
		List<Point> pList = new ArrayList<>();
		Point pTemp;
		int size = pcList.size();
		for (int i = 0; i < size; i++) {
			PointColor pointColor = pcList.get(i);
			pTemp = pointColor.getPoint();
			pTemp = new Point((int) pTemp.getX() - 80, (int) pTemp.getY() + 50);
			pList.add(pTemp);
		}
		return pList;
	}
	private void battleSkill1() {
		Point p6 = new Point(906, 475);// 颜色：2;3;3 Color c = new Color(2, 3, 3);
		Point p7 = new Point(675, 474);// 颜色：254;238;211 Color c = new Color(254, 238, 211);
		// 战斗服选择
		Point p5 = new Point(1229, 359);// 颜色：24;60;107 Color c = new Color(24, 60, 107);
		GameUtil.mouseMoveByPoint(p5);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择技能1
		Point p8 = new Point(942, 362);// 颜色：157;224;193 Color c = new Color(157, 224, 193);
		GameUtil.mouseMoveByPoint(p8);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择人物
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
	}
	/**
	 * 宝具平a攻击
	 * 
	 * @throws Exception
	 */
	private boolean attackBAA(boolean isReDo, int apNum) throws Exception {
		Point pNp = new Point(856, 720);

		Color cNp = GameUtil.getScreenPixel(pNp);
		int c1 = cNp.getRGB();
		GameUtil.delay(GameConstant.DELAY);
		Color cNp2 = GameUtil.getScreenPixel(pNp);
		int c2 = cNp2.getRGB();

		LOGGER.info("有宝具吗？  " + (c1 != c2));
		if (c1 != c2) {
			// 等待
			if (!isReDo && apNum > 20) {
				// 等待
				waitToAttack(null);
				// 战斗服技能
				battleSkill23();
				// 等待
				waitToAttack(null);
				// 技能1，2，3
				servantSkill();
				isReDo = true;
			}

			// 等待
			waitToAttack(null);
			// 蓝色圆板选择
            blueAttackSelect();
            // 开始点击卡片
			// 宝具
			Point p6 = new Point(912, 256);// 颜色：72;72;105 Color c = new Color(72, 72, 105);
			GameUtil.mouseMoveByPoint(p6);
			GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
			// 防止宝具冻结
			GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
			GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

			List<Point> pList = getCommondCards();
			Point pTemp;
			// 大大的攻击顺序
			pTemp = pList.get(4);
			GameUtil.mouseMoveByPoint(pTemp);
			GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
			pTemp = pList.get(3);
			GameUtil.mouseMoveByPoint(pTemp);
			GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
			pTemp = pList.get(2);
			GameUtil.mouseMoveByPoint(pTemp);
			GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		} else {
			attackAAAC();
		}
		checkExitCardSelect();
		return isReDo;
	}
	private void battleSkill23() throws Exception {
		Point p6 = new Point(906, 475);// 颜色：2;3;3 Color c = new Color(2, 3, 3);
		Point p7 = new Point(675, 474);// 颜色：254;238;211 Color c = new Color(254, 238, 211);
		// 战斗服选择
		Point p5 = new Point(1229, 359);// 颜色：24;60;107 Color c = new Color(24, 60, 107);
		GameUtil.mouseMoveByPoint(p5);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择技能2
		Point p8 = new Point(1029, 357);// 颜色：252;249;107 Color c = new Color(252, 249, 107);
		GameUtil.mouseMoveByPoint(p8);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择人物
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		// 等待
		waitToAttack(null);
		// 战斗服选择
		GameUtil.mouseMoveByPoint(p5);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择技能3
		Point p9 = new Point(1115, 360);// 颜色：69;71;111 Color c = new Color(69, 71, 111);
		GameUtil.mouseMoveByPoint(p9);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择人物
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
	}
	private void servantSkill() throws Exception {
		Point p2 = new Point(747, 632);// 颜色：91;91;91 Color c = new Color(91, 91, 91);
		GameUtil.mouseMoveByPoint(p2);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		// 选择人物
		Point p6 = new Point(906, 475);// 颜色：2;3;3 Color c = new Color(2, 3, 3);
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		Point p7 = new Point(675, 474);// 颜色：254;238;211 Color c = new Color(254, 238, 211);
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 等待
		waitToAttack(null);
		Point p3 = new Point(838, 625);// 颜色：121;65;79 Color c = new Color(121, 65, 79);
		GameUtil.mouseMoveByPoint(p3);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		// 选择人物
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 等待
		waitToAttack(null);
		Point p4 = new Point(930, 627);
		GameUtil.mouseMoveByPoint(p4);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		// 选择人物
		GameUtil.mouseMoveByPoint(p6);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		GameUtil.mouseMoveByPoint(p7);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

		GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
	}
	/**
	 * 平a攻击
	 * @throws AppNeedRestartException
	 */
	private void attackAAAC() throws AppNeedRestartException {
		// 蓝色圆板选择
        blueAttackSelect();
        // 开始点击卡片
		List<Point> pList = getCommondCards();
		Point pTemp;
		// 大小大的攻击顺序
		pTemp = pList.get(4);
		GameUtil.mouseMoveByPoint(pTemp);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		pTemp = pList.get(3);
		GameUtil.mouseMoveByPoint(pTemp);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		pTemp = pList.get(2);
		GameUtil.mouseMoveByPoint(pTemp);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		checkExitCardSelect();
	}

}
