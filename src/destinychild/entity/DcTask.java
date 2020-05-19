package destinychild.entity;

import java.awt.*;
import java.util.List;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName DcTask.java
 * @Description 天命之子任务实体类
 * @createTime 2019年10月28日 04:13:00
 */
public class DcTask {
    /** 执行任务页面标志点 **/
    private Point taskPagePoint;
    /** 执行任务页面标志色 **/
    private Color taskPageColor;
    /** 主页标志点 **/
    private Point homePagePoint;
    /** 主页标志色 **/
    private Color homePageColor;
    /** 回退按钮点 **/
    private Point taskReturnPoint;
    /** 主页进入按钮点 **/
    private Point intoTaskPoint;
    /** 任务列表点 **/
    private List<TaskInfo> tasklist;

    public Point getTaskPagePoint() {
        return taskPagePoint;
    }

    public void setTaskPagePoint(Point taskPagePoint) {
        this.taskPagePoint = taskPagePoint;
    }

    public Color getTaskPageColor() {
        return taskPageColor;
    }

    public void setTaskPageColor(Color taskPageColor) {
        this.taskPageColor = taskPageColor;
    }

    public Point getTaskReturnPoint() {
        return taskReturnPoint;
    }

    public void setTaskReturnPoint(Point taskReturnPoint) {
        this.taskReturnPoint = taskReturnPoint;
    }

    public List<TaskInfo> getTasklist() {
        return tasklist;
    }

    public void setTasklist(List<TaskInfo> tasklist) {
        this.tasklist = tasklist;
    }

    public Point getHomePagePoint() {
        return homePagePoint;
    }

    public void setHomePagePoint(Point homePagePoint) {
        this.homePagePoint = homePagePoint;
    }

    public Color getHomePageColor() {
        return homePageColor;
    }

    public void setHomePageColor(Color homePageColor) {
        this.homePageColor = homePageColor;
    }

    public Point getIntoTaskPoint() {
        return intoTaskPoint;
    }

    public void setIntoTaskPoint(Point intoTaskPoint) {
        this.intoTaskPoint = intoTaskPoint;
    }
}
