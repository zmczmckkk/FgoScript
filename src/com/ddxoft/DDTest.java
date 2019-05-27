package com.ddxoft;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class DDTest {
	 public static void main(String[] args) {
		 System.out.println("���Կ�ʼ");
		 
		 //DD.INSTANCE.DD_mov(500, 500);   //�����ƶ�
		 //DD.INSTANCE.DD_movR(100, 100); //����ƶ�
		 //DD.INSTANCE.DD_btn(4);DD.INSTANCE.DD_btn(8); //����Ҽ�
		 DD.INSTANCE.DD_key(601, 1);DD.INSTANCE.DD_key(601, 2); //����win
		 DD.INSTANCE.DD_str("123@AbC"); //�ַ���
	 }

	 public interface DD extends Library {
		   DD INSTANCE = (DD)Native.loadLibrary("DD64", DD.class);
		   //64λJAVA����DD64.dll, 32λ����DD32.dll ����ϵͳ����λ���޹ء���
		   public int DD_mov(int x, int y);
		   public int DD_movR(int dx, int dy);
		   public int DD_btn(int btn);
		   public int DD_whl(int whl);
		   public int DD_key(int ddcode, int flag);
		   public int DD_str(String s);  
	 }
}
