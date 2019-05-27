package fgoScript.entity.guda;

import com.sun.xml.internal.bind.v2.TODO;
import commons.util.GameUtil;

public class ApGudaziFactory {
    private static int[] getFgoArray() {
        return  GameUtil.strToIntArray(GameUtil.getValueFromConfig("FgoArray"),false);
    }

    private static int[] getEventArray() {
        return GameUtil.strToIntArray(GameUtil.getValueFromConfig("EventArray"),false);
    }

    private static int[] getApArray() {
        return GameUtil.strToIntArray(GameUtil.getValueFromConfig("apArray"),false);
    }
    private static int[] getQpArray() {
        return GameUtil.strToIntArray(GameUtil.getValueFromConfig("qpArray"),false);
    }
    private static int[] getExpArray() {
        return GameUtil.strToIntArray(GameUtil.getValueFromConfig("expArray"),false);
    }

    private static int[] getMainArray() {
        return GameUtil.strToIntArray(GameUtil.getValueFromConfig("mainArray"),false);
    }

    /**
     *
     * @param workType
     * @param accountType
     * @param apArray 可以为空
     * @return
     */
    public static InterfaceApGudazi getInstance(String workType,String accountType,int[] apArray){
        InterfaceApGudazi apGudazi;
        //设置工作类型
        switch(workType){
            case "train" : {
                switch(accountType){
                    case "big" : {
                        apGudazi = new TrainApGudaziForMain();
                        break;
                    }
                    case "small" : {
                        apGudazi = new TrainApGudazi();
                        break;
                    }
                    default : {
                        apGudazi = null;
                    }
                }
                break;
            }
            case "SPtrain" : {
                switch(accountType){
                    case "big" : {
                        apGudazi = new TrainApGudaziForMainSpecial();
                        break;
                    }
                    case "small" : {
                        apGudazi = new TrainApGudaziForLittleSpecial();
                        break;
                    }
                    default : {
                        apGudazi = null;
                    }
                }
                break;
            }
            case "qp" : {
                apGudazi = new QpApGudazi();
                break;
            }
            case "SPqp" : {
                apGudazi = new QpApGudaziForSpecial();
                break;
            }
            case "exp" : {
                switch(accountType){
                    case "big" : {
                        apGudazi = new ExpApGudazi();
                        break;
                    }
                    case "small" : {
                        apGudazi = new ExpApGudaziForMission();
                        break;
                    }
                    default : {
                        apGudazi = null;
                    }
                }

                break;
            }
            case "event" : {
                apGudazi = new EventGudazi();
                break;
            }
            default : {
                apGudazi = null;
            }
        }
        //如果apGudazi不为空设置副本和账号类型
        if (apGudazi != null) {
            //传入副本数组
            if (apArray != null) {
                apGudazi.setApArray(apArray);
            }else {
                switch(workType){
                    case "train" : {
                        apGudazi.setApArray(getApArray());
                        break;
                    }
                    case "SPtrain" : {
                        apGudazi.setApArray(getApArray());
                        break;
                    }
                    case "qp" : {
                        apGudazi.setApArray(getQpArray());
                        break;
                    }
                    case "SPqp" : {
                        apGudazi.setApArray(getQpArray());
                        break;
                    }
                    case "exp" : {
                        apGudazi.setApArray(getExpArray());
                        break;
                    }
                    case "event" : {
                        apGudazi.setApArray(getApArray());
                        break;
                    }
                    default : {
                    }
                }
            }
            //传入账号数组
            switch(accountType){
                case "big" : {
                    apGudazi.setAccountArray(getMainArray());
                    break;
                }
                case "small" : {
                    apGudazi.setAccountArray(getFgoArray());
                    break;
                }
                case "event" : {
                    apGudazi.setAccountArray(getEventArray());
                    break;
                }
                default : {
                }
            }
        }
        return apGudazi;
    }
}
