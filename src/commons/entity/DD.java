package commons.entity;

import com.ddxoft.DDTest;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DD extends Library {
    DDTest.DD INSTANCE = (DDTest.DD) Native.loadLibrary("DD64", DDTest.DD.class);
    //64λJAVA����DD64.dll, 32λ����DD32.dll ����ϵͳ����λ���޹ء���
    public int DD_mov(int x, int y);
    public int DD_movR(int dx, int dy);
    public int DD_btn(int btn);
    public int DD_whl(int whl);
    public int DD_key(int ddcode, int flag);
    public int DD_str(String s);
}
