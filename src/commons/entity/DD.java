package commons.entity;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DD extends Library {
    public static DD getInstance(){
        DD INSTANCE = (DD)Native.loadLibrary("HIVKDll64", DD.class);
        return INSTANCE;
    }
    DD INSTANCE = (DD)Native.loadLibrary("HIVKDll64", DD.class);

    public int HIVM_MOV(int x, int y);
    public int HIVM_MOVR(int dx, int dy);
    public int HIVM_BTN(int btn);
    public int HIVM_WHL(int whl);
    public int HIVK_KEY(int ddcode, int flag);
}
