package fgoScript.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import commons.util.MySpringUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FgoPreference {
    public static FgoPreference getInstance(){
        FgoPreference fp;
        String filepath = NativeCp.getUserDir() + "/config/"+ Constant.FGO +"/FgoPreference.json";
        fp = JSONObject.parseObject(GameUtil.getJsonString(filepath), FgoPreference.class);
        return fp;
    }
    public static FgoPreference getSpringBean(){
        return (FgoPreference) MySpringUtil.getApplicationContext().getBean("fgoPreference");
    }
    /** 按钮坐标 **/
    private Point Location;
    /** 具体选项点 **/
    private List<Point> optionPoints;
    /** 具体选项色 **/
    private List<String> colorStyles;
    /** 加速中点 **/
    private Point accelerateMiddle;
    /** 加速左点 **/
    private Point accelerateLeft;

    public Point getLocation() {
        return Location;
    }

    public void setLocation(Point location) {
        Location = location;
    }

    public List<Point> getOptionPoints() {
        return optionPoints;
    }

    public void setOptionPoints(List<Point> optionPoints) {
        this.optionPoints = optionPoints;
    }

    public List<String> getColorStyles() {
        return colorStyles;
    }

    public void setColorStyles(List<String> colorStyles) {
        this.colorStyles = colorStyles;
    }

    public Point getAccelerateMiddle() {
        return accelerateMiddle;
    }

    public void setAccelerateMiddle(Point accelerateMiddle) {
        this.accelerateMiddle = accelerateMiddle;
    }

    public Point getAccelerateLeft() {
        return accelerateLeft;
    }

    public void setAccelerateLeft(Point accelerateLeft) {
        this.accelerateLeft = accelerateLeft;
    }

    public static void main(String[] args) {
        FgoPreference pf = new FgoPreference();
        pf.setLocation(new Point(0,0));
        List<Point> optionPoints = new ArrayList<>();
        optionPoints.add(new Point(0,0));
        optionPoints.add(new Point(0,0));
        List<Color> optionColors = new ArrayList<>();
        optionColors.add(new Color(255,255,255));
        optionColors.add(new Color(255,255,255));
        pf.setOptionPoints(optionPoints);
        pf.setAccelerateMiddle(new Point(1171, 118));
        pf.setAccelerateLeft(new Point(1158, 116));
        String text = JSON.toJSONString(pf);
        System.out.println(text);
    }
}
