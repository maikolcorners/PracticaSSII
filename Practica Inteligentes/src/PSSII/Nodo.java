package PSSII;

import java.util.LinkedList;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;

public class Nodo {	
	private Node nodo;
	private LinkedList <Enlaces> nodosAdyacentes;
	public Nodo(Node nodo) {	
		this.nodo=nodo;
		nodosAdyacentes=new LinkedList<Enlaces>();
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
	public LinkedList<Enlaces> getNodosAdyacentes() {
		return nodosAdyacentes;
	}
	public void setNodosAdyacentes(LinkedList<Enlaces> nodosAdyacentes) {
		this.nodosAdyacentes = nodosAdyacentes;
	}
}
