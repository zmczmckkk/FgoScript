package fgoScript.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import commons.util.MySpringUtil;

import java.awt.*;

public class PointInfo {
	public static PointInfo getInstance(){
		PointInfo pi;
		String filepath = NativeCp.getUserDir() + "/config/PointInfo.json";
		pi = JSONObject.parseObject(GameUtil.getJsonString(filepath), PointInfo.class);
		return pi;
	}
	public static PointInfo getSpringBean(){
		return (PointInfo) MySpringUtil.getApplicationContext().getBean("pointInfo");
	}
	//重复战斗
	private Point pBattleReapeatYes;
	private Color cBattleReapeatYes;
	private Point pBattleReapeatNo;
	private Color cBattleReapeatNo;
	// 滚动条重置点
	private Point pScrollRestTop;
	private Point pScrollRestDown;
	// 周回入口
	private Point pWeekEntrance01;
	private Point pWeekEntrance02;
	private Point pWeekEntrance03;

	/* 副本01  Y间隔 190 */
	private Point pRoomSelect01;
	// 锁定日常本 滚动条 坐标
	private Point pDailySliceStrat;
	private Point pDailySliceEnd;
	// 日常入口
	private Point pDailyEntrance;
	// 训练场进入相关坐标
	private Point pTrainAll;
	private Point pTrainAp10;
	private Point pTrainAp20;
	private Point pTrainAp30;
	private Point pTrainAp40;
	// QP进入相关坐标
	private Point pQpAll;
	private Point pQpAp10;
	private Point pQpAp20;
	private Point pQpAp30;
	private Point pQpAp40;
	// Exp进入相关坐标
	private Point pExpAll;
	private Point pExpAp10;
	private Point pExpAp20;
	private Point pExpAp30;
	private Point pExpAp40;
	// 公告相关坐标，颜色
	private Point pNoticeExit;
	private Color cNoticeExit;
	// 公告相关坐标，颜色(灰色)
	private Point pNoticeExitDark;// 颜色：158;155;158
	private Color cNoticeExitDark;
	// 后退按钮
	private Point pBack              = new Point(139, 95);
	// 职介坐标
	private Point pServantAll;// all
	private Point pServantSaber;// saber
	private Point pServantArcher;// archer
	private Point pServantLancer;// lancer
	private Point pServantRider;// rider
	private Point pServantCaster;// caster
	private Point pServantAssasin;// asssion
	private Point pServantBasker;// basker
	private Point pServantFour;// four
	private Point pServantMix;// four
	// 受取可能坐标 颜色
	private Point pRewardGet;
	private Color cRewardGet;
	// 获取奖励动态坐标颜色
	private Point pRewardAction;
	private Color cRewardAction;
	// 复位点
	private Point pReset;
	// 蓝盾攻击点
	private Point pBlueAttack;
	private Color cBlueAttack;
	// 羁绊三角1
	private Point pFetter01;
	private Color cFetter01;
	// 羁绊三角2
	private Point pFetter02;
	private Color cFetter02;
	// 羁绊三角3
	private Point pFetter03;
	private Color cFetter03;
	// 关闭点
	private Point pCloseMd;
	private Color cCloseMd;
	// 好友申请拒绝点
	private Point pGetFriendNo;
	private Color cGetFriendNo;
	// 确认点
	private Point pConfirmRd;
	private Color cConfirmRd;
	// 咕哒子
	private Point pGuda;
	private Color cGuda;
	// 咕哒子 (深色)
	private Point pGudaDark;
	private Color cGudaDark;
	// 底部菜单关闭按钮
	private Point pDownPanelClose;
	private Color cDownPanelClose;
	// 死角点
	private Point deadPoint;
	// 角色升级点
	private Point pLevelUp;
	private Color cLevelUp;
	// 退出选卡按钮
	private Point pCardExit;
	private Color cCardExit;
	// 宝具详情退出按钮
	private Point pNpDtExit;
	private Color cNpDtExit;
	// 怪物详情退出按钮
	private Point pMosterDtExit;
	private Color cMosterDtExit;
	// 窗口左上角点
	private Point pLeftTop;
	private Color cLeftTop;
	// 再开按钮(否定)
	private Point pButtonRestartNo;
	private Color cButtonRestartNo;
	// 再开按钮(确定)
	private Point pButtonRestartYes;
	private Color cButtonRestartYes;
	// 战败撤退按钮
	private Point pButtonFailBack;
	private Color cButtonFailBack;
	// 撤退确定按钮
	private Point pButtonFailBackYes;
	private Color cButtonFailBackYes;
	// 撤退否定按钮
	private Point pButtonFailBackNo;
	private Color cButtonFailBackNo;
	// 需要苹果
	private Point pAppleNeed01;
	private Color cAppleNeed01;
	private Point pAppleNeed02;
	private Color cAppleNeed02;
	// 直连按钮（通信异常）
	private Point pConnectYes;
	private Color cConnectYes;
	// 通信终了
	private Point pConnectEnd;
	private Color cConnectEnd;
	// 再连按钮（通信异常）
	private Point pReConnectYes;
	private Color cReConnectYes;
	//更新按钮
	private Point pUpdate;
	private Color cUpdate;
	//更新按钮 否定按钮
	private Point pUpdateNo;
	private Color cUpdateNo;
	//小手图标
	private Point pHand;
	private Color cHand;
	// 卡牌支援判断点
	private Point pSupport;
	private Point[] pSupports = {
			new Point(266,479),
			new Point(520,479),
			new Point(777,479),
			new Point(1034,479),
			new Point(1294,473)
	};
	private Point pCardColor;
	private Point pCardWeak;
	private Point pCardClick;
	// 羁绊升级点
	private Point pFetterUp;
	private Color cFetterUp;
	// 应用左上角
	private Point pLtApp;
	private Color cLtApp;
	// 我的游戏（点击后）
	private Point pMygame;
	private Color cMygame;
	// 无支援处理
	private Point pNoSupport;
	private Color cNoSupport;
	// 加载页面
	private Point pLoading;
	private Color cLoading;

