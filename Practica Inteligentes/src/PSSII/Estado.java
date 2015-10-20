package PSSII;

public class Estado {
	private long id;
	private double latitud;
	private double longitud;
	public Estado(long id, double latitud, double longitud) {		
		this.id = id;
		this.latitud = latitud;
		this.longitud = longitud;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
}
