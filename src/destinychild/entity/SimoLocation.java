package destinychild.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import fgoScript.entity.PointColor;
import jodd.io.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName SimoLocation.java
 * @Description 模拟器定位坐标信息实体
 * @createTime 2020年01月12日 09:57:00
 */
public class SimoLocation {
    private Point originalPoint;
    private Point destinyPoint;
    private List<PointColor> targetPCList;
    private int dy;
    public static final String filepath = NativeCp.getUserDir() + "/config/"+ Constant.DC +"/SimoLocation" + ".json";
    public Point forOriginalPoint(){
        int minvalue = 0;
        Point p = null;
        Point tempPoint = new Point();
        Point mousePoint = GameUtil.getMousePosition();
        int x = (int) mousePoint.getX();
        int y = (int) mousePoint.getY();
        List<PointColor> pCList = getTargetPCList();
        Color tempColor;
        PointColor tempPc;
        Boolean flag01;
        Boolean flag02;
        int dx;
        int dy;
        for (int i = x-15; i < x+15; i++) {
            for (int j = y-15; j < y+15; j++) {
               System.out.println("i: " + i + " _ j: " + j + " |");
               tempPoint.setLocation(i,j);
               tempColor = GameUtil.getScreenPixel(tempPoint);
               if (GameUtil.likeEqualColor(tempColor, pCList.get(0).getColor(),minvalue)){
                   tempPc = pCList.get(0);
                   dx = (int) (tempPoint.getX() - tempPc.getPoint().getX());
                   dy = (int) (tempPoint.getY() - tempPc.getPoint().getY());
                   tempPc = pCList.get(1);
                   flag01 =  GameUtil.likeEqualColor(tempPc.getColor(),GameUtil.getRb().getPixelColor((int)tempPc.getPoint().getX()+dx,(int)tempPc.getPoint().getY()+dy),minvalue);
                   tempPc = pCList.get(2);
                   flag02 =  GameUtil.likeEqualColor(tempPc.getColor(),GameUtil.getRb().getPixelColor((int)tempPc.getPoint().getX()+dx,(int)tempPc.getPoint().getY()+dy),minvalue);
                   System.out.println(flag01+" " + flag02);
                   if(flag01 && flag02){
                       p = tempPoint;
                       System.out.println(p.getX()+"_"+p.getY());
//                       p.setLocation(p.getX(), p.getY() + getDy());
                       return p;
                   }else{
                   }
               }else {
                   continue;
               }
            }
        }
        return p;
    }
    public void moveToDestinyPoint() {
        Point p = getOriginalPoint();
        p.setLocation(p.getX(), p.getY() + getDy());
        GameUtil.moveToDestinyPoint(p,getDestinyPoint());
    }
    public void locateOriginalPoint(){
        Point p = forOriginalPoint();
        if (p==null){
            JOptionPane.showMessageDialog(null, "定位失败，请重新移动鼠标至首页体力红心中心处！", "信息", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.setOriginalPoint(p);
        String jsonString = JSON.toJSONString(this);
        try {
            FileUtil.writeString(new File(filepath),jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "定位成功！", "信息", JOptionPane.INFORMATION_MESSAGE);


    }
    public static SimoLocation getInstance() {
        SimoLocation simoLocation = JSONObject.parseObject(GameUtil.getJsonString(filepath), SimoLocation.class);
        return simoLocation;
    }
    public Point getOriginalPoint() {
        return originalPoint;
    }

    public void setOriginalPoint(Point originalPoint) {
        this.originalPoint = originalPoint;
    }

    public List<PointColor> getTargetPCList() {
        return targetPCList;
    }

    public void setTargetPCList(List<PointColor> targetPCList) {
        this.targetPCList = targetPCList;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public Point getDestinyPoint() {
        return destinyPoint;
    }

    public void setDestinyPoint(Point destinyPoint) {
        this.destinyPoint = destinyPoint;
    }

    public static void main(String[] args) {
        try {
            SimoLocation.getInstance().moveToDestinyPoint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}