	private Point pTransfer;
	private Color cTransfer;

	private Point pSupportUpdate;
	private Color cSupportUpdate;
	// 支援更新确定按钮
	private Point pSupportUpdateYes;
	private Color cSupportUpdateYes;
	// 无法更新按钮，确定
	private Point pSupportNoConfirm;
	private Color cSupportNoConfirm;
	// SKILL01
	private Point pSkill01;
	// SKILL02
	private Point pSkill02;
	// SKILL03
	private Point pSkill03;
	// 战斗服
	private Point pCloth;
	// CLOTH_SKILL01
	private Point pClothSkill01;
	// CLOTH_SKILL02
	private Point pClothSkill02;
	// CLOTH_SKILL03
	private Point pClothSkill03;
	// NPCHECK
	private Point npCheck;
	// NP
	private Point npNp;
	// 技能对象窗口关闭按钮
	private Point pClothToSkill;
	private Color cClothToSkill;
	//怪物
	private Point pMoster01;
	private Point pMoster02;
	private Point pMoster03;
	// 选择人物
	private Point skillPerson01;
	private Point skillPerson02;
	private Point skillPerson03;
	private Point pEventScrollLocate01;
	private Point pEventBattleLocate01;

    public Point getP_WEEK_ENTRANCE() {
        String code = GameUtil.getValueFromConfig("WEEK_ENTRANCE");
        if ("01".equals(code)) {
            return pWeekEntrance01;
        } else if ("02".equals(code)) {
            return pWeekEntrance02;
        } else if ("03".equals(code)) {
			return pWeekEntrance03;
		} else {
            return pWeekEntrance02;
        }

    }
	// 桌面重置位置
	public Point getTrainingRoom(int trainTip) throws Exception {
		switch (trainTip) {
		case 10: {
			return pTrainAp10;
		}
		case 20: {
			return pTrainAp20;
		}
		case 30: {
			return pTrainAp30;
		}
		case 40: {
			return pTrainAp40;
		}
		default: {
			throw new Exception("No Such RoomCode");
		}
		}
	}

	public Point getQpRoom(int trainTip) throws Exception {
		switch (trainTip) {
		case 10: {
			return pQpAp10;
		}
		case 20: {
			return pQpAp20;
		}
		case 30: {
			return pQpAp30;
		}
		case 40: {
			return pQpAp40;
		}
		default: {
			throw new Exception("No Such RoomCode");
		}
		}
	}
	public Point getExpRoom(int trainTip) throws Exception {
		switch (trainTip) {
		case 10: {
			return pExpAp10;
		}
		case 20: {
			return pExpAp20;
		}
		case 30: {
			return pExpAp30;
		}
		case 40: {
			return pExpAp40;
		}
		default: {
			throw new Exception("No Such RoomCode");
		}
		}
	}

	public Point getpScrollRestTop() {
		return pScrollRestTop;
	}

	public void setpScrollRestTop(Point pScrollRestTop) {
		this.pScrollRestTop = pScrollRestTop;
	}

	public Point getpScrollRestDown() {
		return pScrollRestDown;
	}

