package PSSII;

public class NodoAdyacente {
	private long idA;
	private double distancia;
	private String informacion;
	public NodoAdyacente(long idA, double distancia, String informacion) {	
		this.idA=idA;
		this.distancia=distancia;
		this.informacion = informacion;
		
	}	
	public long getIdA() {
		return idA;
	}
	public void setIdA(long idA) {
		this.idA = idA;
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
	
}
