package cn.edu.xmut.parameteredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class SOLFileEditor extends FileEditor {
	private  String backuppath;
	private  String modelpath;
	private  String subfix;

	public SOLFileEditor(String path,String backup) {
		modelpath = path;
		backuppath = backup;
		subfix = "sol";
	}

	public void modifySOLFiles(String[] parameter, double[] tindex) {

		try {
			// delete all related *.mgt files in the directory
			File dir = new File(modelpath);
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(subfix) && !name.startsWith("output");
				}
			};
			File[] files = dir.listFiles(filter);
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			File backupdir = new File(backuppath);
			File[] backupfiles = backupdir.listFiles(filter);
			OutputStream oStream = null;
			FileInputStream in2 = null;
			for (int i = 0; i < backupfiles.length; i++) {
				try {
					in2 = new FileInputStream(backupfiles[i]);
					List lines = IOUtils.readLines(in2);
					// =========================================
					// multi parameters need to be modified
					for (int j = 0; j < parameter.length; j++) {
						if (parameter[j].contains("SOL_Z")) {
							// 8
							// Depth                [mm]:      100.00      250.00      420.00      800.00
							int line = 7;
							String tp = (String) lines.get(line);
							if (parameter[j].contains("1")) {

								//" Depth                [mm]:      100.00".length()
								String backpart = tp.substring(39);
								//" Depth                [mm]:".length()
								String startpart = tp.substring(0, 27);

								String midpart = tp.substring(27, 39).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("2")) {

								//" Depth                [mm]:      100.00      250.00".length()
								String backpart = tp.substring(51);
								//" Depth                [mm]:      100.00".length()
								String startpart = tp.substring(0, 39);

								String midpart = tp.substring(39, 51).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("3")) {

								//" Depth                [mm]:      100.00      250.00      420.00".length()
								String backpart = tp.substring(63);
								//" Depth                [mm]:      100.00      250.00".length()
								String startpart = tp.substring(0, 51);

								String midpart = tp.substring(51, 63).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("4")) {

								//" Depth                [mm]:      100.00      250.00      420.00      800.00".length()
								String backpart = tp.substring(75);
								//" Depth                [mm]:".length()
								String startpart = tp.substring(0, 63);

								String midpart = tp.substring(63, 75).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							}

						} else if (parameter[j].contains("SOL_AWC")) {

							// 10
							// Ave. AW Incl. Rock Frag  :        0.15        0.15        0.16        0.15
							int line = 9;
							String tp = (String) lines.get(line);
							if (parameter[j].contains("1")) {

								String backpart = tp.substring(39);
								String startpart = tp.substring(0, 27);

								String midpart = tp.substring(27, 39).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("2")) {

								String backpart = tp.substring(51);
								String startpart = tp.substring(0, 39);

								String midpart = tp.substring(39, 51).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("3")) {

								String backpart = tp.substring(63);
								String startpart = tp.substring(0, 51);

								String midpart = tp.substring(51, 63).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("4")) {

								String backpart = tp.substring(75);
								String startpart = tp.substring(0, 63);

								String midpart = tp.substring(63, 75).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else {
								//all
								String startpart = tp.substring(0, 27);
								String backpart = tp.substring(27);

								backpart = backpart.trim();

								String list[] = backpart.split("\\s+");
								for (int k = 0; k < list.length; k++) {
									double v = Double.parseDouble(list[k]);
									v = ParameterUtil.transfort(parameter[j],
											v, tindex[j]);
									String vsString = ParameterUtil
											.doubleToString(v, 2);
									startpart = startpart
											+ "            ".substring(vsString
													.trim().length())
											+ vsString.trim();

								}

								lines.set(line, startpart);

							}

						} else if (parameter[j].contains("SOL_K")) {

							// 11
							// Ksat. (est.)      [mm/hr]:        3.56        3.56        2.54        0.50
							int line = 10;
							String tp = (String) lines.get(line);
							if (parameter[j].contains("1")) {

								String backpart = tp.substring(39);
								String startpart = tp.substring(0, 27);

								String midpart = tp.substring(27, 39).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("2")) {

								String backpart = tp.substring(51);
								String startpart = tp.substring(0, 39);

								String midpart = tp.substring(39, 51).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("3")) {

								String backpart = tp.substring(63);
								String startpart = tp.substring(0, 51);

								String midpart = tp.substring(51, 63).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else if (parameter[j].contains("4")) {

								String backpart = tp.substring(75);
								String startpart = tp.substring(0, 63);

								String midpart = tp.substring(63, 75).trim();

								double v = Double.parseDouble(midpart);
								v = ParameterUtil.transfort(parameter[j], v,
										tindex[j]);
								String vsString = ParameterUtil.doubleToString(
										v, 2);

								String padded = startpart
										+ "            ".substring(vsString
												.trim().length())
										+ vsString.trim() + backpart;
								tp = padded;
								lines.set(line, padded);

							} else {
								String startpart = tp.substring(0, 27);
								String backpart = tp.substring(27);

								backpart = backpart.trim();

								String list[] = backpart.split("\\s+");
								for (int k = 0; k < list.length; k++) {
									double v = Double.parseDouble(list[k]);
									v = ParameterUtil.transfort(parameter[j],
											v, tindex[j]);
									String vsString = ParameterUtil
											.doubleToString(v, 2);
									startpart = startpart
											+ "            ".substring(vsString
													.trim().length())
											+ vsString.trim();

								}

								lines.set(line, startpart);
							}

						}
					}
					// ==================================================
					oStream = new FileOutputStream(modelpath
							+ backupfiles[i].getName());
					IOUtils.writeLines(lines, null, oStream);
				} catch (Exception e) {
					// logDebugInfo(e.toString());
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(in2);
					IOUtils.closeQuietly(oStream);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String t = " Depth                [mm]:      100.00      250.00      420.00      800.00";
		System.out.println(t.length());
		System.out.println(t.substring(t.length()));
	}

	@Override
	public void modifyFiles(String[] parameter, double[] tindex) {
		this.modifySOLFiles(parameter, tindex);

	}
}
