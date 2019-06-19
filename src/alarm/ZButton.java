package alarm;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @description: TODO
 * @author: RENZHEHAO
 * @create: 2019-06-17 09:29
 **/
public class ZButton extends JButton {
    public ZButton(String name, int style) {
        super.setText(name);
        this.setOpaque(false);
        setColor(style);
    }


    /* 决定圆角的弧度 */
    public static final int radius = 4;
    private Color COLOR1, COLOR2;
    private Color MY_GRID_COLOR01, MY_GRID_COLOR02, MY_FONT_COLOR;
    public static final int pink = 3;
    public static final int ashen = 2;
    public static final int sky = 1;
    public static final int stone = 0;
    /* 粉红 */
    public static final Color mainStyle01 = new Color(100, 0, 0);
    public static final Color mainStyle02 = new Color(0, 0, 100);
    /* 激活颜色 */
    public static final Color active01 = new Color(100, 149, 237);
    public static final Color active02 = new Color(100, 149, 237);
    /* 深宝石蓝 */
    public static final Color stone1 = new Color(255, 0, 0);
    public static final Color stone2 = new Color(39, 121, 181);
    /* 执行颜色 */
    public static final Color excuteStyle01 = new Color(205, 150, 205);
    public static final Color excuteStyle02 = new Color(205, 150, 205);
    /* 技能任务组颜色 */
    public static final Color skillColor01 = new Color(100, 0, 0);
    public static final Color skillColor02 = new Color(0, 0, 80);
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(getMyGraphics(g));
    }

    private void setColor(int style) {
        paintcolor();
        if (style == pink) {
            COLOR1 = mainStyle01;
            COLOR2 = mainStyle02;
            MY_GRID_COLOR01 = mainStyle01;
            MY_GRID_COLOR02 = mainStyle02;
        }
        if (style == ashen) {
            COLOR1 = active01;
            COLOR2 = active02;
            MY_GRID_COLOR01 = active01;
            MY_GRID_COLOR02 = active02;
        }
        if (style == stone) {
            COLOR1 = stone1;
            COLOR2 = stone2;
            MY_GRID_COLOR01 = stone1;
            MY_GRID_COLOR02 = stone2;
        }
        if (style == sky) {
            COLOR1 = excuteStyle01;
            COLOR2 = excuteStyle02;
            MY_GRID_COLOR01 = excuteStyle01;
            MY_GRID_COLOR02 = excuteStyle02;
        }
    }
    private void paintcolor() {
        setMargin(new Insets(0, 0, 0, 0));
        setFont(new Font("微软雅黑", Font.BOLD, 13));
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setBackground(Color.BLACK);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
    private Graphics getMyGraphics(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        float tran = 0.65F;
        int height = getHeight();
        int with = getWidth();
        if (getModel().isRollover() || getModel().isSelected()) { //鼠标离开/进入时的透明度改变量
            tran = 0.85F;
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /* GradientPaint是颜色渐变类 */
        GradientPaint p1;
        GradientPaint p2;
        if (getModel().isPressed()) {
            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, height, new Color(100, 100, 100), true);
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, height, new Color(255, 255, 255, 100), true);
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, height, new Color(0, 0, 0), true);
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0, height, new Color(0, 0, 0, 50), true);
        }
        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, with - 1, height - 1, radius, radius);
        // 最后两个参数数值越大，按钮越圆，以下同理
        Shape clip = g2d.getClip();
        g2d.clip(r2d);
        GradientPaint gp;

        if (!getModel().isEnabled()) {
            gp = new GradientPaint(0.0F, 0.0F, MY_GRID_COLOR01, 0.0F, height * 1 / 5, MY_GRID_COLOR02, true);
            this.setUI(new BasicButtonUI() //如果你的是windows风格的界面,这里new一个
            {
                //com.sun.java.swing.plaf.windows.WindowsButtonUI
                @Override
                protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
                    AbstractButton b = ((AbstractButton) c);
                    ButtonModel model = b.getModel();
                    if (!model.isEnabled()) {
                        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
                        int mnemonicIndex = b.getDisplayedMnemonicIndex();
                        g.setColor(MY_FONT_COLOR); //你的字体颜色
                        SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemonicIndex,
                                textRect.x + getTextShiftOffset(),
                                textRect.y + fm.getAscent() + getTextShiftOffset());
                    } else {
                        super.paintText(g, c, textRect, text);
                    }
                }
            });
        } else {
            gp = new GradientPaint(0.0F, 0.0F, COLOR1, 0.0F, height / 2, COLOR2, true);
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tran));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, with, height);
        g2d.setClip(clip);
        g2d.setPaint(p1);
        g2d.drawRoundRect(0, 0, with - 3, height - 3, radius, radius);
        g2d.setPaint(p2);
        g2d.drawRoundRect(1, 1, with - 3, height - 3, radius, radius);
        g2d.dispose();
        return g;
    }
}
