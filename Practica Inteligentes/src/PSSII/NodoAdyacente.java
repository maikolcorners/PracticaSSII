package PSSII;

public class NodoAdyacente {
	private long id;
	private double distancia;
	private String informacion;
	public NodoAdyacente(long id, double distancia, String informacion) {		
		this.id = id;
		this.distancia = distancia;
		this.informacion = informacion;
	}	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public void calcularDistancia(){
		
	}
}
