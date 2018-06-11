package cn.edu.xmut.parameteredit;
public class ParameterUtil {

	public static double transfort(String parameter, double oldvalue,
			double changes) {

		if (parameter.startsWith("r")) {

			return oldvalue * (1 + changes);

		} else if (parameter.startsWith("a")) {

			return oldvalue + changes;

		} else if (parameter.startsWith("v")) {

			return changes;

		} else {

			return oldvalue;

		}

	}

	public static String doubleToString(double in,int subfix) {

		return String.format("%."+subfix+"f", in);

	}
}
