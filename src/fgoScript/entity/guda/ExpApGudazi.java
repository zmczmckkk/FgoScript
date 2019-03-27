package fgoScript.entity.guda;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.CardComparator;
import fgoScript.entity.CommonCard;
import fgoScript.entity.PointColor;
import fgoScript.service.EventFactors;
import fgoScript.util.GameUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpApGudazi extends AbstractApGudazi {
    private static final Logger LOGGER = LogManager.getLogger(ExpApGudazi.class);
	@Override
	public void intoAndSelect(int apNum) throws Exception {
		insertIntoExpRoom(apNum);

	}

	@Override
	public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
		fightAndBackForExp(rebootFlag);

	}

	@Override
	public Point getSuppotServant() {
		return PointInfo.P_SERVANT_CASTER;
	}
	
	
	
	
	
	
	public void insertIntoExpRoom(int apNum) throws Exception {
    	GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 进入训练场
        // 周回进去
        GameUtil.mouseMoveByPoint(PointInfo.getP_WEEK_ENTRANCE());
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        
        GameUtil.delay(GameConstant.DELAY*5);
        
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 拖拽画面
        Point start = PointInfo.P_DAILY_SLICE_STRAT;
        Point end = PointInfo.P_DAILY_SLICE_END;
        moveBySteps(start, end);

        // 点击日常
        Point p6 = PointInfo.P_DAILY_ENTRANCE;
        GameUtil.mouseMoveByPoint(p6);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 拉滚动条至中间
        Point p7 = PointInfo.P_EXP_ALL;
        GameUtil.mouseMoveByPoint(p7);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 点击ap本
        Point p8 = PointInfo.getExpRoom(apNum);
        GameUtil.mouseMoveByPoint(p8);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
    }
	private void fightAndBackForExp(boolean rebootFlag) throws Exception {
		int THRESHOLD = GameConstant.THRESHOLD;
		// 1回合数字高亮点
		Point ps1 = new Point(909, 71);// 颜色：200;200;200 Color c = new Color(200, 200, 200);
		Point ps2 = new Point(915, 68);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		Point ps3 = new Point(914, 74);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		Point ps4 = new Point(914, 79);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		List<Point> pointList = new ArrayList<>();
		pointList.add(ps1);
		pointList.add(ps2);
		pointList.add(ps3);
		pointList.add(ps4);
		int eveValue;
		int battleRounds = 0;
		int MaxRounds = 20;
		// 第一回合
		LOGGER.info("准备战斗，等待。。。。");
		waitToAttack(null);
		LOGGER.info("第一回合开始：");
		boolean[] hases = {false,false};
		eveValue = GameUtil.getColorEveValue(pointList);
		if (rebootFlag) {
			DEFAULT_SKILL_COLORS = EventFactors.getDefaultSkillColorsFromFile();
		}else {
			DEFAULT_SKILL_COLORS = EventFactors.getSkillColors();
			EventFactors.writeDefaultSkillColors(DEFAULT_SKILL_COLORS);
		}
		while (eveValue > THRESHOLD && battleRounds < MaxRounds) {
			battleRounds++;
			/*
			  连续使用技能组
			 */
			giveServantSkills(EventFactors.getExpPreSkills(DEFAULT_SKILL_COLORS));
			waitToAttack(null);
			// 副宝具平a
			attackBAAForEvent(false, hases);
			// 等待
			waitToAttack("1");
			eveValue = GameUtil.getColorEveValue(pointList);
		}
		// 第二回合
		// 2回合数字高亮点
		Point ps5 = new Point(909, 69);// 颜色：209;209;209 Color c = new Color(209, 209, 209);
		Point ps6 = new Point(917, 70);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		Point ps7 = new Point(916, 76);// 颜色：247;247;247 Color c = new Color(247, 247, 247);
		Point ps8 = new Point(909, 84);// 颜色：248;248;248 Color c = new Color(248, 248, 248);
		Point ps13 = new Point(915, 78);// 颜色：252;252;252 Color c = new Color(252, 252, 252);

		pointList = new ArrayList<>();
		pointList.add(ps5);
		pointList.add(ps6);
		pointList.add(ps7);
		pointList.add(ps8);
		pointList.add(ps13);
		waitToAttack(null);
		LOGGER.info("第二回合开始：");
		eveValue = GameUtil.getColorEveValue(pointList);
		
		PointColor pc;
		while (eveValue > THRESHOLD&& battleRounds < MaxRounds) {
			battleRounds++;
			/*
			  连续使用技能组
			 */
			giveServantSkills(EventFactors.getExpPreSkills(DEFAULT_SKILL_COLORS));
			pc = waitToAttack(null);
			if ("attack".equals(pc.getName())) {
				attackBAAForEvent(true, hases);
			}else {
				LOGGER.info("羁绊了，跳出来");
				break;
			}
			// 等待
			waitToAttack("3");
			eveValue = GameUtil.getColorEveValue(pointList);
		}

		// 3回合数字高亮点
		Point ps21 = new Point(917, 69);// 颜色：243;243;243 Color c = new Color(243, 243, 243);
		Point ps22 = new Point(909, 69);// 颜色：221;221;221 Color c = new Color(221, 221, 221);
		Point ps23 = new Point(908, 84);// 颜色：226;226;226 Color c = new Color(226, 226, 226);
		Point ps24 = new Point(918, 80);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		
		pointList = new ArrayList<>();
		pointList.add(ps21);
		pointList.add(ps22);
		pointList.add(ps23);
		pointList.add(ps24);
		// 第三回合
		// 等待
		waitToAttack(null);
		LOGGER.info("第三回合开始：");
		eveValue = GameUtil.getColorEveValue(pointList);
		while (eveValue > THRESHOLD && battleRounds < MaxRounds) {
			battleRounds++;
			/*
			  连续使用技能组
			 */
			giveServantSkills(EventFactors.getExpPreSkills(DEFAULT_SKILL_COLORS));
			// 宝具平A
			pc = waitToAttack("3");
			if ("attack".equals(pc.getName())) {
				attackBAAForEvent(true, hases);
			}else {
				LOGGER.info("羁绊了，跳出来");
				break;
			}
			// 等待
			waitToAttack("3");
			eveValue = GameUtil.getColorEveValue(pointList);
		}
	
	}
	private boolean[] attackBAAForEvent(boolean goMainNP, boolean[] hases) throws Exception {
		Point pNp01 = PointInfo.NP_CHECK;
		Point pNp02 = new Point((int)pNp01.getX()+ GameConstant.HEAD_SPACE, (int)pNp01.getY());
		Point pNp03 = new Point((int)pNp01.getX()+ GameConstant.HEAD_SPACE*2, (int)pNp01.getY());
		Color cNp01 = GameUtil.getScreenPixel(pNp01);
		Color cNp02 = GameUtil.getScreenPixel(pNp02);
		Color cNp03 = GameUtil.getScreenPixel(pNp03);
		int rgb01 = cNp01.getRGB();
		int rgb02 = cNp02.getRGB();
		int rgb03 = cNp03.getRGB();
		Color cNp01_Delay = GameUtil.getScreenPixel(pNp01);
		Color cNp02_Delay = GameUtil.getScreenPixel(pNp02);
		Color cNp03_Delay = GameUtil.getScreenPixel(pNp03);
		int rgb01_Delay = cNp01_Delay.getRGB();
		int rgb02_Delay = cNp02_Delay.getRGB();
		int rgb03_Delay = cNp03_Delay.getRGB();
		boolean has = rgb01 != rgb01_Delay 
				|| rgb02 != rgb02_Delay
				|| rgb03 != rgb03_Delay;
		int npCount = 0;
		boolean hasNp01 = rgb01 != rgb01_Delay;
		boolean hasNp02 = rgb02 != rgb02_Delay;
		boolean hasNp03 = rgb03 != rgb03_Delay;
		LOGGER.info("有宝具吗？  " + (has));
		if ( has ) {
			// 等待
			waitToAttack(null);
			// 蓝色圆板选择
            blueAttackSelect();
            // 开始点击卡片
			Map<String, List<CommonCard>> scMap;
			if (hasNp01 && goMainNP) {
				scMap = getWeakCommondCards(CardComparator.getRbgComparotor());
			}else {
				scMap = getWeakCommondCards(CardComparator.getBgrComparotor());
			}
			Point np_np01 = PointInfo.NP_NP;
			Point np_np02 = new Point((int) np_np01.getX()+GameConstant.NP_SPACE,(int) np_np01.getY());
			Point np_np03 = new Point((int) np_np01.getX()+GameConstant.NP_SPACE*2,(int) np_np01.getY());
			if (hasNp03) {
				// 宝具
				GameUtil.mouseMoveByPoint(np_np03);
				GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
				npCount++;
			}
			if (hasNp02) {
				// 宝具
				GameUtil.mouseMoveByPoint(np_np02);
				GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
				npCount++;
			}
			if (hasNp01 && goMainNP) {
				// 宝具
				GameUtil.mouseMoveByPoint(np_np01);
				GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
				npCount++;
			}
			List<CommonCard> trueList = scMap.get("trueList");
			List<CommonCard> falseList = scMap.get("falseList");
			int size = trueList.size();
			for (int j = 0; j < size; j++) {
				GameUtil.mouseMoveByPoint(trueList.get(j).getpLoc());
				GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
			}
			if (size < (3-npCount)) {
				int num = 2-trueList.size()+1;
				for (int j = 0; j < num; j++) {
					GameUtil.mouseMoveByPoint(falseList.get(j).getpLoc());
					GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
				}
			}
		} else {
			attackNPAAA();
		}
		checkExitCardSelect();
		return hases;
	}
}
