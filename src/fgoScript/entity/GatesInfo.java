package fgoScript.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GatesInfo {
    private Point sliceTopPoint = new Point();

    private Point sliceDownPoint = new Point();

    private List<Gates> gatesArray = new ArrayList<Gates>();;

    public Point getSliceTopPoint() {
        return sliceTopPoint;
    }

    public void setSliceTopPoint(Point sliceTopPoint) {
        this.sliceTopPoint = sliceTopPoint;
    }

    public Point getSliceDownPoint() {
        return sliceDownPoint;
    }

    public void setSliceDownPoint(Point sliceDownPoint) {
        this.sliceDownPoint = sliceDownPoint;
    }

    public List<Gates> getGatesArray() {
        return gatesArray;
    }

    public void setGatesArray(List<Gates> gatesArray) {
        this.gatesArray = gatesArray;
    }
}
