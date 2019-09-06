package com.ddxoft;
import com.sun.jna.Library;
import com.sun.jna.Native;
import commons.entity.NativeCp;
import fgoScript.constant.GameConstant;

public class DDTest {
	 public static void main(String[] args) {
		 DD.INSTANCE.HIVK_KEY(601, 1);DD.INSTANCE.HIVK_KEY(601, 2);
		 DDTest.DD.INSTANCE.HIVM_BTN(1);
		 DDTest.DD.INSTANCE.HIVM_BTN(2);
	 }

	 public interface DD extends Library {
		   DD INSTANCE = (DD)Native.loadLibrary("HIVKDll64", DDTest.DD.class);
		   public int HIVM_MOV(long x, long y);
		   public int HIVM_MOVR(long dx, long dy);
		   public int HIVM_BTN(int btn);
		   public int HIVM_WHL(int whl);
		   public int HIVK_KEY(int ddcode, int flag);
	 }
}
