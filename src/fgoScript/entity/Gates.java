package fgoScript.entity;

import fgoScript.constant.GameConstant;

import java.awt.*;

public class Gates {
    private int id;
    private String name;
    private int classify;
    private Point pSetLoc;
    private Point pAp10;
    private Point pAp20;
    private Point pAp30;
    private Point pAp40;

    public  Point getGateByApNum(int apNum) throws Exception {
        Point gatePoint;
        switch(apNum){
            case 10 : {
                gatePoint = getpAp10();
                break;
            }
            case 20 : {
                gatePoint = getpAp20();
                break;
            }
            case 30 : {
                gatePoint = getpAp30();
                break;
            }
            case 40 : {
                gatePoint = getpAp40();
                break;
            }
            default : {
                throw new Exception("no such ap room!");
            }
        }
        return gatePoint;
    }
    public  Point getSuppPoint() throws Exception {
        Point suppPoint;
        Point pAll = new Point(131, 176);// 颜色：72;75;72 Color c = new Color(72, 75, 72); all
        Point pSaber = new Point(196, 176);// 颜色：41;40;41 Color c = new Color(41, 40, 41); saber
        Point pArcher = new Point(264, 180);// 颜色：176;189;190 Color c = new Color(176, 189, 190); archer
        Point pLancer = new Point(334, 177);// 颜色：26;30;26 Color c = new Color(26, 30, 26); lancer
        Point pRider = new Point(400, 180);// 颜色：67;65;67 Color c = new Color(67, 65, 67); rider
        Point pCaster = new Point(468, 179);// 颜色：9;13;9 Color c = new Color(9, 13, 9); caster
        Point pAsssion = new Point(535, 178);// 颜色：253;250;253 Color c = new Color(253, 250, 253); asssion
//			Point p7 = new Point(598, 180);// 颜色：255;251;255 Color c = new Color(255, 251, 255); basker
//			Point p8 = new Point(669, 186);// 颜色：255;251;255 Color c = new Color(255, 251, 255); four
        switch(getClassify()){
            case GameConstant.CLASSIFY_ARCHER : {
                suppPoint = pLancer;
                break;
            }
            case GameConstant.CLASSIFY_ASSASIN : {
                suppPoint = pCaster;
                break;
            }
            case GameConstant.CLASSIFY_BASAKER : {
                suppPoint = pAll;
                break;
            }
            case GameConstant.CLASSIFY_CASTER : {
                suppPoint = pRider;
                break;
            }
            case GameConstant.CLASSIFY_LANCER : {
                suppPoint = pSaber;
                break;
            }
            case GameConstant.CLASSIFY_RIDER : {
                suppPoint = pAsssion;
                break;
            }
            case GameConstant.CLASSIFY_SABER : {
                suppPoint = pArcher;
                break;
            }
            default : {
                throw new Exception("no such ap room!");
            }
        }
        return suppPoint;
    }

    public Point getpSetLoc() {
        return pSetLoc;
    }

    public void setpSetLoc(Point pSetLoc) {
        this.pSetLoc = pSetLoc;
    }

    public Point getpAp10() {
        return pAp10;
    }

    public void setpAp10(Point pAp10) {
        this.pAp10 = pAp10;
    }

    public Point getpAp20() {
        return pAp20;
    }

    public void setpAp20(Point pAp20) {
        this.pAp20 = pAp20;
    }

    public Point getpAp30() {
        return pAp30;
    }

    public void setpAp30(Point pAp30) {
        this.pAp30 = pAp30;
    }

    public Point getpAp40() {
        return pAp40;
    }

    public void setpAp40(Point pAp40) {
        this.pAp40 = pAp40;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }
}
