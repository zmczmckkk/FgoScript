package fgoScript.entity.guda;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import commons.util.MySpringUtil;
import fgoScript.entity.*;
import fgoScript.service.CommonMethods;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.exception.FgoNeedNextException;
import fgoScript.exception.FgoNeedRestartException;
import fgoScript.exception.FgoNeedStopException;
import fgoScript.service.AutoAct;
import fgoScript.service.EventFactors;
import commons.util.ProcessDealUtil;
import commons.util.GameUtil;
import commons.util.PropertiesUtil;

@SuppressWarnings("ALL")
public abstract class AbstractApGudazi implements InterfaceApGudazi{
    private static final Logger LOGGER = LogManager.getLogger(AbstractApGudazi.class);
    private static final PointInfo POINT_INFO = (PointInfo) MySpringUtil.getApplicationContext().getBean("pointInfo");
    private String PREFIX = GameUtil.getPreFix();
    private int retunTimes = 0;
    protected Color[][] DEFAULT_SKILL_COLORS;

    private int[] accountArray;
    private int[] apArray;
    @Override
    public void setAccountArray(int[] accountArray) {
        this.accountArray = accountArray;
    }

    @Override
    public void setApArray(int[] apArray) {
        this.apArray = apArray;
    }

    /**
     * 入口json文件路径
     */
    public String getSpecialGatesFilePath(){
        return null;
    }

