package PSSII;

import java.util.LinkedList;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;

public class Nodo {	
	private Node nodo;
	private LinkedList <NodoAdyacente> nodosAdyacentes;
	public Nodo(Node nodo) {	
		this.nodo=nodo;
		nodosAdyacentes=new LinkedList<NodoAdyacente>();
	}
	public long getId() {
		return nodo.getId();
	}	
	public double getLongitud() {
		return nodo.getLongitude();
	}	
	public double getLatitud() {
		return nodo.getLatitude();
	}	
	public LinkedList<NodoAdyacente> getNodosAdyacentes() {
		return nodosAdyacentes;
	}
	public void setNodosAdyacentes(LinkedList<NodoAdyacente> nodosAdyacentes) {
		this.nodosAdyacentes = nodosAdyacentes;
	}
	public double calcularDistanciaNodoAdy(Nodo nodoA) {
		int R = 6371;

		double latDistance = toRad(nodoA.getLatitud() - getLatitud());
		double lonDistance = toRad(nodoA.getLongitud() - getLongitud());
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(toRad(getLatitud())) * Math.cos(toRad(nodoA.getLatitud())) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		return distance;

	}

	private static Double toRad(double value) {
		return value * Math.PI / 180;
	}
}
