package PSSII;

public class EspacioEstados {
	private double latmax;
	private double latmin;
	private double lonmax;
	private double lonmin;
	public EspacioEstados(double latmax, double latmin, double lonmax, double lonmin) {
		super();
		this.latmax = latmax;
		this.latmin = latmin;
		this.lonmax = lonmax;
		this.lonmin = lonmin;
	}
	public double getLatmax() {
		return latmax;
	}
	public void setLatmax(double latmax) {
		this.latmax = latmax;
	}
	public double getLatmin() {
		return latmin;
	}
	public void setLatmin(double latmin) {
		this.latmin = latmin;
	}
	public double getLonmax() {
		return lonmax;
	}
	public void setLonmax(double lonmax) {
		this.lonmax = lonmax;
	}
	public double getLonmin() {
		return lonmin;
	}
	public void setLonmin(double lonmin) {
		this.lonmin = lonmin;
	}
}