	public void setpScrollRestDown(Point pScrollRestDown) {
		this.pScrollRestDown = pScrollRestDown;
	}

	public Point getpWeekEntrance01() {
		return pWeekEntrance01;
	}

	public void setpWeekEntrance01(Point pWeekEntrance01) {
		this.pWeekEntrance01 = pWeekEntrance01;
	}

	public Point getpWeekEntrance02() {
		return pWeekEntrance02;
	}

	public void setpWeekEntrance02(Point pWeekEntrance02) {
		this.pWeekEntrance02 = pWeekEntrance02;
	}

	public Point getpRoomSelect01() {
		return pRoomSelect01;
	}

	public void setpRoomSelect01(Point pRoomSelect01) {
		this.pRoomSelect01 = pRoomSelect01;
	}

	public Point getpDailySliceStrat() {
		return pDailySliceStrat;
	}

	public void setpDailySliceStrat(Point pDailySliceStrat) {
		this.pDailySliceStrat = pDailySliceStrat;
	}

	public Point getpDailySliceEnd() {
		return pDailySliceEnd;
	}

	public void setpDailySliceEnd(Point pDailySliceEnd) {
		this.pDailySliceEnd = pDailySliceEnd;
	}

	public Point getpDailyEntrance() {
		return pDailyEntrance;
	}

	public void setpDailyEntrance(Point pDailyEntrance) {
		this.pDailyEntrance = pDailyEntrance;
	}

	public Point getpTrainAll() {
		return pTrainAll;
	}

	public void setpTrainAll(Point pTrainAll) {
		this.pTrainAll = pTrainAll;
	}

	public Point getpTrainAp10() {
		return pTrainAp10;
	}

	public void setpTrainAp10(Point pTrainAp10) {
		this.pTrainAp10 = pTrainAp10;
	}

	public Point getpTrainAp20() {
		return pTrainAp20;
	}

	public void setpTrainAp20(Point pTrainAp20) {
		this.pTrainAp20 = pTrainAp20;
	}

	public Point getpTrainAp30() {
		return pTrainAp30;
	}

	public void setpTrainAp30(Point pTrainAp30) {
		this.pTrainAp30 = pTrainAp30;
	}

	public Point getpTrainAp40() {
		return pTrainAp40;
	}

	public void setpTrainAp40(Point pTrainAp40) {
		this.pTrainAp40 = pTrainAp40;
	}

	public Point getpQpAll() {
		return pQpAll;
	}

	public void setpQpAll(Point pQpAll) {
		this.pQpAll = pQpAll;
	}

	public Point getpQpAp10() {
		return pQpAp10;
	}

	public void setpQpAp10(Point pQpAp10) {
		this.pQpAp10 = pQpAp10;
	}

	public Point getpQpAp20() {
		return pQpAp20;
	}

	public void setpQpAp20(Point pQpAp20) {
		this.pQpAp20 = pQpAp20;
	}

	public Point getpQpAp30() {
		return pQpAp30;
	}

	public void setpQpAp30(Point pQpAp30) {
		this.pQpAp30 = pQpAp30;
	}

	public Point getpQpAp40() {
		return pQpAp40;
	}

	public void setpQpAp40(Point pQpAp40) {
		this.pQpAp40 = pQpAp40;
	}

	public Point getpExpAll() {
		return pExpAll;
	}

	public void setpExpAll(Point pExpAll) {
		this.pExpAll = pExpAll;
	}

	public Point getpExpAp10() {
		return pExpAp10;
	}

	public void setpExpAp10(Point pExpAp10) {
		this.pExpAp10 = pExpAp10;
	}

	public Point getpExpAp20() {
		return pExpAp20;
	}

	public void setpExpAp20(Point pExpAp20) {
		this.pExpAp20 = pExpAp20;
	}

	public Point getpExpAp30() {
		return pExpAp30;
	}

	public void setpExpAp30(Point pExpAp30) {
		this.pExpAp30 = pExpAp30;
	}

	public Point getpExpAp40() {
		return pExpAp40;
	}

	public void setpExpAp40(Point pExpAp40) {
		this.pExpAp40 = pExpAp40;
	}

	public Point getpNoticeExit() {
		return pNoticeExit;
	}

	public void setpNoticeExit(Point pNoticeExit) {
		this.pNoticeExit = pNoticeExit;
	}

	public Color getcNoticeExit() {
		return cNoticeExit;
	}

	public void setcNoticeExit(Color cNoticeExit) {
		this.cNoticeExit = cNoticeExit;
	}

	public Point getpNoticeExitDark() {
		return pNoticeExitDark;
	}

