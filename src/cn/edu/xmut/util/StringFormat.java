package cn.edu.xmut.util;

public class StringFormat {
	public static String fixLenConvert(int val, int len, String pad)
	{
		String tString = "" + val;
		int kk = len - tString.length();
		for (int j = 0; j < kk; j++) {
			tString = pad + tString;
		}
		return tString;
	}
}
