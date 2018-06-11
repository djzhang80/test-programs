package cn.edu.xmut.parameteredit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public abstract class FileEditor {
	public  void modifyFiles(HashMap pmap,String[] inputs){
		
		if (pmap != null) {

			Set keys = pmap.keySet();

			if (keys != null && keys.size() > 0) {
				String[] parameter = new String[keys.size()];
				double[] tindex = new double[keys.size()];

				Iterator keysiter = keys.iterator();
				int i = 0;
				for (Object key : keys) {
					int v = (Integer) pmap.get(key);
					parameter[i] = (String) key;
					tindex[i] = Double.parseDouble(inputs[v + 1]);
					i++;

				}
				modifyFiles(parameter, tindex);
			}

		}
		
	}
	public abstract void modifyFiles(String[] parameter, double[] tindex);

}
