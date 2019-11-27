package destinychild.entity;

import java.awt.*;
import java.util.List;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName TaskInfo.java
 * @Description Dc任务明细实体
 * @createTime 2019年11月06日 11:41:00
 */
public class TaskInfo {
    /** 任务id **/
    private int taskId;
    /** 任务名称 **/
    private String taskName;
    /** 任务名称 **/
    private String startModleName;
    /** 单任务点击点列表（点）**/
    private List<Point> taskClickPoints;
    /** 单任务点击点列表（色） **/
    private List<Color> taskClickColors;
    /** 任务点击点，点击次数 **/
    private int taskPointRepetitions;
    /** 自动点击等待时间 **/
    private int taskAutoClickDelaySeconds;
    /** 进入任务页面等待时间 **/
    private int taskInToDelaySeconds;
    /** 是否启用 **/
    private boolean enable;
    /** 小任务是否点击返回按钮 **/
    private boolean smallReturn;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<Point> getTaskClickPoints() {
        return taskClickPoints;
    }

    public void setTaskClickPoints(List<Point> taskClickPoints) {
        this.taskClickPoints = taskClickPoints;
    }

    public int getTaskPointRepetitions() {
        return taskPointRepetitions;
    }

    public void setTaskPointRepetitions(int taskPointRepetitions) {
        this.taskPointRepetitions = taskPointRepetitions;
    }

    public int getTaskAutoClickDelaySeconds() {
        return taskAutoClickDelaySeconds;
    }

    public void setTaskAutoClickDelaySeconds(int taskAutoClickDelaySeconds) {
        this.taskAutoClickDelaySeconds = taskAutoClickDelaySeconds;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getTaskInToDelaySeconds() {
        return taskInToDelaySeconds;
    }

    public void setTaskInToDelaySeconds(int taskInToDelaySeconds) {
        this.taskInToDelaySeconds = taskInToDelaySeconds;
    }

    public String getStartModleName() {
        return startModleName;
    }

    public void setStartModleName(String startModleName) {
        this.startModleName = startModleName;
    }

    public List<Color> getTaskClickColors() {
        return taskClickColors;
    }

    public void setTaskClickColors(List<Color> taskClickColors) {
        this.taskClickColors = taskClickColors;
    }

    public boolean isSmallReturn() {
        return smallReturn;
    }

    public void setSmallReturn(boolean smallReturn) {
        this.smallReturn = smallReturn;
    }
}