	public void setpNoticeExitDark(Point pNoticeExitDark) {
		this.pNoticeExitDark = pNoticeExitDark;
	}

	public Color getcNoticeExitDark() {
		return cNoticeExitDark;
	}

	public void setcNoticeExitDark(Color cNoticeExitDark) {
		this.cNoticeExitDark = cNoticeExitDark;
	}

	public Point getpBack() {
		return pBack;
	}

	public void setpBack(Point pBack) {
		this.pBack = pBack;
	}

	public Point getpServantAll() {
		return pServantAll;
	}

	public void setpServantAll(Point pServantAll) {
		this.pServantAll = pServantAll;
	}

	public Point getpServantSaber() {
		return pServantSaber;
	}

	public void setpServantSaber(Point pServantSaber) {
		this.pServantSaber = pServantSaber;
	}

	public Point getpServantArcher() {
		return pServantArcher;
	}

	public void setpServantArcher(Point pServantArcher) {
		this.pServantArcher = pServantArcher;
	}

	public Point getpServantLancer() {
		return pServantLancer;
	}

	public void setpServantLancer(Point pServantLancer) {
		this.pServantLancer = pServantLancer;
	}

	public Point getpServantRider() {
		return pServantRider;
	}

	public void setpServantRider(Point pServantRider) {
		this.pServantRider = pServantRider;
	}

	public Point getpServantCaster() {
		return pServantCaster;
	}

	public void setpServantCaster(Point pServantCaster) {
		this.pServantCaster = pServantCaster;
	}

	public Point getpServantAssasin() {
		return pServantAssasin;
	}

	public void setpServantAssasin(Point pServantAssasin) {
		this.pServantAssasin = pServantAssasin;
	}

	public Point getpServantBasker() {
		return pServantBasker;
	}

	public void setpServantBasker(Point pServantBasker) {
		this.pServantBasker = pServantBasker;
	}

	public Point getpServantFour() {
		return pServantFour;
	}

	public void setpServantFour(Point pServantFour) {
		this.pServantFour = pServantFour;
	}

	public Point getpServantMix() {
		return pServantMix;
	}

	public void setpServantMix(Point pServantMix) {
		this.pServantMix = pServantMix;
	}

	public Point getpRewardGet() {
		return pRewardGet;
	}

	public void setpRewardGet(Point pRewardGet) {
		this.pRewardGet = pRewardGet;
	}

	public Color getcRewardGet() {
		return cRewardGet;
	}

	public void setcRewardGet(Color cRewardGet) {
		this.cRewardGet = cRewardGet;
	}

	public Point getpRewardAction() {
		return pRewardAction;
	}

	public void setpRewardAction(Point pRewardAction) {
		this.pRewardAction = pRewardAction;
	}

	public Color getcRewardAction() {
		return cRewardAction;
	}

	public void setcRewardAction(Color cRewardAction) {
		this.cRewardAction = cRewardAction;
	}

	public Point getpReset() {
		return pReset;
	}

	public void setpReset(Point pReset) {
		this.pReset = pReset;
	}

	public Point getpBlueAttack() {
		return pBlueAttack;
	}

	public void setpBlueAttack(Point pBlueAttack) {
		this.pBlueAttack = pBlueAttack;
	}

	public Color getcBlueAttack() {
		return cBlueAttack;
	}

	public void setcBlueAttack(Color cBlueAttack) {
		this.cBlueAttack = cBlueAttack;
	}

	public Point getpFetter01() {
		return pFetter01;
	}

	public void setpFetter01(Point pFetter01) {
		this.pFetter01 = pFetter01;
	}

	public Color getcFetter01() {
		return cFetter01;
	}

	public void setcFetter01(Color cFetter01) {
		this.cFetter01 = cFetter01;
	}

	public Point getpFetter02() {
		return pFetter02;
	}

	public void setpFetter02(Point pFetter02) {
		this.pFetter02 = pFetter02;
	}

	public Color getcFetter02() {
		return cFetter02;
	}

	public void setcFetter02(Color cFetter02) {
		this.cFetter02 = cFetter02;
	}

	public Point getpCloseMd() {
		return pCloseMd;
	}

	public void setpCloseMd(Point pCloseMd) {
		this.pCloseMd = pCloseMd;
	}

	public Color getcCloseMd() {
		return cCloseMd;
	}

	public void setcCloseMd(Color cCloseMd) {
		this.cCloseMd = cCloseMd;
	}

	public Point getpGetFriendNo() {
		return pGetFriendNo;
	}

