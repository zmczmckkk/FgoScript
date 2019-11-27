package destinychild;

/**
 * @description: 百鬼夜行自动按键接口
 * @author: RENZHEHAO
 * @create: 2019-06-15 08:55
 **/
public interface IModule {
    /**
     * @Description: 启动方法
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/15
     */
    public void start();
    /**
     * @Description: 终止方法
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/15
     */
    public void stop();
    /**
     * @Description: 切换灯方法
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/15
     */
    public void toggle();
}
