package PSSII;

import java.util.LinkedList;

public class EspacioEstados {
	private double lonOeste;
	private double lonEste;
	private double latNorte;
	private double latSur;
	private CrearGrafo cG;
	public EspacioEstados(CrearGrafo cG,double lonOeste, double lonEste, double latNorte, double latSur) {		
		this.cG=cG;
		this.lonOeste = lonOeste;
		this.lonEste = lonEste;
		this.latNorte = latNorte;
		this.latSur = latSur;
	}
	public CrearGrafo getcG() {
		return cG;
	}
	public void setcG(CrearGrafo cG) {
		this.cG = cG;
	}
	public double getlonOeste() {
		return lonOeste;
	}
	public void setlonOeste(double lonOeste) {
		this.lonOeste = lonOeste;
	}
	public double getlonEste() {
		return lonEste;
	}
	public void setlonEste(double lonEste) {
		this.lonEste = lonEste;
	}
	public double getlatNorte() {
		return latNorte;
	}
	public void setlatNorte(double latNorte) {
		this.latNorte = latNorte;
	}
	public double getlatSur() {
		return latSur;
	}
	public void setlatSur(double latSur) {
		this.latSur = latSur;
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
