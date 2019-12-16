package fgoScript.entity.guda;

import commons.util.GameUtil;
import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.CardComparator;
import fgoScript.entity.CommonCard;
import fgoScript.entity.PointColor;
import fgoScript.service.EventFactors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QpApGudazi extends AbstractApGudazi{
    private static final Logger LOGGER = LogManager.getLogger(QpApGudazi.class);
    private static final PointInfo POINT_INFO = PointInfo.getSpringBean();

    @Override
    public void intoAndSelect(int apNum, int acountNum) throws Exception{
        insertIntoQpRoom(apNum);
    }

    @Override
    public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
        fightAndBackForQp(rebootFlag);
    }

    @Override
	public Point getSuppotServant() {
		return POINT_INFO.getpServantFour();
	}

    @Override
    public void fightOverMethod() {
    }


    private void fightAndBackForQp(boolean rebootFlag) throws Exception {
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
            giveServantSkills(EventFactors.getQpPreSkills(DEFAULT_SKILL_COLORS));
            waitToAttack(null);
            // 平A
            attackBAAForEvent(true, hases);
            // 等待
            waitToAttack("1");
            eveValue = GameUtil.getColorEveValue(pointList);
        }
        // 第二回合
        // 2回合数字高亮点
        Point ps5 = new Point(906, 70);// 颜色：209;209;209 Color c = new Color(209, 209, 209);
        Point ps6 = new Point(900, 66);// 颜色：255;255;255 Color c = new Color(255, 255, 255);
        Point ps7 = new Point(899, 81);// 颜色：247;247;247 Color c = new Color(247, 247, 247);
        pointList = new ArrayList<>();
        pointList.add(ps5);
        pointList.add(ps6);
        pointList.add(ps7);
        waitToAttack(null);
        LOGGER.info("第二回合开始：");
        eveValue = GameUtil.getColorEveValue(pointList);

        boolean hasSelect = false;
        int count = 0;
        PointColor pc;
        while (eveValue > THRESHOLD&& battleRounds < MaxRounds) {
            battleRounds++;
            /*
              连续使用技能组
             */
            giveServantSkills(EventFactors.getQpPreSkills(DEFAULT_SKILL_COLORS));
            if (!hasSelect) {
                // 选择第2个怪物
                Point p_moster01 =POINT_INFO.getpMoster01();
                GameUtil.mouseMoveByPoint(p_moster01);
                GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
                hasSelect = true;
            }
            if (count==0) {
                //加回避
                giveClothSkills(EventFactors.getRound2ClothSkillto3());
            }
            pc = waitToAttack(null);
            if ("attack".equals(pc.getName())) {
                attackBAAForEvent(false, hases);
            }else {
                LOGGER.info("羁绊了，跳出来");
                break;
            }
            // 等待
            waitToAttack("3");
            eveValue = GameUtil.getColorEveValue(pointList);
            count++;
        }
        Point ps9 =  new Point(900, 66);// 颜色：243;243;243 Color c = new Color(243, 243, 243);
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
        LOGGER.info("第三回合开始：");
        eveValue = GameUtil.getColorEveValue(pointList);
        hasSelect= false;
        while (eveValue > THRESHOLD && battleRounds < MaxRounds) {
            battleRounds++;
            /*
              连续使用技能组
             */
            giveServantSkills(EventFactors.getQpPreSkills(DEFAULT_SKILL_COLORS));
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

    private void insertIntoQpRoom(int apNum) throws Exception {
    	GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestDown());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestTop());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 进入训练场
        // 周回进去
        Point p4 = POINT_INFO.getP_WEEK_ENTRANCE();
        GameUtil.mouseMoveByPoint(p4);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        
        GameUtil.delay(GameConstant.DELAY*5);
        
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestTop());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestDown());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 拖拽画面
        Point start = POINT_INFO.getpDailySliceStrat();
        Point end = POINT_INFO.getpDailySliceEnd();
        moveBySteps(start, end);

        // 点击日常
        Point p6 = POINT_INFO.getpDailyEntrance();
        GameUtil.mouseMoveByPoint(p6);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestDown());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.mouseMoveByPoint(POINT_INFO.getpScrollRestTop());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 拉滚动条至中间
        Point p7 = POINT_INFO.getpQpAll();
        GameUtil.mouseMoveByPoint(p7);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 点击ap本
        Point p8 = POINT_INFO.getQpRoom(apNum);
        GameUtil.mouseMoveByPoint(p8);
        GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
    }
    private boolean[] attackBAAForEvent(boolean goMainNP, boolean[] hases) throws Exception {
        Point pNp01 = POINT_INFO.getNpCheck();
        Point pNp02 = new Point((int)pNp01.getX()+ GameConstant.HEAD_SPACE, (int)pNp01.getY());
        Point pNp03 = new Point((int)pNp01.getX()+ GameConstant.HEAD_SPACE*2, (int)pNp01.getY());
        Color cNp01 = GameUtil.getScreenPixel(pNp01);
        Color cNp02 = GameUtil.getScreenPixel(pNp02);
        Color cNp03 = GameUtil.getScreenPixel(pNp03);
        int rgb01 = cNp01.getRGB();
        int rgb02 = cNp02.getRGB();
        int rgb03 = cNp03.getRGB();
        GameUtil.delay(GameConstant.DELAY);
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
        boolean hasNp03 = rgb03 != rgb03_Delay;
        LOGGER.info("有宝具吗？  " + (has));
        if ( has ) {
            if (hasNp03 && goMainNP) {
                /*
                  宝具技能组03
                 */
                giveServantSkills(EventFactors.getNPSkills03(DEFAULT_SKILL_COLORS));
            }
            // 等待
            waitToAttack(null);
            // 蓝色圆板选择
            blueAttackSelect();
            // 开始点击卡片
            Map<String, List<CommonCard>> scMap;
            if (hasNp01 && goMainNP) {
                scMap = getWeakCommondCards(CardComparator.getRgbComparotor());
            }else {
                scMap = getWeakCommondCards(CardComparator.getBgrComparotor());
            }
            Point np_np01 = POINT_INFO.getNpNp();
            Point np_np03 = new Point((int) np_np01.getX()+GameConstant.NP_SPACE*2,(int) np_np01.getY());
            if (hasNp03 && goMainNP) {
                // 宝具
                GameUtil.mouseMoveByPoint(np_np03);
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
    public static void main(String[] args) {
		LOGGER.trace("Trace Message!"); 
		LOGGER.debug("Debug Message!"); 
		LOGGER.info("Info Message!"); 
		LOGGER.warn("Warn Message!"); 
		LOGGER.error("Error Message!"); 
		LOGGER.fatal("Fatal Message!");
	}
}
