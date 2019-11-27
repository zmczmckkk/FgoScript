package destinychild.entity;

import com.alibaba.fastjson.JSON;

import java.awt.*;

/**
 * @description: raid开始战斗页面
 * @author: RENZHEHAO
 * @create: 2019-08-02 19:47
 **/
public class RaidStartPage {
    /** 排行等级点 */
    private Point rankPoint;
    /** 排行等级色 */
    private Color rankColor;

    /** 车等级点 */
    private Point levelPoint;
    /** 车等级色 */
    private Color levelColor;

    /** 票等级点 */
    private Point ticketPoint;
    /** 票等级色 */
    private Color ticketColor;

    /** 无票点 */
    private Point noTicketPoint;
    /** 无票色 */
    private Color noTicketColor;

    /** 10票点 */
    private Point tenTicketPoint;
    /** 10票色 */
    private Color tenTicketColor;

    /** 开始点 */
    private Point startPoint;
    /** 开始色 */
    private Color startColor;

    /** 回退点 */
    private Point returnPoint;

    public Point getRankPoint() {
        return rankPoint;
    }

    public void setRankPoint(Point rankPoint) {
        this.rankPoint = rankPoint;
    }

    public Color getRankColor() {
        return rankColor;
    }

    public void setRankColor(Color rankColor) {
        this.rankColor = rankColor;
    }

    public Point getLevelPoint() {
        return levelPoint;
    }

    public void setLevelPoint(Point levelPoint) {
        this.levelPoint = levelPoint;
    }

    public Color getLevelColor() {
        return levelColor;
    }

    public void setLevelColor(Color levelColor) {
        this.levelColor = levelColor;
    }

    public Point getTicketPoint() {
        return ticketPoint;
    }

    public void setTicketPoint(Point ticketPoint) {
        this.ticketPoint = ticketPoint;
    }

    public Color getTicketColor() {
        return ticketColor;
    }

    public void setTicketColor(Color ticketColor) {
        this.ticketColor = ticketColor;
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

    public Point getReturnPoint() {
        return returnPoint;
    }

    public void setReturnPoint(Point returnPoint) {
        this.returnPoint = returnPoint;
    }

    public Point getNoTicketPoint() {
        return noTicketPoint;
    }

    public void setNoTicketPoint(Point noTicketPoint) {
        this.noTicketPoint = noTicketPoint;
    }

    public Color getNoTicketColor() {
        return noTicketColor;
    }

    public void setNoTicketColor(Color noTicketColor) {
        this.noTicketColor = noTicketColor;
    }

    public Point getTenTicketPoint() {
        return tenTicketPoint;
    }

    public void setTenTicketPoint(Point tenTicketPoint) {
        this.tenTicketPoint = tenTicketPoint;
    }

    public Color getTenTicketColor() {
        return tenTicketColor;
    }

    public void setTenTicketColor(Color tenTicketColor) {
        this.tenTicketColor = tenTicketColor;
    }

    public static void main(String[] args) {
        RaidStartPage pi = new RaidStartPage();
        pi.setTenTicketColor(new Color(255,255,255));
        pi.setTenTicketPoint(new Point(0,0));
        String text = JSON.toJSONString(pi);
        System.out.println(text);
    }
}
