package destinyChild.entity;

import java.awt.*;
import java.util.List;

/**
 * @description: raid过滤菜单
 * @author: RENZHEHAO
 * @create: 2019-06-05 12:32
 **/
public class RaidFilterMenu {
    /** 菜单点 */
    private Point menuPoint;
    /** 菜单色 */
    private Color menuColor;
    /** 未参加点 */
    private Point unDoPoint;
    /** 未参加色 */
    private Color unDoColor;
    /** 参加人数点 */
    private Point partPoint;
    /** 参加人数色 */
    private Color partColor;
    /** 确认点 */
    private Point confirmPoint;
    /** 确认色 */
    private Color confirmColor;

    /** 加载点 */
    private Point loadPoint;
    /** 加载色 */
    private Color loadColor;


    /** 停止点击点 */
    private Point stopClickPoint;


    /** 结束点集合 */
    private List<Point> stopPointList;
    /** 结束色集合 */
    private List<Color> stopColorList;


    public Point getMenuPoint() {
        return menuPoint;
    }

    public void setMenuPoint(Point menuPoint) {
        this.menuPoint = menuPoint;
    }

    public Color getMenuColor() {
        return menuColor;
    }

    public void setMenuColor(Color menuColor) {
        this.menuColor = menuColor;
    }

    public Point getUnDoPoint() {
        return unDoPoint;
    }

    public void setUnDoPoint(Point unDoPoint) {
        this.unDoPoint = unDoPoint;
    }

    public Color getUnDoColor() {
        return unDoColor;
    }

    public void setUnDoColor(Color unDoColor) {
        this.unDoColor = unDoColor;
    }

    public Point getPartPoint() {
        return partPoint;
    }

    public void setPartPoint(Point partPoint) {
        this.partPoint = partPoint;
    }

    public Color getPartColor() {
        return partColor;
    }

    public void setPartColor(Color partColor) {
        this.partColor = partColor;
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

    public List<Point> getStopPointList() {
        return stopPointList;
    }

    public void setStopPointList(List<Point> stopPointList) {
        this.stopPointList = stopPointList;
    }

    public List<Color> getStopColorList() {
        return stopColorList;
    }

    public void setStopColorList(List<Color> stopColorList) {
        this.stopColorList = stopColorList;
    }

    public Point getStopClickPoint() {
        return stopClickPoint;
    }

    public void setStopClickPoint(Point stopClickPoint) {
        this.stopClickPoint = stopClickPoint;
    }

    public Point getLoadPoint() {
        return loadPoint;
    }

    public void setLoadPoint(Point loadPoint) {
        this.loadPoint = loadPoint;
    }

    public Color getLoadColor() {
        return loadColor;
    }

    public void setLoadColor(Color loadColor) {
        this.loadColor = loadColor;
    }
}
