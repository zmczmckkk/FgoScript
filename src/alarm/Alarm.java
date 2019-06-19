package alarm;

import commons.entity.NativeCp;
import fgoScript.entity.BaseZButton;
import fgoScript.entity.Zpanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @description: TODO
 * @author: RENZHEHAO
 * @create: 2019-06-17 07:37
 **/
public class Alarm {
    private static final Logger LOGGER = LogManager.getLogger(Alarm.class);
    private JFrame alarmFrame;
    ZLable label1, label2, label3, label4, label5;
    ZTextField tf1, tf2, tf3, tf4, tf5;
    ZButton bt1, bt2, bt3;
    AlarmTimer alTimer = null;
    private static Alarm alm;
    public static Alarm instance(){
        if (alm == null) {
            alm = new Alarm();
        }
        return alm;
    }
    public Zpanel getAlarmPanel(){
        Zpanel p = new Zpanel();
        p.setLayout(null);
        p.setOpaque(false);
        label1 = new ZLable("  闹钟时间（HH:mm）");
        label2 = new ZLable("  闹钟音乐（wav）");
        label3 = new ZLable("  响铃间隔（分钟）");
        label4 = new ZLable("  响铃次数");
        label5 = new ZLable("  定时时间（HH:mm）");
        tf1 = new ZTextField();
        tf2 = new ZTextField();
        tf3 = new ZTextField();
        tf4 = new ZTextField();
        tf5 = new ZTextField();
        tf1.setText("07:00");
        tf2.setText(NativeCp.getUserDir() + "\\naoling.wav");
        tf3.setText("1");
        tf4.setText("5");
        tf5.setText("00:05");
        bt1 = new ZButton("启动闹钟", BaseZButton.pink);
        bt3 = new ZButton("启动定时", BaseZButton.pink);
        bt2 = new ZButton("稍后再响", BaseZButton.pink);
        bt2.setVisible(false);
        bt1.addActionListener(getAlarmActionListener());
        bt2.addActionListener(getAlarmLaterActionListener());
        bt3.addActionListener(getAlarmCountActionListener(bt3));
        label1.setBounds(5, 5 + (0 * 32), 160, 27);
        label5.setBounds(5, 5 + (1 * 32), 160, 27);
        label2.setBounds(5, 5 + (2 * 32), 160, 27);
        label3.setBounds(5, 5 + (3 * 32), 160, 27);
        label4.setBounds(5, 5 + (4 * 32), 160, 27);

        tf1.setBounds(165, 5 + (0 * 32), 160, 27);
        tf5.setBounds(165, 5 + (1 * 32), 160, 27);
        tf2.setBounds(165, 5 + (2 * 32), 160, 27);
        tf3.setBounds(165, 5 + (3 * 32), 160, 27);
        tf4.setBounds(165, 5 + (4 * 32), 160, 27);

        bt1.setBounds(85, 5 + (6 * 32), 160, 27);
        bt3.setBounds(85, 5 + (7 * 32), 160, 27);
        bt2.setBounds(85, 5 + (8 * 32), 160, 27);


        p.setBounds(0, 0, 335, 518);
        p.add(label1);
        p.add(label2);
        p.add(label3);
        p.add(label4);
        p.add(label5);
        p.add(tf1);
        p.add(tf2);
        p.add(tf3);
        p.add(tf4);
        p.add(tf5);
        p.add(bt1);
        p.add(bt2);
        p.add(bt3);
        return p;
    }
    private ActionListener getAlarmActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Constants.ALARM_OK) {
                    if (init()) {
                        Constants.ALARM_AGAIN = true;
                        int hour = getHour(), min = getMin();
                        alTimer = new AlarmTimer(hour, min);
                        alTimer.startAlarm();
                        bt1.setText("停止闹钟");
                        bt2.setVisible(true);
                        Constants.ALARM_OK = true;
                        JOptionPane.showMessageDialog(null, "闹钟启动成功!  " + hour + " : " + min, "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "请正确填写闹钟参数！！", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    LOGGER.info("闹钟关闭成功!");
                    alTimer.setPlay(false);
                    alTimer.getExecutor().shutdown();
                    Constants.ALARM_AGAIN = false;
                    bt1.setText("启动闹钟 ");
                    bt2.setVisible(false);
                    Constants.ALARM_OK = false;
                }
            }

            private boolean init() {
                String text1 = tf1.getText();
                String text2 = tf2.getText();
                String text3 = tf3.getText();
                String text4 = tf4.getText();
                String pattern = "[0-2]{0,1}[0-9]{1}:[0-5]{0,1}[0-9]{1}";
                if (text1 != null) {
                    text1 = text1.replace("：", ":");
                } else {
                    return false;
                }
                if (!Pattern.matches(pattern, text1)) {
                    return false;
                } else {
                    if (Integer.parseInt(text1.split(":")[0]) >= 24) {
                        return false;
                    }
                }
                if (text2 != null && !"".equals(text2)) {
                    text2 = text2.replace("\\\\", "/");
                    Constants.FILE_PATH = text2;
                } else {
                    return false;
                }
                if (text3 != null && !"".equals(text3)) {
                    String pattern3 = "[0-9]{1,}";
                    if (!Pattern.matches(pattern3, text3)) {
                        return false;
                    } else if (Integer.parseInt(text3) <= 0) {
                        return false;
                    }
                    Constants.EVERY_OTHER_TIME = Integer.parseInt(text3);
                    if (Constants.EVERY_OTHER_TIME > 2) {
                        Constants.ALARM_SECONDS = 150;
                    } else if (Constants.EVERY_OTHER_TIME > 1) {
                        Constants.ALARM_SECONDS = 90;
                    }
                } else {
                    return false;
                }
                if (text4 != null && !"".equals(text4)) {
                    String pattern4 = "[0-9]{1,}";
                    if (!Pattern.matches(pattern4, text4)) {
                        return false;
                    } else if (Integer.parseInt(text4) <= 0) {
                        return false;
                    }
                    Constants.ALARM_TIMES = Integer.parseInt(text4);
                } else {
                    return false;
                }
                return true;
            }

            private int getHour() {
                String text = tf1.getText();
                text = text.replace("：", ":");
                if (text.indexOf(":") > 0 && text.indexOf(":") < text.length() - 1) {
                    return Integer.parseInt(text.split(":")[0]);
                }
                return 8;
            }

            private int getMin() {
                String text = tf1.getText();
                text = text.replace("：", ":");
                if (text.indexOf(":") > 0 && text.indexOf(":") < text.length() - 1) {
                    return Integer.parseInt(text.split(":")[1]);
                }
                return 0;
            }
        };
    }

    private ActionListener getAlarmLaterActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGER.info("稍后再响! ");
                alTimer.setPlay(false);
            }
        };
    }
    private ActionListener getAlarmCountActionListener(ZButton zt) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zt.getText().contains("启动")) {
                    if (init()) {
                        Constants.ALARM_AGAIN = true;
                        int hour = getHour(), min = getMin();
                        LOGGER.info("定时间隔: " + hour + " : " + min);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.HOUR_OF_DAY, hour);
                        calendar.add( Calendar.MINUTE, min);
                        hour = calendar.get(Calendar.HOUR_OF_DAY);
                        min = calendar.get(Calendar.MINUTE);
                        alTimer = new AlarmTimer(hour, min);
                        alTimer.startAlarm();
                        bt3.setText("停止定时");
                        bt2.setVisible(true);
                        Constants.ALARM_OK = true;
                        JOptionPane.showMessageDialog(null, "定时启动成功!  " + hour + " : " + min, "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "请正确填写闹钟参数！！", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    LOGGER.info("定时关闭成功！");
                    alTimer.setPlay(false);
                    alTimer.getExecutor().shutdown();
                    Constants.ALARM_AGAIN = false;
                    bt3.setText("启动定时");
                    bt2.setVisible(false);
                    Constants.ALARM_OK = false;
                }
            }

            private boolean init() {
                String text1 = tf1.getText();
                String text2 = tf2.getText();
                String text3 = tf3.getText();
                String text4 = tf4.getText();
                String text5 = tf4.getText();
                String pattern = "[0-2]{0,1}[0-9]{1}:[0-5]{0,1}[0-9]{1}";
                if (text1 != null) {
                    text1 = text1.replace("：", ":");
                } else {
                    return false;
                }
                if (!Pattern.matches(pattern, text1)) {
                    return false;
                } else {
                    if (Integer.parseInt(text1.split(":")[0]) >= 24) {
                        return false;
                    }
                }
                if (text2 != null && !"".equals(text2)) {
                    text2 = text2.replace("\\\\", "/");
                    Constants.FILE_PATH = text2;
                } else {
                    return false;
                }
                if (text3 != null && !"".equals(text3)) {
                    String pattern3 = "[0-9]{1,}";
                    if (!Pattern.matches(pattern3, text3)) {
                        return false;
                    } else if (Integer.parseInt(text3) <= 0) {
                        return false;
                    }
                    Constants.EVERY_OTHER_TIME = Integer.parseInt(text3);
                    if (Constants.EVERY_OTHER_TIME > 2) {
                        Constants.ALARM_SECONDS = 150;
                    } else if (Constants.EVERY_OTHER_TIME > 1) {
                        Constants.ALARM_SECONDS = 90;
                    }
                } else {
                    return false;
                }
                if (text4 != null && !"".equals(text4)) {
                    String pattern4 = "[0-9]{1,}";
                    if (!Pattern.matches(pattern4, text4)) {
                        return false;
                    } else if (Integer.parseInt(text4) <= 0) {
                        return false;
                    }
                    Constants.ALARM_TIMES = Integer.parseInt(text4);
                } else {
                    return false;
                }
                if (text5 != null) {
                    text5 = text5.replace("：", ":");
                } else {
                    return false;
                }
                return true;
            }

            private int getHour() {
                String text = tf5.getText();
                text = text.replace("：", ":");
                if (text.indexOf(":") > 0 && text.indexOf(":") < text.length() - 1) {
                    return Integer.parseInt(text.split(":")[0]);
                }
                return 8;
            }

            private int getMin() {
                String text = tf5.getText();
                text = text.replace("：", ":");
                if (text.indexOf(":") > 0 && text.indexOf(":") < text.length() - 1) {
                    return Integer.parseInt(text.split(":")[1]);
                }
                return 0;
            }
        };
    }
}