	public void setpGetFriendNo(Point pGetFriendNo) {
		this.pGetFriendNo = pGetFriendNo;
	}

	public Color getcGetFriendNo() {
		return cGetFriendNo;
	}

	public void setcGetFriendNo(Color cGetFriendNo) {
		this.cGetFriendNo = cGetFriendNo;
	}

	public Point getpConfirmRd() {
		return pConfirmRd;
	}

	public void setpConfirmRd(Point pConfirmRd) {
		this.pConfirmRd = pConfirmRd;
	}

	public Color getcConfirmRd() {
		return cConfirmRd;
	}

	public void setcConfirmRd(Color cConfirmRd) {
		this.cConfirmRd = cConfirmRd;
	}

	public Point getpGuda() {
		return pGuda;
	}

	public void setpGuda(Point pGuda) {
		this.pGuda = pGuda;
	}

	public Color getcGuda() {
		return cGuda;
	}

	public void setcGuda(Color cGuda) {
		this.cGuda = cGuda;
	}

	public Point getpGudaDark() {
		return pGudaDark;
	}

	public void setpGudaDark(Point pGudaDark) {
		this.pGudaDark = pGudaDark;
	}

	public Color getcGudaDark() {
		return cGudaDark;
	}

	public void setcGudaDark(Color cGudaDark) {
		this.cGudaDark = cGudaDark;
	}

	public Point getpDownPanelClose() {
		return pDownPanelClose;
	}

	public void setpDownPanelClose(Point pDownPanelClose) {
		this.pDownPanelClose = pDownPanelClose;
	}

	public Color getcDownPanelClose() {
		return cDownPanelClose;
	}

	public void setcDownPanelClose(Color cDownPanelClose) {
		this.cDownPanelClose = cDownPanelClose;
	}

	public Point getDeadPoint() {
		return deadPoint;
	}

	public void setDeadPoint(Point deadPoint) {
		this.deadPoint = deadPoint;
	}

	public Point getpLevelUp() {
		return pLevelUp;
	}

	public void setpLevelUp(Point pLevelUp) {
		this.pLevelUp = pLevelUp;
	}

	public Color getcLevelUp() {
		return cLevelUp;
	}

	public void setcLevelUp(Color cLevelUp) {
		this.cLevelUp = cLevelUp;
	}

	public Point getpCardExit() {
		return pCardExit;
	}

	public void setpCardExit(Point pCardExit) {
		this.pCardExit = pCardExit;
	}

	public Color getcCardExit() {
		return cCardExit;
	}

	public void setcCardExit(Color cCardExit) {
		this.cCardExit = cCardExit;
	}

	public Point getpNpDtExit() {
		return pNpDtExit;
	}

	public void setpNpDtExit(Point pNpDtExit) {
		this.pNpDtExit = pNpDtExit;
	}

	public Color getcNpDtExit() {
		return cNpDtExit;
	}

	public void setcNpDtExit(Color cNpDtExit) {
		this.cNpDtExit = cNpDtExit;
	}

	public Point getpMosterDtExit() {
		return pMosterDtExit;
	}

	public void setpMosterDtExit(Point pMosterDtExit) {
		this.pMosterDtExit = pMosterDtExit;
	}

	public Color getcMosterDtExit() {
		return cMosterDtExit;
	}

	public void setcMosterDtExit(Color cMosterDtExit) {
		this.cMosterDtExit = cMosterDtExit;
	}

	public Point getpLeftTop() {
		return pLeftTop;
	}

	public void setpLeftTop(Point pLeftTop) {
		this.pLeftTop = pLeftTop;
	}

	public Color getcLeftTop() {
		return cLeftTop;
	}

	public void setcLeftTop(Color cLeftTop) {
		this.cLeftTop = cLeftTop;
	}

	public Point getpButtonRestartNo() {
		return pButtonRestartNo;
	}

	public void setpButtonRestartNo(Point pButtonRestartNo) {
		this.pButtonRestartNo = pButtonRestartNo;
	}

	public Color getcButtonRestartNo() {
		return cButtonRestartNo;
	}

	public void setcButtonRestartNo(Color cButtonRestartNo) {
		this.cButtonRestartNo = cButtonRestartNo;
	}

	public Point getpButtonRestartYes() {
		return pButtonRestartYes;
	}

	public void setpButtonRestartYes(Point pButtonRestartYes) {
		this.pButtonRestartYes = pButtonRestartYes;
	}

	public Color getcButtonRestartYes() {
		return cButtonRestartYes;
	}

	public void setcButtonRestartYes(Color cButtonRestartYes) {
		this.cButtonRestartYes = cButtonRestartYes;
	}

