package PSSII;

import java.util.LinkedList;

public class EspacioEstados {
	private double latmax;
	private double latmin;
	private double lonmax;
	private double lonmin;
	private CrearGrafo cG;
	public EspacioEstados(CrearGrafo cG,double latmax, double latmin, double lonmax, double lonmin) {		
		this.cG=cG;
		this.latmax = latmax;
		this.latmin = latmin;
		this.lonmax = lonmax;
		this.lonmin = lonmin;
	}
	public CrearGrafo getcG() {
		return cG;
	}
	public void setcG(CrearGrafo cG) {
		this.cG = cG;
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
	public LinkedList<Estado> suc(Estado es) {
		LinkedList<NodoAdyacente> v = es.getIdO().getNodosAdyacentes();
		LinkedList<Estado> vE = new LinkedList<Estado>();
		for (int i = 0; i < v.size(); i++) {
			LinkedList<Nodo> v2 = new LinkedList<Nodo>();
			for (int k = 0; k < es.getIdD().size(); k++) {
				if (cG.getNodo(v.get(i).getIdA()) != es.getIdD().get(k)) {
					v2.add(es.getIdD().get(k));
				}				
			}
			vE.add(new Estado(cG.getNodo(v.get(i).getIdA()), v2));
		}
		return vE;
	}
	public boolean esValido(Estado estado) {

		return false;
	}

	public boolean esObjetivo(Estado estado) {
		boolean ok = false;
		for (int i = 0; i < estado.getIdD().size(); i++) {
			if (estado.getIdO() == estado.getIdD().get(i)) {
				ok = true;
			}
		}
		return ok;
	}
}
