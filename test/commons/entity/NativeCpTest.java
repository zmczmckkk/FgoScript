package commons.entity;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class NativeCpTest extends NativeCp {

    @Test
    public void getUserDirTest() {
        String dir = super.getUserDir();
        File file = new File(dir);
        assertTrue(file.exists());
        //打开目录
        try {
            Runtime.getRuntime().exec("explorer " + dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserNameTest() {
        String username = super.getUserName();
        assertTrue(getUserDir().contains(username));
    }
}