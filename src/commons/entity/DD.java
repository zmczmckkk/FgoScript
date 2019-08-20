package commons.entity;

import com.ddxoft.DDTest;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DD extends Library {
    DDTest.DD INSTANCE = (DDTest.DD)Native.loadLibrary("HIVKDll64", DDTest.DD.class);
    public int HIVM_MOV(int x, int y);
    public int HIVM_MOVR(int dx, int dy);
    public int HIVM_BTN(int btn);
    public int HIVM_WHL(int whl);
    public int HIVK_KEY(int ddcode, int flag);
}
