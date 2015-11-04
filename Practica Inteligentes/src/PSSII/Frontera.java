package PSSII;
import java.util.LinkedList;
import java.util.PriorityQueue;



public class Frontera {
	private PriorityQueue<NodoBusqueda>nodosFrontera=new PriorityQueue<NodoBusqueda>();
	public Frontera() {
	}		
	public void limpiar() {
		nodosFrontera.clear();
	}

	public void insertar(NodoBusqueda nodo) {
		nodosFrontera.add(nodo);
	}
	public void insertarLista(LinkedList<NodoBusqueda> listaNodos) {
		for(int i=0; i<listaNodos.size(); i++)
			insertar(listaNodos.get(i));		
	}

	public boolean esVacia() {
		return nodosFrontera.isEmpty();
	}

	public NodoBusqueda getPrimerN() {
		return nodosFrontera.poll();
	}	
	public PriorityQueue<NodoBusqueda> getNodosFrontera() {
		return nodosFrontera;
	}
	public void setNodosFrontera(PriorityQueue<NodoBusqueda> nodosFrontera) {
		this.nodosFrontera = nodosFrontera;
	}

}
