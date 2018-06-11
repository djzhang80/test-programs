package cn.edu.xmut.latinhypercube;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class LatinHyperCube {
	
	public static List<String> paraList;

	public static double[][] getParemeterValues(String filename, int simpleCount) {
		FileReader filereader = null;

		try {
			filereader = new FileReader(new File(filename));
			List<String> paraInfoList = IOUtils.readLines(filereader);
			paraList=paraInfoList;
			int dim = paraInfoList.size();
			double[][] results = new double[simpleCount][dim];

			ArrayList<ParameterInfo> parameterInfoset = new ArrayList<ParameterInfo>();
			// initial parainfo set
			for (int i = 0; i < paraInfoList.size(); i++) {
				String paraInfo = paraInfoList.get(i);
				String[] paraInfos = paraInfo.split("\\s+");

				ParameterInfo pi = new ParameterInfo();
				pi.setName(paraInfos[0]);
				pi.setStartPos(Double.parseDouble(paraInfos[1]));
				pi.setEndPos(Double.parseDouble(paraInfos[2]));
				parameterInfoset.add(pi);
			}

			for (int i = 0; i < dim; i++) {

				int[] oneDim = getOneDim(simpleCount);
				ParameterInfo vParameterInfo = parameterInfoset.get(i);

				for (int j = 0; j < simpleCount; j++) {
					int c = oneDim[j];

					results[j][i] = ((c - Math.random()) / simpleCount)
							* (vParameterInfo.getEndPos() - vParameterInfo
									.getStartPos())
							+ vParameterInfo.getStartPos();

				}
			}

			return results;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(filereader);
		}
		return null;

	}

	public static int[] getOneDim(int N) {
		int[] res = new int[N];
		int i, k;
		// initialization
		for (i = 0; i < N; i++)
			res[i] = i + 1;
		// permutation
		for (i = 0; i < N - 1; i++) {
			k = i + (int) (Math.random() * (N - i));
			int temp = res[i];
			res[i] = res[k];
			res[k] = temp;
		}
		return res;
	}

	public static void genParameterValFile(String input, String output, int sampleCount) {

		File file = new File(output);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);

			double[][] results = getParemeterValues(input, sampleCount);

			List<String> list = new ArrayList<String>();

			for (int i = 0; i < results.length; i++) {
				double[] ps = results[i];
				String t = (i + 1) + " ";

				for (int j = 0; j < ps.length; j++) {
					DecimalFormat df = new DecimalFormat("###.######");
					String cc = df.format(ps[j]) + "";
					t = t + cc + " ";
				}

				list.add(t);

			}

			IOUtils.writeLines(list, null, fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);

		}

	}

	public static List<String> genParameterVal(String input, String output,
			int sampleCount) {

		double[][] results = getParemeterValues(input, sampleCount);

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < results.length; i++) {
			double[] ps = results[i];
			String t = (i + 1) + " ";

			for (int j = 0; j < ps.length; j++) {
				DecimalFormat df = new DecimalFormat("###.######");
				String cc = df.format(ps[j]) + "";
				t = t + cc + " ";
			}

			list.add(t);

		}

		return list;

	}

	public static void main(String[] args) {

		LatinHyperCube lhc = new LatinHyperCube();
		/*
		 * double[][] results = lhc.getParemeterValues(
		 * "F:\\workshop\\par_inf.txt", 100000);
		 * 
		 * for (int i = 0; i < results.length; i++) { double[] ti = results[i];
		 * for (int j = 0; j < ti.length; j++) { System.out.print(ti[j]);
		 * System.out.print("  "); } System.out.println();
		 * 
		 * }
		 */

		// lhc.genParameterValFile("C:\\Users\\Administrator\\Desktop\\率定数据收集\\率定数据收集\\par_inf5.txt",
		// "C:\\Users\\Administrator\\Desktop\\率定数据收集\\率定数据收集\\par_val6.txt",
		// 2000);
		// lhc.genParameterValFile("F:\\workshop\\par_inf.txt",
		// "F:\\workshop\\par_val20000.txt", 10000);
		lhc.genParameterValFile("F:\\cy\\para_n_inf.txt",
				"F:\\cy\\para_n_val20141214.txt", 30000);
		// lhc.genParameterValFile("F:\\加拿大项目\\database\\采样程序\\para_n_inf.txt",
		// "F:\\加拿大项目\\database\\采样程序\\para_n_val.txt", 109000);

	}

}
