package destinychild;

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
    /** 终了点 */
    private Point finishPoint;
    /** 终了色 */
    private Color finishColor;
    /** 开灯点 */
    private Point startPoint;
    /** 开灯色 */
    private Color startColor;
    /** 免费灯点 */
    private Point freeFinishPoint;
    /** 免费灯色（无） */
    private Color freeFinishColor;
    /** 免费灯返回按钮点 */
    private Point freeReturnPoint;
    /** 免费灯返回按钮色 */
    private Color freeReturnColor;
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

    public Point getFinishPoint() {
        return finishPoint;
    }

    public void setFinishPoint(Point finishPoint) {
        this.finishPoint = finishPoint;
    }

    public Color getFinishColor() {
        return finishColor;
    }

    public void setFinishColor(Color finishColor) {
        this.finishColor = finishColor;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public Point getFreeFinishPoint() {
        return freeFinishPoint;
    }

    public void setFreeFinishPoint(Point freeFinishPoint) {
        this.freeFinishPoint = freeFinishPoint;
    }

    public Color getFreeFinishColor() {
        return freeFinishColor;
    }

    public void setFreeFinishColor(Color freeFinishColor) {
        this.freeFinishColor = freeFinishColor;
    }

    public Point getFreeReturnPoint() {
        return freeReturnPoint;
    }

    public void setFreeReturnPoint(Point freeReturnPoint) {
        this.freeReturnPoint = freeReturnPoint;
    }

    public Color getFreeReturnColor() {
        return freeReturnColor;
    }

    public void setFreeReturnColor(Color freeReturnColor) {
        this.freeReturnColor = freeReturnColor;
    }
}
