package commons.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 我的spring上下文获取
 * @author: RENZHEHAO
 * @create: 2019-05-23 10:57
 **/
public class MySpringUtil {
   private static ClassPathXmlApplicationContext applicationContext;
   public static ClassPathXmlApplicationContext getApplicationContext(){
       if (applicationContext == null) {
           applicationContext = new ClassPathXmlApplicationContext("beans.xml");
       }
       return applicationContext;
   }
   private MySpringUtil(){
   }
}
