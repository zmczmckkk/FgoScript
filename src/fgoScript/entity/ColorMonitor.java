package fgoScript.entity;

import java.awt.*;
import java.util.List;

public class ColorMonitor {
    private String name;
    /** 检查点集合 **/
    private List<PointColor> checkPointList;
    /** 点击点集合 **/
    private List<Point> clickPointList;
    /** 是否抛异常 **/
    private boolean throwException;
    /** 是否延长超时 **/
    private boolean extendOutTime;

    public List<PointColor> getCheckPointList() {
        return checkPointList;
    }

    public void setCheckPointList(List<PointColor> checkPointList) {
        this.checkPointList = checkPointList;
    }

    public List<Point> getClickPointList() {
        return clickPointList;
    }

    public void setClickPointList(List<Point> clickPointList) {
        this.clickPointList = clickPointList;
    }

    public boolean isThrowException() {
        return throwException;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExtendOutTime() {
        return extendOutTime;
    }

    public void setExtendOutTime(boolean extendOutTime) {
        this.extendOutTime = extendOutTime;
    }
}
