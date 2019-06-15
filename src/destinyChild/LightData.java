package destinyChild;

import java.awt.*;

/**
 * @description: 百鬼夜行相关数据
 * @author: RENZHEHAO
 * @create: 2019-06-15 08:59
 **/
public class LightData {
    /** 点击点 */
    private Point clickPoint;
    /** 左点 */
    private Point leftPoint;
    /** 右点 */
    private Point rightPoint;
    /** 按钮点 */
    private Point buttonPoint;
    /** 按钮色 */
    private Color buttonColor;
    /** 确认点 */
    private Point confirmPoint;
    /** 确认色 */
    private Color confirmColor;
    /** 起始毫秒步长 */
    private int smStep;
    /** 毫秒加速步长 */
    private int amStep;
    /** 点击延时 */
    private int clickDelay;

    public Point getClickPoint() {
        return clickPoint;
    }

    public void setClickPoint(Point clickPoint) {
        this.clickPoint = clickPoint;
    }

    public Point getLeftPoint() {
        return leftPoint;
    }

    public void setLeftPoint(Point leftPoint) {
        this.leftPoint = leftPoint;
    }

    public Point getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Point rightPoint) {
        this.rightPoint = rightPoint;
    }

    public int getSmStep() {
        return smStep;
    }

    public void setSmStep(int smStep) {
        this.smStep = smStep;
    }

    public int getAmStep() {
        return amStep;
    }

    public void setAmStep(int amStep) {
        this.amStep = amStep;
    }

    public int getClickDelay() {
        return clickDelay;
    }

    public void setClickDelay(int clickDelay) {
        this.clickDelay = clickDelay;
    }

    public Point getButtonPoint() {
        return buttonPoint;
    }

    public void setButtonPoint(Point buttonPoint) {
        this.buttonPoint = buttonPoint;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;
    }

    public Point getConfirmPoint() {
        return confirmPoint;
    }

    public void setConfirmPoint(Point confirmPoint) {
        this.confirmPoint = confirmPoint;
    }

    public Color getConfirmColor() {
        return confirmColor;
    }

    public void setConfirmColor(Color confirmColor) {
        this.confirmColor = confirmColor;
    }
}