	public Point getpButtonFailBack() {
		return pButtonFailBack;
	}

	public void setpButtonFailBack(Point pButtonFailBack) {
		this.pButtonFailBack = pButtonFailBack;
	}

	public Color getcButtonFailBack() {
		return cButtonFailBack;
	}

	public void setcButtonFailBack(Color cButtonFailBack) {
		this.cButtonFailBack = cButtonFailBack;
	}

	public Point getpButtonFailBackYes() {
		return pButtonFailBackYes;
	}

	public void setpButtonFailBackYes(Point pButtonFailBackYes) {
		this.pButtonFailBackYes = pButtonFailBackYes;
	}

	public Color getcButtonFailBackYes() {
		return cButtonFailBackYes;
	}

	public void setcButtonFailBackYes(Color cButtonFailBackYes) {
		this.cButtonFailBackYes = cButtonFailBackYes;
	}

	public Point getpButtonFailBackNo() {
		return pButtonFailBackNo;
	}

	public void setpButtonFailBackNo(Point pButtonFailBackNo) {
		this.pButtonFailBackNo = pButtonFailBackNo;
	}

	public Color getcButtonFailBackNo() {
		return cButtonFailBackNo;
	}

	public void setcButtonFailBackNo(Color cButtonFailBackNo) {
		this.cButtonFailBackNo = cButtonFailBackNo;
	}

	public Point getpAppleNeed01() {
		return pAppleNeed01;
	}

	public void setpAppleNeed01(Point pAppleNeed01) {
		this.pAppleNeed01 = pAppleNeed01;
	}

	public Color getcAppleNeed01() {
		return cAppleNeed01;
	}

	public void setcAppleNeed01(Color cAppleNeed01) {
		this.cAppleNeed01 = cAppleNeed01;
	}

	public Point getpAppleNeed02() {
		return pAppleNeed02;
	}

	public void setpAppleNeed02(Point pAppleNeed02) {
		this.pAppleNeed02 = pAppleNeed02;
	}

	public Color getcAppleNeed02() {
		return cAppleNeed02;
	}

	public void setcAppleNeed02(Color cAppleNeed02) {
		this.cAppleNeed02 = cAppleNeed02;
	}

	public Point getpConnectYes() {
		return pConnectYes;
	}

	public void setpConnectYes(Point pConnectYes) {
		this.pConnectYes = pConnectYes;
	}

	public Color getcConnectYes() {
		return cConnectYes;
	}

	public void setcConnectYes(Color cConnectYes) {
		this.cConnectYes = cConnectYes;
	}

	public Point getpConnectEnd() {
		return pConnectEnd;
	}

	public void setpConnectEnd(Point pConnectEnd) {
		this.pConnectEnd = pConnectEnd;
	}

	public Color getcConnectEnd() {
		return cConnectEnd;
	}

	public void setcConnectEnd(Color cConnectEnd) {
		this.cConnectEnd = cConnectEnd;
	}

	public Point getpReConnectYes() {
		return pReConnectYes;
	}

	public void setpReConnectYes(Point pReConnectYes) {
		this.pReConnectYes = pReConnectYes;
	}

	public Color getcReConnectYes() {
		return cReConnectYes;
	}

	public void setcReConnectYes(Color cReConnectYes) {
		this.cReConnectYes = cReConnectYes;
	}

	public Point getpUpdate() {
		return pUpdate;
	}

	public void setpUpdate(Point pUpdate) {
		this.pUpdate = pUpdate;
	}

	public Color getcUpdate() {
		return cUpdate;
	}

	public void setcUpdate(Color cUpdate) {
		this.cUpdate = cUpdate;
	}

	public Point getpUpdateNo() {
		return pUpdateNo;
	}

	public void setpUpdateNo(Point pUpdateNo) {
		this.pUpdateNo = pUpdateNo;
	}

	public Color getcUpdateNo() {
		return cUpdateNo;
	}

	public void setcUpdateNo(Color cUpdateNo) {
		this.cUpdateNo = cUpdateNo;
	}

	public Point getpHand() {
		return pHand;
	}

	public void setpHand(Point pHand) {
		this.pHand = pHand;
	}

	public Color getcHand() {
		return cHand;
	}

	public void setcHand(Color cHand) {
		this.cHand = cHand;
	}

	public Point getpSupport() {
		return pSupport;
	}

	public void setpSupport(Point pSupport) {
		this.pSupport = pSupport;
	}

	public Point[] getpSupports() {
		return pSupports;
	}

	public void setpSupports(Point[] pSupports) {
		this.pSupports = pSupports;
	}

