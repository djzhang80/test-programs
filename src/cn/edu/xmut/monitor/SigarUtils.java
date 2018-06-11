package cn.edu.xmut.monitor;

import org.hyperic.sigar.Sigar;

import cn.edu.xmut.entry.ConfigInfo;

public class SigarUtils {
	public final static Sigar sigar = initSigar();

	private static Sigar initSigar() {
		try {
			// �˴�ֻΪ�õ��������ļ���Ŀ¼���ɸ���ʵ����Ŀ�Զ���
			String path = System.getProperty("java.library.path");
			String sigarLibPath =ConfigInfo.sigarLibPath;
			// Ϊ��ֹjava.library.path�ظ��ӣ��˴��ж���һ��
			if (!path.contains(sigarLibPath)) {
				if (isOSWin()) {
					path += ";" + sigarLibPath;
				} else {
					path += ":" + sigarLibPath;
				}
				System.setProperty("java.library.path", path);
			}
			return new Sigar();
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isOSWin() {// OS �汾�ж�
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0) {
			return true;
		} else
			return false;
	}
}