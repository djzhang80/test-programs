package cn.edu.xmut.latinhypercube;

public class ParameterInfo {
	private String name;
	private double startPos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getStartPos() {
		return startPos;
	}

	public void setStartPos(double startPos) {
		this.startPos = startPos;
	}

	public double getEndPos() {
		return endPos;
	}

	public void setEndPos(double endPos) {
		this.endPos = endPos;
	}

	private double endPos;

}
