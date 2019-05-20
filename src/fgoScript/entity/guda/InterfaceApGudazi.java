package fgoScript.entity.guda;

import java.awt.*;

public interface InterfaceApGudazi {
    /**
     * 进入选择副本
     * @param apNum ap数字
     */
    public abstract void intoAndSelect(int apNum, int account)throws Exception;

    /**
     * 战斗并结束
     * @param rebootFlag
     */
    public abstract void fightAndStop(boolean rebootFlag, int apNum) throws Exception;

    /**
     * 选职介
     */
    public abstract Point getSuppotServant();
    /**
     * 启动fgo脚本
     */
    public abstract void startAllFgo() throws Exception;

    /**
     * 传入账号
     * @param accountArray
     */
    public abstract void setAccountArray(int[] accountArray);

    /**
     * 传入ap
     * @param apArray
     */
    public abstract void setApArray(int[] apArray);
}
