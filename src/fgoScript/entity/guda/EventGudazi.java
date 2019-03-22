package fgoScript.entity.guda;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.CardComparator;
import fgoScript.entity.CommonCard;
import fgoScript.entity.PointColor;
import fgoScript.service.EventFactors;
import fgoScript.util.GameUtil;

public class EventGudazi extends AbstractApGudazi {
	private static final Logger LOGGER = LogManager.getLogger(EventGudazi.class);
	@Override
	public void intoAndSelect(int apNum) throws Exception {
		insertIntoEventRoom(apNum);
	}
	@Override
	public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
		fightAndBackForEvent(rebootFlag);
	}
	@Override
	public Point getSuppotServant() {
		return PointInfo.P_SERVANT_MIX;
	}
	private void insertIntoEventRoom(int apNum) throws Exception {
    	GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 进入活动本
        Point p4 = PointInfo.P_WEEK_ENTRANCE_01;
        GameUtil.mouseMoveByPoint(p4);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        
        GameUtil.delay(GameConstant.DELAY*5);
        
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_TOP);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(PointInfo.P_SCROLL_REST_DOWN);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        
        
        
        // 拉滚动条至目标本
        Point p7 = PointInfo.P_EVENT_SCROLL_LOCATE01;
        GameUtil.mouseMoveByPoint(p7);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 点击ap本
        Point p8 = PointInfo.P_EVENT_BATTLE_LOCATE01;
        GameUtil.mouseMoveByPoint(p8);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
    }
	
	private void fightAndBackForEvent(boolean rebootFlag) throws Exception {
		// 1回合数字高亮点
		Point ps1 = new Point(909, 71);// 颜色：200;200;200 Color c = new Color(200, 200, 200);
		Point ps2 = new Point(915, 68);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		Point ps3 = new Point(914, 74);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		Point ps4 = new Point(914, 79);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
		List<Point> pointList = new ArrayList<Point>();
		pointList.add(ps1);
		pointList.add(ps2);
		pointList.add(ps3);
		pointList.add(ps4);
		int eveValue = 0;
		int battleRounds = 0;
		int MaxRounds = 20;
		int rounds = EventFactors.battleRounds;
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
		while (eveValue > GameConstant.THRESHOLD && battleRounds < MaxRounds) {
			battleRounds++;
			/**
			 * 连续使用技能组
			 */
			giveServantSkills(EventFactors.getPreSkills(DEFAULT_SKILL_COLORS));
			waitToAttack(null);
			// 平A
			if (EventFactors.ifNP) {
				// 副宝具平a
				hases = attackBAAForEvent(false, hases);
			}else {
				// 平a
				attackNPAAA();
			}
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

		pointList = new ArrayList<Point>();
		pointList.add(ps5);
		pointList.add(ps6);
		pointList.add(ps7);
		pointList.add(ps8);
		pointList.add(ps13);
		waitToAttack(null);
		LOGGER.info("第二回合开始：");
		eveValue = GameUtil.getColorEveValue(pointList);
		
		boolean hasSelect = false;
		int count = 0;
		PointColor pc = null;
		boolean ifSecondNP = EventFactors.ifSecondNP;
		while (eveValue > GameConstant.THRESHOLD&& battleRounds < MaxRounds) {
			battleRounds++;
			/**
			 * 连续使用技能组
			 */
			giveServantSkills(EventFactors.getPreSkills(DEFAULT_SKILL_COLORS));
			if (!hasSelect) {
				// 选择第2个怪物
				Point p_moster01 =PointInfo.P_MOSTER01;
				GameUtil.mouseMoveByPoint(p_moster01);
				GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
				hasSelect = true;
			}
			if (count==0) {
				//加回避
				giveClothSkills(EventFactors.getRound2ClothSkill());
			}
			pc = waitToAttack(null);
			if ("attack".equals(pc.getName())) {
				if (rounds==3 ) {
					if (ifSecondNP) {
						hases = attackBAAForEvent(true, hases);
					}else {
						hases = attackBAAForEvent(false, hases);
					}
				}else {
					hases = attackBAAForEvent(true, hases);
				}
			}else {
				LOGGER.info("羁绊了，跳出来");
				break;
			}
			// 等待
			waitToAttack("3");
			eveValue = GameUtil.getColorEveValue(pointList);
			count++;
		}
		if (rounds==3) {
			// 3回合数字高亮点
			Point ps9 = new Point(917, 69);// 颜色：243;243;243 Color c = new Color(243, 243, 243);
			Point ps10 = new Point(909, 69);// 颜色：221;221;221 Color c = new Color(221, 221, 221);
			Point ps11 = new Point(908, 84);// 颜色：226;226;226 Color c = new Color(226, 226, 226);
			Point ps14 = new Point(918, 80);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
			
			pointList = new ArrayList<Point>();
			pointList.add(ps9);
			pointList.add(ps10);
			pointList.add(ps11);
			pointList.add(ps14);
			// 第三回合
			// 等待
			waitToAttack(null);
			LOGGER.info("第三回合开始：");
			eveValue = GameUtil.getColorEveValue(pointList);
			hasSelect= false;
			while (eveValue > GameConstant.THRESHOLD && battleRounds < MaxRounds) {
				battleRounds++;
				/**
				 * 连续使用技能组
				 */
				giveServantSkills(EventFactors.getPreSkills(DEFAULT_SKILL_COLORS));
				if (!hasSelect) {
					// 选择第个怪物
					Point monstor =EventFactors.getMonsterPoint();
					if (monstor!=null) {
						GameUtil.mouseMoveByPoint(monstor);
						GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
						waitToAttack(null);
						hasSelect = true;
					}
				}
				// 宝具平A
				pc = waitToAttack("3");
				if ("attack".equals(pc.getName())) {
					hases = attackBAAForEvent(true, hases);
				}else {
					LOGGER.info("羁绊了，跳出来");
					break;
				}
				// 等待
				waitToAttack("3");
				eveValue = GameUtil.getColorEveValue(pointList);
			}
		}
	}
	/**
	 * 宝具平a攻击
	 * 
	 * @throws Exception
	 */
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
			if (hasNp01) {
				if (hases[0] == false && goMainNP) {
					// 战斗服技能
					battleSkillForP01();
					hases[0] = true;
					// 等待
					waitToAttack(null);
					/**
					 * 宝具技能组01
					 */
					giveServantSkills(EventFactors.getNPSkills01(DEFAULT_SKILL_COLORS));
				}
			}
			if (hasNp02) {
				/**
				 * 宝具技能组02
				 */
				giveServantSkills(EventFactors.getNPSkills02(DEFAULT_SKILL_COLORS));
			}	
			// 等待
			waitToAttack(null);
			// 蓝色圆板选择
            blueAttackSelect();
            // 开始点击卡片
			Map<String, List<CommonCard>> scMap = null;
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
	private void battleSkillForP01() throws Exception {
		// 战斗服选择
		Point p5 = new Point(1229, 359);// 颜色：24;60;107 Color c = new Color(24, 60, 107);
		GameUtil.mouseMoveByPoint(p5);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择技能2
		Point p8 = new Point(1029, 357);// 颜色：252;249;107 Color c = new Color(252, 249, 107);
		GameUtil.mouseMoveByPoint(p8);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		// 选择人物
		GameUtil.mouseMoveByPoint(PointInfo.SKILL_PERSON_01);
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
		GameUtil.mouseMoveByPoint(PointInfo.SKILL_PERSON_01);
		GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
	}
}