    /**
     * @param accountArray
     * @param apArray
     * @throws Exception
     */
    @Override
    public void startAllFgo() throws Exception {
        int accountNum;
        int size = accountArray.length;
        boolean ifClose = true;
        for (int i = 0; i < size; i++) {
            accountNum = accountArray[i];
            // 刷训练本
            try {
                startOneFgo(accountNum, apArray);
                // 关闭所有相关应用
                ProcessDealUtil.killAllTianTian();
            } catch (FgoNeedNextException e) {
                LOGGER.info(e.getMessage());
                GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + accountNum + "_体力不足页面.");
                continue;
            } catch (FgoNeedStopException e) {
            	ifClose = false;
            	throw e;
			}finally {
				if (ifClose) {
					ProcessDealUtil.killAllTianTian();
				}
			}

        }

    }
    /**
     *
     * @param accountNum 账号数字
     * @param apArray AP数组
     */
    private void startOneFgo(int accountNum, int[] apArray) throws Exception {
        int len = apArray.length;
        int apNum;//ap数字
        int appleCost = 0;
        String value = "";
        String key  = "";
        int count = 0;
        for (int i = 0; i < len; i++) {
        	apNum = apArray[i];
        	key ="hasDo" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	value = accountNum+"ap"+apNum;
        	String hasDo = PropertiesUtil.getValueFromHasDoFile(key);
        	if (len < 5 || i>3 || StringUtils.isBlank(hasDo) || !hasDo.contains(value)) {
        		 // 开始战斗
                LOGGER.info("账号" + accountNum + ",ap场合：" + apNum + " AP 开始");
                startFight(accountNum, apNum, false, count, len, appleCost);
        		 //写入已经完成的 内容
                Map<String, String> map = new HashMap<>();
                if (!hasDo.contains(value)) {
                	map.put(key, StringUtils.isBlank(hasDo)?value:hasDo+value);
                	PropertiesUtil.setValueForHasDo(map);
				}
                // 结束以后需要执行的方法
                fightOverMethod();
                count++;
			}else {
				continue;
			}

        }
        GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "账号" + accountNum + "_结束战斗首页.");
    }
    private void startFight(int accountNum, int apNum, boolean reStart, int count, int apLen, int appleCost) throws Exception {
        boolean continueGo = true;
        boolean rebootFlag = false;
        try {
        	if (isTopage()) {
				rebootFlag = false;
			}else {
				if (count == 0 || reStart == true) {
					// 打开账号
					ProcessDealUtil.startFgo(accountNum);
					// 检测loading
					move2WinAndTransferPage(accountNum);
				}
				// 检测公告
				LOGGER.info("开始检测是否重开。。。");
				PointColor pc = CommonMethods.open2GudaOrRestart();
				rebootFlag = pc.getColor().getRGB() == POINT_INFO.getcButtonRestartYes().getRGB();
			}
            if (rebootFlag) {
                LOGGER.info("重开战斗");
            } else {
                waitForHomePage();
                // 选本选人
                LOGGER.info("选本选人");
                intoAndSelect(apNum, accountNum);
                EventFactors.supportServant = getSuppotServant();
                selectRoomPressFightForQp(accountNum, appleCost, apNum);
                waitToAttack(null);
            }
        } catch (FgoNeedRestartException e) {
            LOGGER.info("进入房间异常！");
            LOGGER.info(e.getMessage());
            ProcessDealUtil.killAllTianTian();
            startFight(accountNum, apNum, true, count, apLen, appleCost);
            continueGo = false;
        }
        if (continueGo) {
            try {
                // 偏好设置
                if (count == 0 && GameConstant.IF_SET_PRE && !rebootFlag) {
                    preferenceSet();
                }
                // 战斗并且返回
                fightAndStop(rebootFlag, apNum);
            } catch (FgoNeedRestartException e) {
                LOGGER.info("战斗异常！");
                LOGGER.info(e.getMessage());
                ProcessDealUtil.killAllTianTian();
                startFight(accountNum, apNum, true, count, apLen, appleCost);
                continueGo = false;
            }
        }
        if (continueGo) {
            try {
                // 结算材料羁绊，处理方块，返回开始界面
                startBalance(accountNum, apNum, count);
                // 回到登录页面
                if (count != apLen - 1) {
                    returnTopPage();
                    LOGGER.info("回到了首页:" + count);
                } else {
                    LOGGER.info("最后一页：" + count);
                }
            } catch (FgoNeedRestartException e) {
                LOGGER.info("结算，返回异常！");
                LOGGER.info(e.getMessage());
                ProcessDealUtil.killAllTianTian();
                startFight(accountNum, apNum, true, count, apLen, appleCost);
            }
        }
    }
    private void selectRoomPressFightForQp(int acountNum, int appleCost, int apNum) throws Exception {
        List<PointColor> pocoList = new ArrayList<PointColor>();
        // 选人界面点
        Point p_wait = new Point(1235, 298);// 颜色：239;186;99
        Color c_wait = new Color(239, 186, 99);

        // 无苹果点
 		Point p_apple_need01 = POINT_INFO.getpAppleNeed01();
 		Color c_apple_need01 = POINT_INFO.getcAppleNeed01();
 		Point p_apple_need02 = POINT_INFO.getpAppleNeed02();
 		Color c_apple_need02 = POINT_INFO.getcAppleNeed02();

        List<PointColor> pcList = new ArrayList<PointColor>();
        pcList.add(new PointColor(p_wait, c_wait, null, true));
        pcList.add(new PointColor(p_apple_need01, c_apple_need01, null, true));
		pcList.add(new PointColor(p_apple_need02, c_apple_need02, null, true));
        List<PointColor> finishPCList = new ArrayList<PointColor>();
        finishPCList.add(new PointColor(p_wait, c_wait, null, true));
        AutoAct ac = new AutoAct(pcList, finishPCList) {
            @Override
            public void doSomeThing() throws Exception {
                Color cWait = this.getPcWait().getColor();
                if (GameUtil.likeEqualColor(c_apple_need01, cWait) ||
						GameUtil.likeEqualColor(c_apple_need02, cWait)) {
                    if (appleCost == GameConstant.APPLE_COUNT) {
                        LOGGER.info("已达到苹果消耗量，停止更新苹果。");
                        throw new FgoNeedNextException();
                    }else {
                        GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + acountNum + "_使用第"+(appleCost+1)+"个苹果.");
                        GameUtil.mouseMoveByPoint(POINT_INFO.getpAppleNeed01());
                        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
                        GameUtil.mouseMoveByPoint(POINT_INFO.getpConnectYes());
                        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
                        GameUtil.delay(GameConstant.DELAY*5);
                        LOGGER.info("ap不足，更新苹果。");
                    }
                }
            }
        };
        //监控短时间超时线程
  		new Thread(new AbstractWaitForOutTime() {
  			@Override
  			public void beforeDo() {
  				GameUtil.setCHECK_COUNT(0);
  			}
  			@Override
  			public void afterDo() {
  				GameUtil.setGO_FLAG(false);
  			}
  		}).start();
  		boolean flag = ac.autoAct();
  		if (!flag) {
  			if (++retunTimes == 2) {
  				retunTimes = 0;
  				throw new FgoNeedRestartException();
			}
  			returnTopPage();
  			waitForHomePage();
  			intoAndSelect(apNum, acountNum);
  			selectRoomPressFightForQp(acountNum, appleCost, apNum);
  		}else {
  			// 等待进入选人界面
  			pocoList = new ArrayList<PointColor>();
  			pocoList.add(new PointColor(p_wait, c_wait, true));
  			GameUtil.waitUntilAllColor(pocoList, GameConstant.DELAY);
  			// 选职介
  	        GameUtil.mouseMoveByPoint(EventFactors.supportServant);
  	        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
  			// 选人
  			Point p10 = new Point(300, 319);// 颜色：255;255;219 Color c = new Color(255, 255, 219);
  			GameUtil.mouseMoveByPoint(p10);
  			GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);

  			// 等待进入队伍配置界面
  			Point p12 = new Point(427, 731);// 颜色：210;0;58
  			Color c12 = new Color(210, 0, 58);
  			pocoList = new ArrayList<PointColor>();
  			pocoList.add(new PointColor(p12, c12, true));
  			GameUtil.waitUntilAllColor(pocoList, GameConstant.DELAY);
  			// 点击开始战斗按钮
  			Point p18 = new Point(1256, 731);// 颜色：41;45;90 开始战斗按钮
  			GameUtil.mouseMoveByPoint(p18);
  			GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
  			GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
  		}
    }

    /**
     *
     * @param countNum 账号数字
     * @throws Exception
     */
    private void move2WinAndTransferPage(int countNum) throws Exception {
        // 检测异动前的FGO游戏图标
        Point p_left_top = POINT_INFO.getpLeftTop();
        Color c_left_top = POINT_INFO.getcLeftTop();
        java.util.List<PointColor> pocoList = new ArrayList<PointColor>();
        pocoList.add(new PointColor(p_left_top, c_left_top, true));
        GameUtil.waitUntilAllColor(pocoList, GameConstant.DELAY);

        // 移动窗口至左上角
        GameUtil.moveToLeftTop();

        // 检测loading是否完毕
        checkLoadingAndTransferPage(countNum);
        return;
    }

    private void checkLoadingAndTransferPage(int countNum) throws Exception {
        List<PointColor> pcList = new ArrayList<PointColor>();
        pcList.add(new PointColor(POINT_INFO.getpLoading(), POINT_INFO.getcLoading(), POINT_INFO.getDeadPoint(), true));
        pcList.add(new PointColor(POINT_INFO.getpTransfer(), POINT_INFO.getcTransfer(), POINT_INFO.getDeadPoint(), true));
        List<PointColor> finishPCList = new ArrayList<PointColor>();
        finishPCList.add(new PointColor(POINT_INFO.getpTransfer(), POINT_INFO.getcTransfer(), POINT_INFO.getDeadPoint(), true));
        AutoAct ac = new AutoAct(pcList, finishPCList) {
            @Override
            public void doSomeThing() {
                Color cWait = this.getPcWait().getColor();
                if (GameUtil.likeEqualColor(POINT_INFO.getcTransfer(), cWait)) {
                    GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + countNum + "_引继页面.");
                }
            }
        };
        ac.autoAct();
    }

    private void waitForHomePage() throws Exception {
        GameUtil.getRb().delay(GameConstant.DELAY*5);
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

    /**
     * 等待攻击（蓝色）
     * @param str
     * @return
     * @throws Exception
     */
    protected final PointColor waitToAttack(String str) throws Exception {
        if (str != null) {
            LOGGER.info("等待（ " + str + " ）回合蓝盾攻击：");
        }
        // 蓝盾攻击点
        Point p_blue_attack = POINT_INFO.getpBlueAttack();
        Color c_blue_attack = POINT_INFO.getcBlueAttack();
        // 羁绊结算三角
        Point p_fetter01 = POINT_INFO.getpFetter01();
        Color c_fetter01 = POINT_INFO.getcFetter01();

        List<PointColor> pocoList = new ArrayList<PointColor>();
        pocoList.add(new PointColor(p_blue_attack, c_blue_attack, true, "attack"));
        pocoList.add(new PointColor(p_fetter01, c_fetter01, true, "balance"));
        PointColor pc = GameUtil.waitUntilOneColor(pocoList);
        return pc;
    }

    private void preferenceSet() {
        // 初始化偏好
        // 偏好坐标点
        Point ph0 = new Point(1231, 257);// 颜色：25;55;98 Color ch0 = new Color(25, 55, 98);
        GameUtil.mouseMoveByPoint(ph0);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.delay(2 *GameConstant.DELAY);
        // 选项坐标点
        Point ph1 = new Point(898, 327);// 颜色：156;150;140 灰色 不等于
        Color ch1 = new Color(156, 150, 140);
        Color ch11 = GameUtil.getScreenPixel(ph1);
        Point ph2 = new Point(1125, 329);// 颜色：156;153;147 灰色 不等于
        Color ch2 = new Color(156, 153, 147);
        Color ch22 = GameUtil.getScreenPixel(ph2);
        Point ph3 = new Point(897, 396);// 颜色：156;150;140 灰色 等于
        Color ch3 = new Color(156, 150, 140);
        Color ch33 = GameUtil.getScreenPixel(ph3);
        if (!GameUtil.likeEqualColor(ch1, ch11)) {
            GameUtil.mouseMoveByPoint(ph1);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        if (!GameUtil.likeEqualColor(ch2, ch22)) {
            GameUtil.mouseMoveByPoint(ph2);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        if (GameUtil.likeEqualColor(ch3, ch33)) {
            GameUtil.mouseMoveByPoint(ph3);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        // 死角点击
        GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        // 蓝色圆板
        Point p0 = new Point(1171, 697);// 颜色：0;113;216 Color c1 = new Color(0, 113, 216);
        GameUtil.mouseMoveByPoint(p0);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        GameUtil.delay(2 * GameConstant.DELAY);
        // 加速坐标
        // 偏中间
        Point pm = new Point(1171, 118);// 颜色：55;71;53 Color c = new Color(55, 71, 53);
        // 偏左
        Point pl = new Point(1158, 116);// 颜色：21;51;16 Color c = new Color(21, 51, 16);
        if (GameUtil.colorMinus(pm, pl) > 0) {
            GameUtil.mouseMoveByPoint(pm);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        // 复位点
        Point pf = new Point(1237, 733);// 颜色：202;204;207 Color cf = new Color(202, 204, 207);
        GameUtil.mouseMoveByPoint(pf);
        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
    }
    private void startBalance(int accountNum, int apNum, int count) throws Exception {
        LOGGER.info("结算侦测");
        // 死角点
        Point dead_point = POINT_INFO.getDeadPoint();
        // 复位点
        Point p_reset = POINT_INFO.getpReset();
        // 羁绊三角1
        Point p_fetter01 = POINT_INFO.getpFetter01();
        Color c_fetter01 = POINT_INFO.getcFetter01();
        // 羁绊三角2
        Point p_fetter02 = POINT_INFO.getpFetter02();
        Color c_fetter02 = POINT_INFO.getcFetter02();
        // 羁绊升级
        Point p_fetter_up = POINT_INFO.getpFetterUp();
        Color c_fetter_up = POINT_INFO.getcFetterUp();
        Point p_level_up = POINT_INFO.getpLevelUp();
        Color c_level_up = POINT_INFO.getcLevelUp();
        // 确认点
        Point p_confirm_rd = POINT_INFO.getpConfirmRd();
        Color c_confirm_rd = POINT_INFO.getcConfirmRd();
        //好友申请拒绝点
  		Point p_get_friend_no = POINT_INFO.getpGetFriendNo();
  		Color c_get_friend_no = POINT_INFO.getcGetFriendNo();
        // 咕哒子
        Point p_guda = POINT_INFO.getpGuda();
        Color c_guda = POINT_INFO.getcGuda();
        // 获取奖励动态坐标颜色
        Point p_reward_action = POINT_INFO.getpRewardAction();
        Color c_reward_action = POINT_INFO.getcRewardAction();

        List<PointColor> pcList = new ArrayList<PointColor>();
        pcList.add(new PointColor(p_fetter01, c_fetter01, p_reset, true));
        pcList.add(new PointColor(p_fetter02, c_fetter02, p_reset, true));
        pcList.add(new PointColor(p_fetter_up, c_fetter_up, p_reset, true));
        pcList.add(new PointColor(p_level_up, c_level_up, p_reset, true));
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
                if (GameUtil.likeEqualColor(c_confirm_rd, cWait)) {
                    GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "账号" + accountNum + "_" + apNum + "ap第" +count+ "次奖励页面.");
                }
            }
        };
        ac.autoAct();
    }
    private void returnTopPage() throws Exception {
        Point pNotice = POINT_INFO.getpNoticeExit();
        Color cNotice = POINT_INFO.getcNoticeExit();
        Color temp = GameUtil.getScreenPixel(pNotice);
        Point pBack = null;
        LOGGER.info("开始返回首页");
        for (int i = 0; i < 20; i++) {
            if (GameUtil.likeEqualColor(cNotice, temp)) {
                break;
            }
            pBack = POINT_INFO.getpBack();
            GameUtil.mouseMoveByPoint(pBack);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
            GameUtil.delay(GameConstant.DELAY*2);
            temp = GameUtil.getScreenPixel(pNotice);
        }
    }
    protected final  void moveBySteps(Point start, Point end) {
        Robot r = GameUtil.getRb();
        int actor = end.getX() + end.getY() - start.getX() - start.getY() > 0 ? 1 : -1;
        if ((int) start.getX() == (int) end.getX()) {

            for (int i = (int) start.getY(); (i - end.getY()) * actor < 0; i += GameConstant.MOVE_STEP * actor) {
                r.mouseMove((int) start.getX(), i);
                if (i == (int) start.getY()) {
                    GameUtil.delay(GameConstant.DELAY);
                    mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                }
                r.delay(GameConstant.MOVE_TIME);
            }
        } else {
            for (int i = (int) start.getX(); (i - end.getX()) * actor < 0; i += GameConstant.MOVE_STEP * actor) {
                r.mouseMove(i, (int) start.getY());
                if (i == (int) start.getX()) {
                    GameUtil.delay(GameConstant.DELAY);
                    mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                }
                r.delay(GameConstant.MOVE_TIME);
            }
        }
        r.mouseMove((int) end.getX(), (int) end.getY());
        GameUtil.delay(GameConstant.DELAY);
        mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
    }
    private void mousePress(int key) {
        GameUtil.getRb().mousePress(key);
        GameUtil.delay(GameConstant.DELAY);
        GameUtil.delay(GameConstant.DELAY);
    }
    private void mouseRelease(int key) {
        GameUtil.getRb().mouseRelease(key);
        GameUtil.getRb().delay(GameConstant.DELAY);
        GameUtil.getRb().delay(GameConstant.DELAY);
    }

    protected final  void giveServantSkills(List<Map<String, Object>> list) throws Exception {
        Map<String, Object> tempMap = null;
        int size = list.size();
        for (int j = 0; j < size; j++) {
            tempMap = list.get(j);
            int[] skills = (int[]) tempMap.get("skills");
            fromToServantSkills((int) tempMap.get("from"),
                    (int) tempMap.get("to"), skills);
            if (skills.length!=0 && j != size-1) {
                waitToAttack(null);
            }
        }
    }
    private void fromToServantSkills(int from,int to,int[] skill) throws Exception {
        Point[] skillPoints = new Point[skill.length];
        Point temp = null;
        int size = skill.length;
        for (int i = 0; i < size; i++) {
            int key = skill[i];
            switch (key) {
                case 0:{
                    temp = POINT_INFO.getpSkill01();
                    skillPoints[i]=	new Point((int) temp.getX()
                            + GameConstant.HEAD_SPACE * from,
                            (int) temp.getY());
                    break;
                }
                case 1:{
                    temp = POINT_INFO.getpSkill02();
                    skillPoints[i]=	new Point((int) temp.getX()
                            + GameConstant.HEAD_SPACE * from,
                            (int) temp.getY());
                    break;
                }
                case 2:{
                    temp = POINT_INFO.getpSkill03();
                    skillPoints[i]=	new Point((int) temp.getX()
                            + GameConstant.HEAD_SPACE * from,
                            (int) temp.getY());
                    break;
                }
                default:{
                    break;
                }
            }
        }
        Point toPerson = null;
        switch (to) {
            case 0:{
                toPerson = POINT_INFO.getSkillPerson01();
                break;
            }
            case 1:{
                toPerson = POINT_INFO.getSkillPerson02();
                break;
            }
            case 2:{
                toPerson = POINT_INFO.getSkillPerson03();
                break;
            }
            default:{
                break;
            }
        }
        int num =skillPoints.length;
        for (int i = 0; i < num; i++) {
            GameUtil.mouseMoveByPoint(skillPoints[i]);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

            // 选择人物
            GameUtil.mouseMoveByPoint(toPerson);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

            GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
            if (i != num - 1) {
                // 等待
                waitToAttack(null);
            }
        }
    }
    protected final  void giveClothSkills(List<Map<String, Object>> list) throws Exception {
        Map<String, Object> tempMap = null;
        int size = list.size();
        for (int j = 0; j < size; j++) {
            tempMap = list.get(j);
            fromToClothSkills((int) tempMap.get("to"),
                    (int[]) tempMap.get("skills"));
            if (j != size-1) {
                waitToAttack(null);
            }
        }
    }
    private void fromToClothSkills(int to,int[] skill) throws Exception {
        Point[] skillPoints = new Point[skill.length];
        int size = skill.length;
        for (int i = 0; i < size; i++) {
            int key = skill[i];
            switch (key) {
                case 0:{
                    skillPoints[i]=	POINT_INFO.getpClothSkill01();
                    break;
                }
                case 1:{
                    skillPoints[i]=	POINT_INFO.getpClothSkill02();
                    break;
                }
                case 2:{
                    skillPoints[i]=	POINT_INFO.getpClothSkill03();
                    break;
                }
                default:{
                    break;
                }
            }
        }
        Point toPerson = null;
        switch (to) {
            case 0:{
                toPerson = POINT_INFO.getSkillPerson01();
                break;
            }
            case 1:{
                toPerson = POINT_INFO.getSkillPerson02();
                break;
            }
            case 2:{
                toPerson = POINT_INFO.getSkillPerson03();
                break;
            }
            default:{
                break;
            }
        }
        int num = skillPoints.length;
        for (int i = 0; i < num; i++) {
            // 选择衣服，技能
            GameUtil.mouseMoveByPoint(POINT_INFO.getpCloth());
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
            GameUtil.mouseMoveByPoint(skillPoints[i]);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

            // 选择人物
            GameUtil.mouseMoveByPoint(toPerson);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

            GameUtil.mouseMoveByPoint(POINT_INFO.getDeadPoint());
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
            if (i != num - 1) {
                // 等待
                waitToAttack(null);
            }
        }
    }
    private final List<CommonCard> getColorCommondCards(){
        String  zeroPosition =  PropertiesUtil.getValueFromCommandCardFile("zeroPosition");
        String  clickPosition =  PropertiesUtil.getValueFromCommandCardFile("clickPosition");
        String  distribution =  PropertiesUtil.getValueFromCommandCardFile("distribution");
        Point[] zeroPoints = getPointsBySymBol(zeroPosition);// 每张卡的 原点 数组
        Point[] clickPoints = getPointsBySymBol(clickPosition);// 每张卡的 点击点 数组
        Point[] distributionPoints = getPointsBySymBol(distribution);// 一张卡的 颜色分布取点 数组

        int zpLen = zeroPoints.length;
        int cpLen = clickPoints.length;
        int dtLen = distributionPoints.length;

        Point tempPoint;
        Color tempColor;

        int rPulus = 0;
        int gPulus = 0;
        int bPulus = 0;
        int[] rgb;
        int rgblen;
        List<CommonCard> ccList= new ArrayList<>();
        CommonCard tempCommonCard;

        int Maxindex;
        int value;
        for (int i = 0; i < zpLen; i++) {
            rgb = new int[3];
            for (int j = 0; j < dtLen; j++) {
                tempPoint = new Point(
                        (int) (zeroPoints[i].getX() + distributionPoints[j].getX()),
                        (int) (zeroPoints[i].getY() + distributionPoints[j].getY())
                );
                tempColor = GameUtil.getScreenPixel(tempPoint);
                rgb[0] += tempColor.getRed();
                rgb[1] += tempColor.getGreen();
                rgb[2] += tempColor.getBlue();
            }
            rgblen = rgb.length;
            Maxindex = 0;
            value = rgb[0];
            for (int j = 1; j < rgblen; j++) {
                if (value < rgb[j]) {
                    Maxindex = j;
                    value = rgb[j];
                }
            }
            tempCommonCard = new CommonCard();
            tempCommonCard.setpLoc(clickPoints[i]);
            tempCommonCard.setWeak(true);
            switch(Maxindex){
                case 0 : {
                    tempCommonCard.setCardColor(GameConstant.RED);
                    break;
                }
                case 1 : {
                    tempCommonCard.setCardColor(GameConstant.GREEN);
                    break;
                }
                case 2 : {
                    tempCommonCard.setCardColor(GameConstant.BLUE);
                    break;
                }
                default : {
                    break;
                }
            }
            ccList.add(tempCommonCard);
        }
        return ccList;
    }
    private Point[] getPointsBySymBol(String string){
        String[] pointStringArray = string.split("_");
        int len = pointStringArray.length;

        String[] tempStringArray;
        Point[] points = new Point[len];

        for (int i = 0; i < len; i++) {
            tempStringArray = pointStringArray[i].split(",");
            points[i] = new Point(
                    Integer.parseInt(tempStringArray[0]),
                    Integer.parseInt(tempStringArray[1])
            );
        }
        return points;
    }
    protected final  Map<String, List<CommonCard>> getWeakCommondCards(Comparator<CommonCard> comp){
        List<CommonCard> ccList = getColorCommondCards();
        Collections.sort(ccList, comp);
        Map<String, List<CommonCard>> scMap = new HashMap<>();
        List<CommonCard> trueList = new ArrayList<CommonCard>();
        List<CommonCard> falseList = new ArrayList<CommonCard>();
        int num = ccList.size();
        for (int i = 0; i < num; i++) {
            CommonCard commonCard = ccList.get(i);
			LOGGER.info(commonCard.getCardColor() + "_");
			LOGGER.info(commonCard.isWeak());
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
    protected final  void attackNPAAA()  throws Exception{
        attackAAA(CardComparator.getBgrComparotor());
    }
    private void attackAAA(Comparator<CommonCard> comp) throws Exception {
        // 蓝色圆板
        Point p0 = new Point(1171, 697);// 颜色：0;113;216 Color c1 = new Color(0, 113, 216);
        GameUtil.mouseMoveByPoint(p0);
        GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
        // 等待一会儿
        GameUtil.delay(GameConstant.DELAY * 4);
        Map<String, List<CommonCard>> scMap = getWeakCommondCards(comp);
        List<CommonCard> trueList = scMap.get("trueList");

        int size = trueList.size();
        for (int i = 0; i < 3; i++) {
            GameUtil.mouseMoveByPoint(trueList.get(i).getpLoc());
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

        }
        checkExitCardSelect();
    }
    private boolean isTopage() {
		Point p_guda = POINT_INFO.getpGuda();
		Color c_guda = POINT_INFO.getcGuda();
		Color Temp = GameUtil.getScreenPixel(p_guda);
		if (GameUtil.likeEqualColor(Temp, c_guda)) {
			return true;
		}
		return false;
	}
    protected final  void checkExitCardSelect() throws FgoNeedRestartException {
        GameUtil.delay(GameConstant.DELAY * 5);
        Point p = POINT_INFO.getpCardExit();
        Color c = POINT_INFO.getcCardExit();
        Color temp = GameUtil.getScreenPixel(p);
        boolean isColor = GameUtil.likeEqualColor(c, temp);
        if (isColor) {
            GameUtil.mouseMoveByPoint(p);
            GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        }
    }
    protected final  void blueAttackSelect() throws FgoNeedRestartException {
    	 Point p_card_exit = POINT_INFO.getpCardExit();
         Color c_card_exit = POINT_INFO.getcCardExit();
         PointColor pc = new PointColor(p_card_exit, c_card_exit, true);
         Point p5 = new Point(1171, 697);
         GameUtil.mouseMoveByPoint(p5);
         GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK, pc);
         // 宝具动画延时
         GameUtil.delay(GameConstant.DELAY * 4);
    }
    protected GatesInfo getSpecialGatesInfo(){
        String filepath = getSpecialGatesFilePath();
        String jsonString = GameUtil.getJsonString(filepath);
        GatesInfo gi = JSON.parseObject(jsonString, GatesInfo.class);
        return gi;
    }
    public static void main(String[] args) {
    }
}
