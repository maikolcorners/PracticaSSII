package PSSII;

import java.util.LinkedList;

public class Nodo {
	private long id;
	private double longitud;
	private double latitud;
	private LinkedList <NodoAdyacente> nodosAdyacentes;
	public Nodo(long id, double longitud, double latitud) {		
		this.id = id;
		this.longitud = longitud;
		this.latitud = latitud;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public LinkedList<NodoAdyacente> getNodosAdyacentes() {
		return nodosAdyacentes;
	}
	public void setNodosAdyacentes(LinkedList<NodoAdyacente> nodosAdyacentes) {
		this.nodosAdyacentes = nodosAdyacentes;
	}

}
