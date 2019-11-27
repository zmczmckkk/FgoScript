package destinychild.entity;

import java.awt.*;

public class GuestSave {
    //文件管理器坐标点
    private Point fileManagePoint;
    //文件管理器颜色
    private Color fileManageColor;
    //根目录坐标点
    private Point filesPoint;
    //save文件坐标点
    private Point saveFilePoint;
    /**
     * 重命名坐标点
     */
    private Point renamePoint;
    //确认修改坐标点
    private Point saveYesPoint;
    //关闭destiny坐标点
    private Point closeDestinyPoint;
    //首页坐标点
    private Point homePagePoint;

    public Point getFilesPoint() {
        return filesPoint;
    }

    public void setFilesPoint(Point filesPoint) {
        this.filesPoint = filesPoint;
    }

    public Point getSaveFilePoint() {
        return saveFilePoint;
    }

    public void setSaveFilePoint(Point saveFilePoint) {
        this.saveFilePoint = saveFilePoint;
    }

    public Point getSaveYesPoint() {
        return saveYesPoint;
    }

    public void setSaveYesPoint(Point saveYesPoint) {
        this.saveYesPoint = saveYesPoint;
    }

    public Point getCloseDestinyPoint() {
        return closeDestinyPoint;
    }

    public void setCloseDestinyPoint(Point closeDestinyPoint) {
        this.closeDestinyPoint = closeDestinyPoint;
    }

    public Point getHomePagePoint() {
        return homePagePoint;
    }

    public void setHomePagePoint(Point homePagePoint) {
        this.homePagePoint = homePagePoint;
    }

    public Point getFileManagePoint() {
        return fileManagePoint;
    }

    public void setFileManagePoint(Point fileManagePoint) {
        this.fileManagePoint = fileManagePoint;
    }

    public Color getFileManageColor() {
        return fileManageColor;
    }

    public void setFileManageColor(Color fileManageColor) {
        this.fileManageColor = fileManageColor;
    }

    public Point getRenamePoint() {
        return renamePoint;
    }

    public void setRenamePoint(Point renamePoint) {
        this.renamePoint = renamePoint;
    }
}
