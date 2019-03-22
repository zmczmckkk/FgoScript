package fgoScript.entity.guda;

import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fgoScript.constant.GameConstant;
import fgoScript.constant.PointInfo;
import fgoScript.entity.AbstractWaitForOutTime;
import fgoScript.entity.CardComparator;
import fgoScript.entity.ColorVo;
import fgoScript.entity.CommonCard;
import fgoScript.entity.PointColor;
import fgoScript.exception.FgoNeedNextException;
import fgoScript.exception.FgoNeedRestartException;
import fgoScript.exception.FgoNeedStopException;
import fgoScript.service.AutoAct;
import fgoScript.service.EventFactors;
import fgoScript.service.ProcessDeal;
import fgoScript.util.GameUtil;
import fgoScript.util.PropertiesUtil;

public abstract class AbstractApGudazi {
    private static final Logger LOGGER = LogManager.getLogger(AbstractApGudazi.class);
    private String PREFIX = GameUtil.getPreFix();
    private int retunTimes = 0;
    protected Color[][] DEFAULT_SKILL_COLORS;
    /**
     * 进入选择副本
     * @param apNum ap数字
     */
    public abstract void intoAndSelect(int apNum)throws Exception;

    /**
     * 战斗并结束
     * @param rebootFlag
     */
    public abstract void fightAndStop(boolean rebootFlag, int apNum) throws Exception;

    /**
     * 选职介
     */
    public abstract Point getSuppotServant();

