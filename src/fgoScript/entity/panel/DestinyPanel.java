package fgoScript.entity.panel;

import alarm.ZTextField;
import com.melloware.jintellitype.JIntellitype;
import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.ProcessDealUtil;
import destinychild.DaillyMission;
import destinychild.IModule;
import destinychild.IRaid;
import destinychild.entity.SimoLocation;
import fgoScript.entity.BaseZButton;
import fgoScript.entity.Gudazi;
import fgoScript.entity.Zpanel;
import fgoScript.entity.panel.inte.PanelModel;
import fgoScript.service.TimerManager;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName DestinyPanel.java
 * @Description only destiny
 * @createTime 2019年12月15日 15:38:00
 */
public class DestinyPanel extends Zpanel implements PanelModel {
    public DestinyPanel() {
        initP();
    }
    private IModule light;
    private IRaid raid;
    private DaillyMission daillyMission;
    @Override
    public void initP() {
        // 百鬼夜行按钮
        BaseZButton ghostButton = new BaseZButton(null, 0,"百鬼夜行(ALT+F)", JIntellitype.MOD_ALT, (int) 'F',false, false, BaseZButton.pink) {
            private static final long serialVersionUID = -7389326247723796445L;
            @Override
            public void runMethod() {
                light.toggle();
            }
        };
        // raid按钮
        BaseZButton raidButton = new BaseZButton(null, 0,"raid(ALT+R)",JIntellitype.MOD_ALT, (int) 'R',false, false, BaseZButton.pink) {
            private static final long serialVersionUID = -7389326247723796445L;
            @Override
            public void runMethod() {
                raid.toggle();
            }
        };
        // 日常任务按钮
        BaseZButton daillyMissionButton = new BaseZButton(null, 0,"日常任务(ALT+D)",JIntellitype.MOD_ALT, (int) 'D',false, false, BaseZButton.pink) {
            private static final long serialVersionUID = -7389326247723796445L;
            @Override
            public void runMethod() {
                daillyMission.toggle();
            }
        };
        // 模拟器定位按钮
        BaseZButton simoLocateButton = new BaseZButton(null, 0,"模拟器定位(ALT+L)",JIntellitype.MOD_ALT, (int) 'L',false, false, BaseZButton.pink) {
            private static final long serialVersionUID = -7389326247723796445L;
            @Override
            public void runMethod() {
                SimoLocation.getInstance().locateOriginalPoint();
            }
        };
        // 定时text
        ZTextField daillyMissionClockZt = new ZTextField();
        daillyMissionClockZt.setText("03:05");
        // 账号text
        ZTextField accountZt =  new ZTextField("0", "dcOpen_" + NativeCp.getUserName(), Constant.DC + "/", "account");
        daillyMissionClockZt.setText("03:05");
        // 定时启动日常任务按钮
        BaseZButton daillyMissionClockButton = new BaseZButton(null, 0,"启动定时日常任务",JIntellitype.MOD_SHIFT, (int) 'T',true, false, BaseZButton.pink) {
            private static final long serialVersionUID = 2069565531679104865L;
            @Override
            public void runMethod() throws Exception {
                String text = this.getText();
                if (text.contains("启动")){
                    daillyMissionButton.doClick();
                    TimerManager.startTaskByTime(daillyMissionClockZt.getText(), daillyMissionButton, "daillyMission");
                    this.setText(text.replace("启动", "关闭").substring(0,text.indexOf("(")));
                    this.setExcuteColor();
                }else{
                    TimerManager.stopTask("daillyMission");
                    this.setText(this.getText().replace("关闭","启动").substring(0,text.indexOf("(")));
                }
            }
        };
        //打开dc按钮
        BaseZButton openDcBtn = new BaseZButton(null, 0,"开启账号",JIntellitype.MOD_SHIFT, (int) 'O',true, false, BaseZButton.pink) {
            private static final long serialVersionUID = 432750026975346042L;
            @Override
            public void runMethod() throws Exception {
                daillyMission.openDC(Integer.valueOf(accountZt.getText()));
            }
        };
        this.setOpaque(false);  int len = 2;
        daillyMissionClockButton.setBounds(5, 5 + (4 * 32), 160, 27);
        daillyMissionClockZt.setBounds(165, 5 + (4 * 32), 160, 27);
        ghostButton.setBounds(5, 5, 320, 25+len);
        raidButton.setBounds(5, 5 + (1 * (30+len)), 320, 25+len);
        daillyMissionButton.setBounds(5, 5 + (2 * (30+len)), 320, 25+len);
        simoLocateButton.setBounds(5, 5 + (3 * (30+len)), 320, 25+len);
        openDcBtn.setBounds(5, 5 + (5 * (30+len)), 80, 25+len);
        accountZt.setBounds(85, 5 + (5 * (30+len)), 240, 25+len);

        ghostButton.setAllowRepeat(true);
        raidButton.setAllowRepeat(true);
        daillyMissionButton.setAllowRepeat(true);
        simoLocateButton.setAllowRepeat(true);

        this.setLayout(null);
        this.setVisible(false);
        this.setBounds(0, 0, 335, 518);
        this.add(ghostButton);
        this.add(raidButton);
        this.add(daillyMissionButton);
        this.add(simoLocateButton);
        this.add(openDcBtn);
        this.add(accountZt);
        this.add(daillyMissionClockZt);
        this.add(daillyMissionClockButton);
    }
    public void setLight(IModule light) {
        this.light = light;
    }
    public void setRaid(IRaid raid) {
        this.raid = raid;
    }
    public void setDaillyMission(DaillyMission daillyMission) {
        this.daillyMission = daillyMission;
    }
}
