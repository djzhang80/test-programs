/*
 * ��Ŀ���ƣ�ˮ��ģ���Զ��ʶ��Ƽ���ϵͳ
 * ���̣�CAAS
 * ���ߣ��ŵ½�������ΰ
 * �ļ���StreamDrainer.java
 * ������cn.edu.fjnu.hadoop
 * CopyRright (c) 2013-10-4
 */




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


// TODO: Auto-generated Javadoc
/**
 * The Class StreamDrainer.
 */
class StreamDrainer implements Runnable {
	
	/** The ins. */
	private InputStream ins;

	/**
	 * Instantiates a new stream drainer.
	 *
	 * @param ins the ins
	 */
	public StreamDrainer(InputStream ins) {
		this.ins = ins;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(ins));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

