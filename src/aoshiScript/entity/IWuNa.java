package aoshiScript.entity;

import com.melloware.jintellitype.JIntellitype;
import commons.util.PropertiesUtil;
import fgoScript.entity.BaseZButton;
import fgoScript.entity.panel.FgoFrame;
import fgoScript.exception.AppNeedRestartException;
import org.checkerframework.checker.units.qual.A;

/**
 * @description: 吴娜的接口
 * @author: RENZHEHAO
 * @create: 2019-06-03 20:13
 **/
public interface IWuNa {
    /**
     * @Description: 鼠标连点/鼠标点色操作
     * @return: boolean
     * @throw: 
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void alwaysClick();
    /**
     * @Description: 鼠标点色操作
     * @param fileName properties文件名
     * @param factor 点击频率系数
     * @param clickWait 是否点击后等待
     * @param relativePath 相对config路径 eg : fgo/
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void alwaysClickForStrategy(String fileName, Integer factor, boolean alwaysGo, boolean clickWait, String relativePath) throws AppNeedRestartException;
    /**
     * @Description: 配置鼠标点色操作
     * @param bt
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void configClick(BaseZButton bt);
    /**
     * @Description: 重置鼠标点色文件（通过删除）
     * @param bt
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void falshClick(BaseZButton bt);
    /**
     * @Description: 是否成功
     * @return: boolean
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public boolean isScucess();
    /**
     * @Description: 设置是否成功
     * @param scucess
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void setScucess(boolean scucess);
    /**
     * @Description: 是否可执行
     * @return: boolean
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public boolean isGO();
    /**
     * @Description: 是否执行
     * @param go
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public void setGO(boolean go);
    /**
     * @Description: 是否强制停止
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public boolean isForceStop();
    /**
     * @Description: 强制停止状态
     * @param forceStop
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public void setForceStop(boolean forceStop);
    /**
     * @Description: 最后点击时间戳
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public Long getLastClickTime();
    /**
     * @Description: 设置最后点击时间戳
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public void setLastClickTime(Long lastClickTime);
    /**
     * @Description: 设置是否抛异常
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/4
     */
    public void setIfThrowException(boolean ifThrowException);
}
