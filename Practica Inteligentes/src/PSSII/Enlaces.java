package PSSII;

public class Enlaces {
	private Nodo nodo1;
	private Nodo nodo2;
	private double distancia;
	private String informacion;
	public Enlaces(Nodo nodo1, Nodo nodo2, String informacion) {	
		this.nodo1=nodo1;
		this.nodo2=nodo2;
		distancia = calcularDistancia();
		this.informacion = informacion;
		
	}	
	public long getId() {
		return nodo2.getId();
	}	
	public double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	public String getInformacion() {
		return informacion;
	}
	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}
	public double calcularDistancia() {
		int R = 6371;

		double latDistance = toRad(nodo2.getLatitud() - nodo1.getLatitud());
		double lonDistance = toRad(nodo2.getLongitud()-nodo1.getLongitud());
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(toRad(nodo1.getLatitud())) * Math.cos(toRad(nodo2.getLatitud())) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		return distance;

	}

	private static Double toRad(double value) {
		return value * Math.PI / 180;
	}
	
}