    /**
     * @param accountArray
     * @param apArray
     * @throws Exception 
     */
    public void startAllFgo(int[] accountArray, int[] apArray) throws Exception {
        int accountNum;
        int size = accountArray.length;
        boolean ifClose = true;
        for (int i = 0; i < size; i++) {
            accountNum = accountArray[i];
            // 刷训练本
            try {
                startOneFgo(accountNum, apArray);
                // 关闭所有相关应用
                ProcessDeal.killAllTianTian();
            } catch (FgoNeedNextException e) {
                LOGGER.info(e.getMessage());
                GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + accountNum + "_体力不足页面.");
                continue;
            } catch (FgoNeedStopException e) {
            	ifClose = false;
            	throw e;
			}finally {
				if (ifClose) {
					ProcessDeal.killAllTianTian();
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
        	if (i>3 || StringUtils.isBlank(hasDo) || hasDo.indexOf(value)==-1) {
        		 // 开始战斗
                LOGGER.info("账号" + accountNum + ",ap场合：" + apNum + " AP 开始");
                startFight(accountNum, apNum, false, count, len, appleCost);
        		 //写入已经完成的 内容
                Map<String, String> map = new HashMap<>();
                if (hasDo.indexOf(value)==-1) {
                	map.put(key, StringUtils.isBlank(hasDo)?value:hasDo+value);
                	PropertiesUtil.setValueForHasDo(map);
				}
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
					ProcessDeal.startTianTian(accountNum);
					// 检测loading
					move2WinAndTransferPage(accountNum);
				}
				// 检测公告
				LOGGER.info("开始检测是否重开。。。");
				PointColor pc = open2GudaOrRestart();
				rebootFlag = pc.getColor().getRGB() == PointInfo.C_BUTTON_RESTART_YES.getRGB();
			}
            if (rebootFlag) {
                LOGGER.info("重开战斗");
            } else {
                waitForHomePage();
                // 选本选人
                LOGGER.info("选本选人");
                intoAndSelect(apNum);
                EventFactors.supportServant = getSuppotServant();
                selectRoomPressFightForQp(accountNum, appleCost, apNum);
                waitToAttack(null);
            }
        } catch (FgoNeedRestartException e) {
            LOGGER.info("进入房间异常！");
            LOGGER.info(e.getMessage());
            ProcessDeal.killAllTianTian();
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
                ProcessDeal.killAllTianTian();
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
                ProcessDeal.killAllTianTian();
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
 		Point p_apple_need01 = PointInfo.P_APPLE_NEED01;
 		Color c_apple_need01 = PointInfo.C_APPLE_NEED01;
 		Point p_apple_need02 = PointInfo.P_APPLE_NEED02;
 		Color c_apple_need02 = PointInfo.C_APPLE_NEED02;

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
                if (GameUtil.isEqualColor(c_apple_need01, cWait) ||
						GameUtil.isEqualColor(c_apple_need02, cWait)) {
                    if (appleCost == GameConstant.APPLE_COUNT) {
                        LOGGER.info("已达到苹果消耗量，停止更新苹果。");
                        throw new FgoNeedNextException();
                    }else {
                        GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "\\账号" + acountNum + "_使用第"+(appleCost+1)+"个苹果.");
                        GameUtil.mouseMoveByPoint(PointInfo.P_APPLE_NEED01);
                        GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
                        GameUtil.mouseMoveByPoint(PointInfo.P_CONNECT_YES);
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
  			intoAndSelect(apNum);
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
        Point p_left_top = PointInfo.P_LEFT_TOP;
        Color c_left_top = PointInfo.C_LEFT_TOP;
        java.util.List<PointColor> pocoList = new ArrayList<PointColor>();
        pocoList.add(new PointColor(p_left_top, c_left_top, true));
        GameUtil.waitUntilAllColor(pocoList, GameConstant.DELAY);

        // 移动窗口至左上角
        GameUtil.moveToLeftTop();

        // 检测loading是否完毕
        Point p_loading = new Point(516, 722);// 颜色：247;255;255
        Color c_loading = new Color(247, 255, 255);

        Point p_transfer = new Point(911, 670);// 颜色：0;60;165
        Color c_transfer = new Color(0, 60, 165);

        java.util.List<PointColor> pcList = new ArrayList<PointColor>();
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
    private PointColor open2GudaOrRestart() throws Exception {
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
    private void waitForHomePage() throws Exception {
        GameUtil.getRb().delay(GameConstant.DELAY*5);
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
        Point p_blue_attack = PointInfo.P_BLUE_ATTACK;
        Color c_blue_attack = PointInfo.C_BLUE_ATTACK;
        // 羁绊结算三角
        Point p_fetter01 = PointInfo.P_FETTER01;
        Color c_fetter01 = PointInfo.C_FETTER01;

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
        if (!GameUtil.isEqualColor(ch1, ch11)) {
            GameUtil.mouseMoveByPoint(ph1);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        if (!GameUtil.isEqualColor(ch2, ch22)) {
            GameUtil.mouseMoveByPoint(ph2);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        if (GameUtil.isEqualColor(ch3, ch33)) {
            GameUtil.mouseMoveByPoint(ph3);
            GameUtil.mousePressAndRelease(KeyEvent.BUTTON1_DOWN_MASK);
        }
        // 死角点击
        GameUtil.mouseMoveByPoint(PointInfo.DEAD_POINT);
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
        Point p_level_up = PointInfo.P_LEVEL_UP;
        Color c_level_up = PointInfo.C_LEVEL_UP;
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
                if (GameUtil.isEqualColor(c_confirm_rd, cWait)) {
                    GameUtil.img2file(GameConstant.IMG_EXTEND, PREFIX + "账号" + accountNum + "_" + apNum + "ap第" +count+ "次奖励页面.");
                }
            }
        };
        ac.autoAct();
    }
    private void returnTopPage() throws Exception {
        Point pNotice = PointInfo.P_NOTICE_EXIT;
        Color cNotice = PointInfo.C_NOTICE_EXIT;
        Color temp = GameUtil.getScreenPixel(pNotice);
        Point pBack = null;
        LOGGER.info("开始返回首页");
        for (int i = 0; i < 20; i++) {
            if (GameUtil.isEqualColor(cNotice, temp)) {
                break;
            }
            pBack = PointInfo.P_BACK;
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
                    temp = PointInfo.P_SKILL01;
                    skillPoints[i]=	new Point((int) temp.getX()
                            + GameConstant.HEAD_SPACE * from,
                            (int) temp.getY());
                    break;
                }
                case 1:{
                    temp = PointInfo.P_SKILL02;
                    skillPoints[i]=	new Point((int) temp.getX()
                            + GameConstant.HEAD_SPACE * from,
                            (int) temp.getY());
                    break;
                }
                case 2:{
                    temp = PointInfo.P_SKILL03;
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
                toPerson = PointInfo.SKILL_PERSON_01;
                break;
            }
            case 1:{
                toPerson = PointInfo.SKILL_PERSON_02;
                break;
            }
            case 2:{
                toPerson = PointInfo.SKILL_PERSON_03;
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

            GameUtil.mouseMoveByPoint(PointInfo.DEAD_POINT);
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
                    skillPoints[i]=	PointInfo.P_CLOTH_SKILL01;
                    break;
                }
                case 1:{
                    skillPoints[i]=	PointInfo.P_CLOTH_SKILL02;
                    break;
                }
                case 2:{
                    skillPoints[i]=	PointInfo.P_CLOTH_SKILL03;
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
                toPerson = PointInfo.SKILL_PERSON_01;
                break;
            }
            case 1:{
                toPerson = PointInfo.SKILL_PERSON_02;
                break;
            }
            case 2:{
                toPerson = PointInfo.SKILL_PERSON_03;
                break;
            }
            default:{
                break;
            }
        }
        int num = skillPoints.length;
        for (int i = 0; i < num; i++) {
            // 选择衣服，技能
            GameUtil.mouseMoveByPoint(PointInfo.P_CLOTH);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
            GameUtil.mouseMoveByPoint(skillPoints[i]);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

            // 选择人物
            GameUtil.mouseMoveByPoint(toPerson);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);

            GameUtil.mouseMoveByPoint(PointInfo.DEAD_POINT);
            GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
            if (i != num - 1) {
                // 等待
                waitToAttack(null);
            }
        }
    }
    protected final  Map<String, List<CommonCard>> getWeakCommondCards(Comparator<CommonCard> comp){
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
        List<CommonCard> falseList = scMap.get("falseList");
        int size = trueList.size();
        int len = falseList.size();
        if (size >= 2) {
        	  GameUtil.mouseMoveByPoint(trueList.get(0).getpLoc());
              GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
              if (len > 0) {
            	  GameUtil.mouseMoveByPoint(falseList.get(0).getpLoc());
            	  GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
              }else {
            	  GameUtil.mouseMoveByPoint(trueList.get(2).getpLoc());
                  GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK); 
              }
              GameUtil.mouseMoveByPoint(trueList.get(1).getpLoc());
              GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
		}else {
			for (int i = 0; i < size; i++) {
				GameUtil.mouseMoveByPoint(trueList.get(i).getpLoc());
				GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
			}
			if (size < 3) {
				int num = 3 - size;
				for (int i = 0; i < num; i++) {
					GameUtil.mouseMoveByPoint(falseList.get(i).getpLoc());
					GameUtil.mousePressAndReleaseQuick(KeyEvent.BUTTON1_DOWN_MASK);
				}
			}
		}
        checkExitCardSelect();
    }
    private boolean isTopage() {
		Point p_guda = PointInfo.P_GUDA;
		Color c_guda = PointInfo.C_GUDA;
		Color Temp = GameUtil.getScreenPixel(p_guda);
		if (GameUtil.isEqualColor(Temp, c_guda)) {
			return true;
		}
		return false;
	}
    protected final  void checkExitCardSelect() throws FgoNeedRestartException {
        GameUtil.delay(GameConstant.DELAY * 5);
        Point p = PointInfo.P_CARD_EXIT;
        Color c = PointInfo.C_CARD_EXIT;
        Color temp = GameUtil.getScreenPixel(p);
        boolean isColor = GameUtil.isEqualColor(c, temp);
        if (isColor) {
            GameUtil.mouseMoveByPoint(p);
            GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK);
        }
    }
    protected final  void blueAttackSelect() throws FgoNeedRestartException {
    	 Point p_card_exit = PointInfo.P_CARD_EXIT;
         Color c_card_exit = PointInfo.C_CARD_EXIT;
         PointColor pc = new PointColor(p_card_exit, c_card_exit, true);
         Point p5 = new Point(1171, 697);
         GameUtil.mouseMoveByPoint(p5);
         GameUtil.mousePressAndReleaseForConfirm(KeyEvent.BUTTON1_DOWN_MASK, pc);
         // 宝具动画延时
         GameUtil.delay(GameConstant.DELAY * 4);
    }
}
