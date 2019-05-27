package fgoScript.entity.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: Test the proxy
 * @author: RENZHEHAO
 * @create: 2019-05-23 03:10
 **/
public class testProxy implements InvocationHandler {
    private Object delegate;
    public testProxy(){
    }
    public testProxy(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在真实的对象执行之前我们可以添加自己的操作
        System.out.println("before invoke。。。");
        if (proxy instanceof People) {
            System.out.println("proxy is instance of People!");
        }else {
            System.out.println("proxy is not instance of People!");
        }
        Object invoke = method.invoke(delegate, args);
        //在真实的对象执行之后我们可以添加自己的操作
        System.out.println("after invoke。。。");
        return invoke;
    }
}
interface Thing{
    public abstract String work();
    public abstract String play();

}
interface People extends Thing{
};
class Teacher implements People{
    @Override
    public String work() {
        return "teacher is working!";
    }

    @Override
    public String play() {
        return "teacher is playing!";
    }
}
interface Animal extends Thing{
}
class Dog implements Animal{
    @Override
    public String work() {
        return "Dog is working!";
    }

    @Override
    public String play() {
        return "Dog is playing!";
    }
}
class Test {

    public static void main(String[] args) {
        //要代理的真实对象
        Object obj1 = new Teacher();
        Object obj2 = new Dog();
        //代理对象的调用处理程序，我们将要代理的真实对象传入代理对象的调用处理的构造函数中，最终代理对象的调用处理程序会调用真实对象的方法
        testMethod(obj1);
        testMethod(obj2);

    }
    private static void testMethod(Object obj){
        InvocationHandler handler = new testProxy(obj);
        /**
         * 通过Proxy类的newProxyInstance方法创建代理对象，我们来看下方法中的参数
         * 第一个参数：people.getClass().getClassLoader()，使用handler对象的classloader对象来加载我们的代理对象
         * 第二个参数：people.getClass().getInterfaces()，这里为代理类提供的接口是真实对象实现的接口，这样代理对象就能像真实对象一样调用接口中的所有方法
         * 第三个参数：handler，我们将代理对象关联到上面的InvocationHandler对象上
         */
        Object proxyObj = Proxy.newProxyInstance(handler.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);;

        Thing thing = (Thing) proxyObj;
        //System.out.println(proxy.toString());
        System.out.println(thing.work());
        System.out.println(thing.play());
    }

}
