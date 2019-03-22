package fgoScript.constant;

import java.awt.Color;
import java.awt.Point;

import fgoScript.util.PropertiesUtil;

public class PointInfo {
	// FGO下三角坐标
	public static Point P_SELECT_01 = new Point(714,236);
	// 多选父标签坐标
	public static Point P_SELECT_02 = new Point(739,258);
	// 子列表起始坐标
	public static Point P_SELECT_03 = new Point(861,261);

	// 滚动条重置点
	public static Point P_SCROLL_REST_TOP = new Point(1294, 146);
	public static Point P_SCROLL_REST_DOWN = new Point(1294, 679);
	// 周回入口
	public static Point P_WEEK_ENTRANCE_01 = new Point(937, 422);
	public static Point P_WEEK_ENTRANCE_02 = new Point(955, 598);
	public static Point P_WEEK_ENTRANCE;
	
	public static Point getP_WEEK_ENTRANCE() {
		String code = PropertiesUtil.getValueFromConfig("WEEK_ENTRANCE");
		if ("01".equals(code)) {
			return P_WEEK_ENTRANCE_01;
		}else if ("02".equals(code)) {
			return P_WEEK_ENTRANCE_02;
		}else {
			return null;
		}
	}

	public static void setP_WEEK_ENTRANCE(Point p_WEEK_ENTRANCE) {
		P_WEEK_ENTRANCE = p_WEEK_ENTRANCE;
	}
	// 房间起始点标识
	public static Point P_ROOM = new Point(84,93);
	public static Color C_ROOM = new Color(60, 67, 126);	
	// 房间起始点标识
	public static Point P_ROOM_CLOSE = new Point(123,96);
	public static Color C_ROOM_CLOSE = new Color(54, 57, 72);
	// 副本01  Y间隔 190 
	public static Point P_ROOM_SELECT01 = new Point(939,241);
	public static Color C_ROOM_SELECT01 = new Color(191, 215, 239);
	public static Point P_ROOM_SELECT02 = new Point(939,241 + 190);
	public static Color C_ROOM_SELECT02 = new Color(191, 215, 239);
	// 礼物入口（通常）
	public static Point P_WEEK_GIFT = new Point(967, 250);
	// 锁定日常本 滚动条 坐标
	public static Point P_DAILY_SLICE_STRAT = new Point(810, 175);
	public static Point P_DAILY_SLICE_END = new Point(810, 647);
	// 日常入口
	public static Point P_DAILY_ENTRANCE = new Point(852, 244);
	// 训练场进入相关坐标
	public static Point P_TRAIN_ALL = new Point(1296, 374);
	private static Point P_TRAIN_AP10 = new Point(930, 187);
	private static Point P_TRAIN_AP20 = new Point(930, 374);
	private static Point P_TRAIN_AP30 = new Point(930, 560);
	private static Point P_TRAIN_AP40 = new Point(930, 736);
	// QP进入相关坐标
	public static Point P_QP_ALL = new Point(1298, 509);
	private static Point P_QP_AP10 = new Point(930, 195);
	private static Point P_QP_AP20 = new Point(930, 376);
	private static Point P_QP_AP30 = new Point(930, 565);
	private static Point P_QP_AP40 = new Point(930, 740);
	// Exp进入相关坐标
	public static Point P_EXP_ALL = new Point(1298,237);
	private static Point P_EXP_AP10 = new Point(930,192);
	private static Point P_EXP_AP20 = new Point(930,370);
	private static Point P_EXP_AP30 = new Point(930,557);
	private static Point P_EXP_AP40 = new Point(930,729);
	// 公告相关坐标，颜色
	public static Point P_NOTICE_EXIT = new Point(1273, 82);
	public static Color C_NOTICE_EXIT = new Color(8, 12, 8);
	// 公告相关坐标，颜色(灰色)
	public static Point P_NOTICE_EXIT_DARK = new Point(1284, 83);// 颜色：158;155;158
	public static Color C_NOTICE_EXIT_DARK = new Color(158, 155, 158);
	// 后退按钮
	public static Point P_BACK = new Point(139, 95);
	// 职介坐标
	public static Point P_SERVANT_ALL = new Point(131, 176);// all
	public static Point P_SERVANT_SABER = new Point(196, 176);// saber
	public static Point P_SERVANT_ARCHER = new Point(264, 180);// archer
	public static Point P_SERVANT_LANCER = new Point(334, 177);// lancer
	public static Point P_SERVANT_RIDER = new Point(400, 180);// rider
	public static Point P_SERVANT_CASTER = new Point(468, 179);// caster
	public static Point P_SERVANT_ASSASIN = new Point(535, 178);// asssion
	public static Point P_SERVANT_BASKER = new Point(598, 180);// basker
	public static Point P_SERVANT_FOUR = new Point(669, 186);// four
	public static Point P_SERVANT_MIX = new Point(736,176);// four
	// FGO图标坐标
	public static final Point P_ICON_04 = new Point(1089, 395);
	public static final Color C_ICON_04 = new Color(224, 194, 165);
	public static final Point P_ICON_05 = new Point(1289, 393);
	public static final Color C_ICON_05 = new Color(225, 186, 153);
	// 受取可能坐标 颜色
	public static Point P_REWARD_GET = new Point(1211, 269);
	public static Color C_REWARD_GET = new Color(64, 91, 107);
	// 获取奖励动态坐标颜色
	public static Point P_REWARD_ACTION = new Point(1168, 201);
	public static Color C_REWARD_ACTION = new Color(138, 157, 172);
	// 复位点
	public static Point P_RESET = new Point(1237, 733);
	// 蓝盾攻击点
	public static Point P_BLUE_ATTACK = new Point(1128, 625);
	public static Color C_BLUE_ATTACK = new Color(0, 208, 242);
	// 羁绊三角1
	public static Point P_FETTER01 = new Point(126,236);
	public static Color C_FETTER01 = new Color(245, 196, 39);
	// 羁绊三角2
	public static Point P_FETTER02 = new Point(715,255);
	public static Color C_FETTER02 = new Color(239, 190, 40);
	// 关闭点
	public static Point P_CLOSE_MD = new Point(649, 606);
	public static Color C_CLOSE_MD = new Color(79, 78, 79);
	// 好友申请拒绝点
	public static Point P_GET_FRIEND_NO = new Point(323,662);
	public static Color C_GET_FRIEND_NO = new Color(126, 126, 126);	
	// 确认点
	public static Point P_CONFIRM_RD = new Point(1061,731);
	public static Color C_CONFIRM_RD = new Color(222, 219, 222);
	// 咕哒子
	public static Point P_GUDA = new Point(198,741);
	public static Color C_GUDA = new Color(238, 224, 154);
	// 咕哒子 (深色)
	public static Point P_GUDA_DARK = new Point(200,742); 
	public static Color C_GUDA_DARK = new Color(62, 61, 59);
	// 底部菜单关闭按钮
	public static Point P_DOWN_PANEL_CLOSE = new Point(1237,580);
	public static Color C_DOWN_PANEL_CLOSE = new Color(49,60,112);	
	// 死角点
	public static Point DEAD_POINT = new Point(979, 93);
	// 角色升级点
	public static Point P_LEVEL_UP = new Point(762,337);
	public static Color C_LEVEL_UP = new Color(239, 190, 41);	
	// 退出选卡按钮
	public static Point P_CARD_EXIT = new Point(1251,736);
	public static Color C_CARD_EXIT = new Color(11, 39, 77);	
	// 宝具详情退出按钮
	public static Point P_NP_DT_EXIT = new Point(1092,135);
	public static Color C_NP_DT_EXIT = new Color(193,192,195);
	// 怪物详情退出按钮
	public static Point P_MOSTER_DT_EXIT = new Point(1096,167);
	public static Color C_MOSTER_DT_EXIT = new Color(143, 158, 192);	
	// 窗口左上角点
	public static Point P_LEFT_TOP = new Point(401, 193);
	public static Color C_LEFT_TOP = new Color(241, 94, 145);
	// 窗口左上角点(移动后)
	public static Point P_LEFT_TOP_MOVE = new Point(86,32);
	public static Color C_LEFT_TOP_MOVE = new Color(241, 94, 145);
	// 再开按钮(否定)
	public static Point P_BUTTON_RESTART_NO = new Point(452, 598);
	public static Color C_BUTTON_RESTART_NO = new Color(142, 142, 142);
	// 再开按钮(确定)
	public static Point P_BUTTON_RESTART_YES = new Point(851, 600);
	public static Color C_BUTTON_RESTART_YES = new Color(100, 99, 100);
	// 战败撤退按钮
	public static Point P_BUTTON_FAIL_BACK = new Point(326, 366);
	public static Color C_BUTTON_FAIL_BACK = new Color(141, 139, 141);
	// 撤退确定按钮
	public static Point P_BUTTON_FAIL_BACK_YES = new Point(909, 410);
	public static Color C_BUTTON_FAIL_BACK_YES = new Color(103, 102, 103);
	// 撤退否定按钮
	public static Point P_BUTTON_FAIL_BACK_NO = new Point(412, 417);
	public static Color C_BUTTON_FAIL_BACK_NO = new Color(165, 168, 165);
	// 需要苹果
	public static Point P_APPLE_NEED01 = new Point(397,372);
	public static Color C_APPLE_NEED01 = new Color(252, 239, 14);
	public static Point P_APPLE_NEED02 = new Point(398,376);
	public static Color C_APPLE_NEED02 = new Color(250, 234, 2);
	// 直连按钮（通信异常）
	public static Point P_CONNECT_YES = new Point(906,620);
	public static Color C_CONNECT_YES = new Color(192, 190, 193);
	// 通信终了
	public static Point P_CONNECT_END = new Point(902,612);
	public static Color C_CONNECT_END = new Color(84, 84, 84);	
	// 再连按钮（通信异常）
	public static Point P_RE_CONNECT_YES = new Point(907,612);
	public static Color C_RE_CONNECT_YES = new Color(159, 157, 159);
	//更新按钮
	public static Point P_UPDATE = new Point(882,612);
	public static Color C_UPDATE = new Color(33, 33, 33);	
	//更新按钮 否定按钮
	public static Point P_UPDATE_NO = new Point(438,612);
	public static Color C_UPDATE_NO = new Color(154, 152, 154);		
	//小手图标
	public static Point P_HAND = new Point(710,92);
	public static Color C_HAND = new Color(173, 231, 107);		
	// 卡牌支援判断点
	public static Point P_SUPPORT = new Point(265, 481);
	public static Point[] P_SUPPORTS = {
			new Point(266,479),
			new Point(520,479),
			new Point(777,479),
			new Point(1034,479),
			new Point(1294,473)
	};
	public static Point P_CARD_COLOR = new Point(96,486);
	public static Point P_CARD_WEAK = new Point(243,432);
	public static Point P_CARD_CLICK = new Point(163,535);
	// 羁绊升级点
	public static Point P_FETTER_UP = new Point(708,394);//颜色：239;194;37 
	public static Color C_FETTER_UP = new Color(115, 239, 124);
	// 模拟器图标
	public static Point P_VIRTURAL_ICON = new Point(40,218);
	public static Color C_VIRTURAL_ICON = new Color(242, 96, 147);	
	// 应用左上角
	public static Point P_LT_APP = new Point(416,145);
	public static Color C_LT_APP = new Color(241, 94, 145);
	// 我的游戏（点击后）
	public static Point P_MYGAME = new Point(430,378);
	public static Color C_MYGAME = new Color(241, 94, 145);	
	// 无支援处理
	public static Point P_NO_SUPPORT = new Point(491,486);
	public static Color C_NO_SUPPORT = new Color(97, 99, 101);	
	// 支援更新按钮
	public static Point P_SUPPORT_UPDATE = new Point(887,183);
	public static Color C_SUPPORT_UPDATE = new Color(4, 56, 95);
	// 支援更新确定按钮
	public static Point P_SUPPORT_UPDATE_YES = new Point(875,615);
	public static Color C_SUPPORT_UPDATE_YES = new Color(222, 219, 222);
	// 无法更新按钮，确定
	public static Point P_SUPPORT_NO_CONFIRM = new Point(652,608);
	public static Color C_SUPPORT_NO_CONFIRM = new Color(39, 39, 39);	
	// SKILL01
	public static Point P_SKILL01 = new Point(111,627);
	// SKILL02
	public static Point P_SKILL02 = new Point(204,627);
	// SKILL03
	public static Point P_SKILL03 = new Point(295,623);
	// 战斗服
	public static Point P_CLOTH = new Point(1229, 359);
	// CLOTH_SKILL01
	public static Point P_CLOTH_SKILL01 = new Point(942, 362);
	// CLOTH_SKILL02
	public static Point P_CLOTH_SKILL02 = new Point(1029, 357);
	// CLOTH_SKILL03
	public static Point P_CLOTH_SKILL03 = new Point(1115, 360);
	// NPCHECK
	public static Point NP_CHECK = new Point(250,723);
	// NP
	public static Point NP_NP = new Point(452,247);
	//怪物
	public static Point P_MOSTER01 = new Point(167,94);
	public static Point P_MOSTER02 = new Point(407,94);
	public static Point P_MOSTER03 = new Point(669,94);
	// 选择人物
	public static Point SKILL_PERSON_01 = new Point(359,507);
	public static Point SKILL_PERSON_02 = new Point(677,507);
	public static Point SKILL_PERSON_03 = new Point(994,507);
	public static Point P_EVENT_SCROLL_LOCATE01 = new Point(1293,418);
	public static Point P_EVENT_BATTLE_LOCATE01 = new Point(941,315);
	// 桌面重置位置
	public static Point getTrainingRoom(int trainTip) throws Exception {
		switch (trainTip) {
		case 10: {
			return P_TRAIN_AP10;
		}
		case 20: {
			return P_TRAIN_AP20;
		}
		case 30: {
			return P_TRAIN_AP30;
		}
		case 40: {
			return P_TRAIN_AP40;
		}
		default: {
			throw new Exception("No Such RoomCode");
		}
		}
	}

	public static Point getQpRoom(int trainTip) throws Exception {
		switch (trainTip) {
		case 10: {
			return P_QP_AP10;
		}
		case 20: {
			return P_QP_AP20;
		}
		case 30: {
			return P_QP_AP30;
		}
		case 40: {
			return P_QP_AP40;
		}
		default: {
			throw new Exception("No Such RoomCode");
		}
		}
	}
	public static Point getExpRoom(int trainTip) throws Exception {
		switch (trainTip) {
		case 10: {
			return P_EXP_AP10;
		}
		case 20: {
			return P_EXP_AP20;
		}
		case 30: {
			return P_EXP_AP30;
		}
		case 40: {
			return P_EXP_AP40;
		}
		default: {
			throw new Exception("No Such RoomCode");
		}
		}
	}
}
