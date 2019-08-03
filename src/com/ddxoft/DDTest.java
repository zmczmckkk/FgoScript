package com.ddxoft;
import com.sun.jna.Library;
import com.sun.jna.Native;
import commons.entity.NativeCp;
import fgoScript.constant.GameConstant;

public class DDTest {
	 public static void main(String[] args) {
		 DD.INSTANCE.DD_key(601, 1);DD.INSTANCE.DD_key(601, 2);
		 DD.INSTANCE.DD_str("123@AbC");
		 DDTest.DD.INSTANCE.DD_btn(1);
		 DDTest.DD.INSTANCE.DD_btn(2);
	 }

	 public interface DD extends Library {
		   DD INSTANCE = (DD)Native.loadLibrary("DD94687.64", DDTest.DD.class);
		   public int DD_mov(int x, int y);
		   public int DD_movR(int dx, int dy);
		   public int DD_btn(int btn);
		   public int DD_whl(int whl);
		   public int DD_key(int ddcode, int flag);
		   public int DD_str(String s);
	 }
}
