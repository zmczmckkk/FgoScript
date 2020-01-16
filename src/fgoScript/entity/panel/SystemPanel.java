package fgoScript.entity.panel;

import alarm.ZLable;
import alarm.ZTextField;
import com.melloware.jintellitype.JIntellitype;
import commons.entity.Constant;
import commons.entity.FxWindow;
import commons.entity.NativeCp;
import commons.util.FileUtils;
import commons.util.ProcessDealUtil;
import commons.util.PropertiesUtil;
import fgoScript.entity.BaseZButton;
import fgoScript.entity.Zpanel;
import fgoScript.entity.panel.inte.PanelModel;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName SystemPanel.java
 * @Description 系统面板
 * @createTime 2020年01月16日 03:47:00
 */
public class SystemPanel extends Zpanel implements PanelModel {
    public SystemPanel() {
        initP();
    }

    @Override
    public void initP() {
        int len = 2;
        //自动关机按钮
        BaseZButton ShutdownButton = new BaseZButton(null, 0,"定时关机", JIntellitype.MOD_SHIFT, (int) 'S',true, false, BaseZButton.pink) {
            private static final long serialVersionUID = 53933841194261802L;
            @Override
            public void runMethod() throws Exception {
                int minutes = Integer.valueOf(PropertiesUtil.getValueFromSystemFile("minutes"));
                ProcessDealUtil.closeComputerInTime(minutes);
                this.setText("定时关机");
            }
        };
        //取消自动关机按钮
        BaseZButton abortShutdownButton = new BaseZButton(null, 0,"取消",JIntellitype.MOD_SHIFT, (int) 'S',true, false, BaseZButton.pink) {
            private static final long serialVersionUID = 53933841194261802L;
            @Override
            public void runMethod() throws Exception {
                ProcessDealUtil.abordCloseComputer();
                this.setText("取消");
            }
        };
        //关机时间设置输入框
        ZTextField closeTimeZt = new ZTextField("0", "system_" + NativeCp.getUserName(),"", "minutes");
        //输入框
        ZTextField tf1, tf2, tf3;
        tf1 = new ZTextField("请选择路径", "system_" + NativeCp.getUserName(), "", "renamePath");
        tf2 = new ZTextField();
        tf3 = new ZTextField();

        tf1.setBounds(85, 5 + (1 * 32), 240, 27);
        tf2.setBounds(85, 5 + (2 * 32), 120, 27);
        tf3.setBounds(210, 5 + (2 * 32), 115, 27);
        //修改路径按钮
        BaseZButton renameFileBt = new BaseZButton(null, 0,"修改路径",JIntellitype.MOD_SHIFT, (int) 'P',true, true, BaseZButton.pink) {
            private static final long serialVersionUID = 6504858991507730448L;
            @Override
            public void runMethod() {
//                String dir = System.getenv("USERPROFILE");
//                DirectoryChooser jc = new DirectoryChooser();
//                jc.setTitle("请选择路径");
//                Stage s = new Stage();
//                File chosenDir = jc.showDialog(FxWindow.fxWindow.);
//                if (chosenDir != null) {
//                    tf1.setText(chosenDir.getAbsolutePath());
//                } else {
//                    System.out.print("no directory chosen");
//                }

                String dir = System.getenv("USERPROFILE");
                JFileChooser jc = new JFileChooser();
                jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jc.setCurrentDirectory(new File(dir));
                int state = jc.showOpenDialog(this);
                if (JFileChooser.APPROVE_OPTION == state){
                    tf1.setText(jc.getSelectedFile().getAbsolutePath());
                }else {
                    return;
                }
            }
        };
        //修改路径按钮
        BaseZButton renameAllFileBt = new BaseZButton(null, 0,"执行修改",JIntellitype.MOD_SHIFT, (int) 'P',true, true, BaseZButton.pink) {
            private static final long serialVersionUID = 6504858991507730448L;
            @Override
            public void runMethod() {
                FileUtils.recursiveTraversalFolder(tf1.getText(),tf2.getText(),tf3.getText());
            }
        };
        renameFileBt.setBounds(5, 5 + (1 * 32), 80, 27);
        renameAllFileBt.setBounds(5, 5 + (2 * 32), 80, 27);


        closeTimeZt.setBounds(135, 5, 190, 25+len);
        ShutdownButton.setBounds(5, 5, 80, 25+len);
        abortShutdownButton.setBounds(90, 5, 40, 25+len);

        this.add(ShutdownButton);
        this.add(abortShutdownButton);
        this.add(closeTimeZt);
        this.add(renameFileBt);
        this.add(renameAllFileBt);
        this.add(tf1);
        this.add(tf2);
        this.add(tf3);

        this.setLayout(null);
        this.setOpaque(false);
        this.setVisible(false);
        this.setBounds(0, 0, 335, 518);
    }
}
