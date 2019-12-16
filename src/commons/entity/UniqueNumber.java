package commons.entity;

/**
 * @author RENZHEHAO
 * @version 1.0.0
 * @ClassName UniqueNumber.java
 * @Description TODO
 * @createTime 2019年12月15日 16:45:00
 */
public class UniqueNumber {
    private static int num = 0;
    public static int getOneUniqueNumber(){
        int newNumber = num;
        num+=1;
        return newNumber;
    }
}
