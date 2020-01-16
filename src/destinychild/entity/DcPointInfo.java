package destinychild.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import commons.entity.Constant;
import commons.entity.NativeCp;
import commons.util.GameUtil;
import commons.util.MySpringUtil;

import java.awt.*;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName DcPointInfo.java
 * @Description Dc点色
 * @createTime 2020年01月12日 16:11:00
 */
public class DcPointInfo {
    public static DcPointInfo getInstance(){
        DcPointInfo dpi;
        String filepath = NativeCp.getUserDir() + "/config/"+ Constant.DC +"/DcPointInfo.json";
        dpi = JSONObject.parseObject(GameUtil.getJsonString(filepath), DcPointInfo.class);
        return dpi;
    }
    public static DcPointInfo getSpringBean(){
        return (DcPointInfo) MySpringUtil.getApplicationContext().getBean("dcPointInfo");
    }
    /** 进度圈点色 **/
    private PointAndColor loading;

    public PointAndColor getLoading() {
        return loading;
    }

    public void setLoading(PointAndColor loading) {
        this.loading = loading;
    }

    public static void main(String[] args) {
        DcPointInfo d = new DcPointInfo();
        PointAndColor dc = new PointAndColor();
        dc.setColor(new Color(220,125,0));
        dc.setPoint(new Point(256,980));
        d.setLoading(dc);
        String s = JSON.toJSONString(d);
        System.out.println(s);
    }
}
