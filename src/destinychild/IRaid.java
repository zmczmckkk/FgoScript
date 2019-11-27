package destinychild;

/**
 * @description: raid战斗接口
 * @author: RENZHEHAO
 * @create: 2019-06-03 21:10
 **/
public interface IRaid {
    /**
     * @Description: raid战斗挂机启动入口
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void raidBattleStart();
    /**
     * @Description: 停止raid战斗挂机
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/3
     */
    public void raidBattleStop();
    /**
     * @Description: 切换启动关闭方法
     * @return: void
     * @Author: RENZHEHAO
     * @Date: 2019/6/15
     */
    public void toggle();
}
