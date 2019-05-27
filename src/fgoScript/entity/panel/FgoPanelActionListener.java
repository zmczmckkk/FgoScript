package fgoScript.entity.panel;

import com.melloware.jintellitype.JIntellitype;
import fgoScript.entity.BaseZButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @description: 界面按钮监听类,点击按钮后执行哪些内容,如果是热键类型的按钮，将会注册快捷键。
 * @author: RENZHEHAO
 * @create: 2019-05-22 04:44
 **/
public class FgoPanelActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        BaseZButton btTemp = (BaseZButton) e.getSource();
        if (e.getActionCommand().contains(btTemp.getText())) {
            btTemp.setEnabled(btTemp.isEnableStatus());
            if (btTemp.isExcuteble()) {
                btTemp.setExcuteColor();
                btTemp.setExcutebleText();
                new Thread(btTemp).start();
            }else {
                if ("点击设置".equals(btTemp.getText())) {
                    btTemp.setText("选择条件");
                }else {
                    btTemp.setText(btTemp.getText()+" (已激活)");
                }
                btTemp.setActiveColor();
                JIntellitype.getInstance().registerHotKey(btTemp.getMarkCode(), btTemp.getShortcunt01(), btTemp.getShortcunt02());
            }
        }
    }
}