	public Point getpCardColor() {
		return pCardColor;
	}

	public void setpCardColor(Point pCardColor) {
		this.pCardColor = pCardColor;
	}

	public Point getpCardWeak() {
		return pCardWeak;
	}

	public void setpCardWeak(Point pCardWeak) {
		this.pCardWeak = pCardWeak;
	}

	public Point getpCardClick() {
		return pCardClick;
	}

	public void setpCardClick(Point pCardClick) {
		this.pCardClick = pCardClick;
	}

	public Point getpFetterUp() {
		return pFetterUp;
	}

	public void setpFetterUp(Point pFetterUp) {
		this.pFetterUp = pFetterUp;
	}

	public Color getcFetterUp() {
		return cFetterUp;
	}

	public void setcFetterUp(Color cFetterUp) {
		this.cFetterUp = cFetterUp;
	}

	public Point getpLtApp() {
		return pLtApp;
	}

	public void setpLtApp(Point pLtApp) {
		this.pLtApp = pLtApp;
	}

	public Color getcLtApp() {
		return cLtApp;
	}

	public void setcLtApp(Color cLtApp) {
		this.cLtApp = cLtApp;
	}

	public Point getpMygame() {
		return pMygame;
	}

	public void setpMygame(Point pMygame) {
		this.pMygame = pMygame;
	}

	public Color getcMygame() {
		return cMygame;
	}

	public void setcMygame(Color cMygame) {
		this.cMygame = cMygame;
	}

	public Point getpNoSupport() {
		return pNoSupport;
	}

	public void setpNoSupport(Point pNoSupport) {
		this.pNoSupport = pNoSupport;
	}

	public Color getcNoSupport() {
		return cNoSupport;
	}

	public void setcNoSupport(Color cNoSupport) {
		this.cNoSupport = cNoSupport;
	}

	public Point getpLoading() {
		return pLoading;
	}

	public void setpLoading(Point pLoading) {
		this.pLoading = pLoading;
	}

	public Color getcLoading() {
		return cLoading;
	}

	public void setcLoading(Color cLoading) {
		this.cLoading = cLoading;
	}

	public Point getpTransfer() {
		return pTransfer;
	}

	public void setpTransfer(Point pTransfer) {
		this.pTransfer = pTransfer;
	}

	public Color getcTransfer() {
		return cTransfer;
	}

	public void setcTransfer(Color cTransfer) {
		this.cTransfer = cTransfer;
	}

	public Point getpSupportUpdate() {
		return pSupportUpdate;
	}

	public void setpSupportUpdate(Point pSupportUpdate) {
		this.pSupportUpdate = pSupportUpdate;
	}

	public Color getcSupportUpdate() {
		return cSupportUpdate;
	}

	public void setcSupportUpdate(Color cSupportUpdate) {
		this.cSupportUpdate = cSupportUpdate;
	}

	public Point getpSupportUpdateYes() {
		return pSupportUpdateYes;
	}

	public void setpSupportUpdateYes(Point pSupportUpdateYes) {
		this.pSupportUpdateYes = pSupportUpdateYes;
	}

	public Color getcSupportUpdateYes() {
		return cSupportUpdateYes;
	}

	public void setcSupportUpdateYes(Color cSupportUpdateYes) {
		this.cSupportUpdateYes = cSupportUpdateYes;
	}

	public Point getpSupportNoConfirm() {
		return pSupportNoConfirm;
	}

	public void setpSupportNoConfirm(Point pSupportNoConfirm) {
		this.pSupportNoConfirm = pSupportNoConfirm;
	}

	public Color getcSupportNoConfirm() {
		return cSupportNoConfirm;
	}

	public void setcSupportNoConfirm(Color cSupportNoConfirm) {
		this.cSupportNoConfirm = cSupportNoConfirm;
	}

	public Point getpSkill01() {
		return pSkill01;
	}

	public void setpSkill01(Point pSkill01) {
		this.pSkill01 = pSkill01;
	}

	public Point getpSkill02() {
		return pSkill02;
	}

	public void setpSkill02(Point pSkill02) {
		this.pSkill02 = pSkill02;
	}

	public Point getpSkill03() {
		return pSkill03;
	}

	public void setpSkill03(Point pSkill03) {
		this.pSkill03 = pSkill03;
	}

	public Point getpCloth() {
		return pCloth;
	}

	public void setpCloth(Point pCloth) {
		this.pCloth = pCloth;
	}

	public Point getpClothSkill01() {
		return pClothSkill01;
	}

