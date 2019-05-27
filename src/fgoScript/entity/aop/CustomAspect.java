package fgoScript.entity.aop;

/**
 * @description: 定义一个切面类CustomAspect
 * @author: RENZHEHAO
 * @create: 2019-05-23 07:52
 **/
public class CustomAspect {
    public void before() {
        System.out.println("Before custom operation");
    }

    public void after() {
        System.out.println("After custom operation");
    }
}
