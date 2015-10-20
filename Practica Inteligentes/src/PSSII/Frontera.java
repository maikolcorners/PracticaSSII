package PSSII;
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

	public boolean esVacia() {
		return nodosFrontera.isEmpty();
	}

	public NodoBusqueda getPrimerN() {
		return nodosFrontera.poll();
	}

}