	public void setpClothSkill01(Point pClothSkill01) {
		this.pClothSkill01 = pClothSkill01;
	}

	public Point getpClothSkill02() {
		return pClothSkill02;
	}

	public void setpClothSkill02(Point pClothSkill02) {
		this.pClothSkill02 = pClothSkill02;
	}

	public Point getpClothSkill03() {
		return pClothSkill03;
	}

	public void setpClothSkill03(Point pClothSkill03) {
		this.pClothSkill03 = pClothSkill03;
	}

	public Point getNpCheck() {
		return npCheck;
	}

	public void setNpCheck(Point npCheck) {
		this.npCheck = npCheck;
	}

	public Point getNpNp() {
		return npNp;
	}

	public void setNpNp(Point npNp) {
		this.npNp = npNp;
	}

	public Point getpClothToSkill() {
		return pClothToSkill;
	}

	public void setpClothToSkill(Point pClothToSkill) {
		this.pClothToSkill = pClothToSkill;
	}

	public Color getcClothToSkill() {
		return cClothToSkill;
	}

	public void setcClothToSkill(Color cClothToSkill) {
		this.cClothToSkill = cClothToSkill;
	}

	public Point getpMoster01() {
		return pMoster01;
	}

	public void setpMoster01(Point pMoster01) {
		this.pMoster01 = pMoster01;
	}

	public Point getpMoster02() {
		return pMoster02;
	}

	public void setpMoster02(Point pMoster02) {
		this.pMoster02 = pMoster02;
	}

	public Point getpMoster03() {
		return pMoster03;
	}

	public void setpMoster03(Point pMoster03) {
		this.pMoster03 = pMoster03;
	}

	public Point getSkillPerson01() {
		return skillPerson01;
	}

	public void setSkillPerson01(Point skillPerson01) {
		this.skillPerson01 = skillPerson01;
	}

	public Point getSkillPerson02() {
		return skillPerson02;
	}

	public void setSkillPerson02(Point skillPerson02) {
		this.skillPerson02 = skillPerson02;
	}

	public Point getSkillPerson03() {
		return skillPerson03;
	}

	public void setSkillPerson03(Point skillPerson03) {
		this.skillPerson03 = skillPerson03;
	}

	public Point getpEventScrollLocate01() {
		return pEventScrollLocate01;
	}

	public void setpEventScrollLocate01(Point pEventScrollLocate01) {
		this.pEventScrollLocate01 = pEventScrollLocate01;
	}

	public Point getpEventBattleLocate01() {
		return pEventBattleLocate01;
	}

	public void setpEventBattleLocate01(Point pEventBattleLocate01) {
		this.pEventBattleLocate01 = pEventBattleLocate01;
	}

	public Point getpFetter03() {
		return pFetter03;
	}

	public void setpFetter03(Point pFetter03) {
		this.pFetter03 = pFetter03;
	}

	public Color getcFetter03() {
		return cFetter03;
	}

	public void setcFetter03(Color cFetter03) {
		this.cFetter03 = cFetter03;
	}

	public Point getpWeekEntrance03() {
		return pWeekEntrance03;
	}

	public void setpWeekEntrance03(Point pWeekEntrance03) {
		this.pWeekEntrance03 = pWeekEntrance03;
	}

	public Point getpBattleReapeatYes() {
		return pBattleReapeatYes;
	}

	public void setpBattleReapeatYes(Point pBattleReapeatYes) {
		this.pBattleReapeatYes = pBattleReapeatYes;
	}

	public Color getcBattleReapeatYes() {
		return cBattleReapeatYes;
	}

	public void setcBattleReapeatYes(Color cBattleReapeatYes) {
		this.cBattleReapeatYes = cBattleReapeatYes;
	}

	public Point getpBattleReapeatNo() {
		return pBattleReapeatNo;
	}

	public void setpBattleReapeatNo(Point pBattleReapeatNo) {
		this.pBattleReapeatNo = pBattleReapeatNo;
	}

	public Color getcBattleReapeatNo() {
		return cBattleReapeatNo;
	}

	public void setcBattleReapeatNo(Color cBattleReapeatNo) {
		this.cBattleReapeatNo = cBattleReapeatNo;
	}

	public static void main(String[] args) {
		PointInfo pi = new PointInfo();
		pi.setpBattleReapeatYes(new Point(0,0));
		pi.setpBattleReapeatNo(new Point(0,0));
		pi.setcBattleReapeatYes(new Color(0,0,0));
		pi.setcBattleReapeatNo(new Color(0,0,0));
		String text = JSON.toJSONString(pi);
		System.out.println(text);
	}

}
