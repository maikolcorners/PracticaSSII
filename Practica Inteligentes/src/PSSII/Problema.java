package PSSII;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

public class Problema {
	private Frontera frontera;
	private EspacioEstados ee;
	private Hashtable<String, Float> tablaPoda = new Hashtable<String, Float>();

	public Problema(EspacioEstados ee) {
		this.ee = ee;
		frontera = new Frontera();
	}

	public LinkedList<NodoBusqueda> CrearListaNodos(NodoBusqueda nodoPadre, LinkedList<Estado> listaSucesores,
			int estrategia) {

		LinkedList<NodoBusqueda> listaNodos = new LinkedList<NodoBusqueda>();
		NodoBusqueda nodoHijo;
		Estado estado;

		for (int i = 0; i < listaSucesores.size(); i++) {
			estado = new Estado(listaSucesores.get(i).getIdO(), listaSucesores.get(i).getIdD());
			nodoHijo = new NodoBusqueda(nodoPadre, estado,
					nodoPadre.getCosto() + nodoPadre.getEstado().getIdO().getNodosAdyacentes().get(i).getDistancia(),
					nodoPadre.getEstado().getIdO().getNodosAdyacentes().get(i).getInformacion(),
					nodoPadre.getProfundidad() + 1);
			nodoHijo.getValorE(estrategia, nodoPadre, estado);
			listaNodos.add(nodoHijo);

		} // Prueba
		/*
		 * for(int i=0;i<listaNodos.size();i++){
		 * tablaPoda.put(listaNodos.get(i).getEstado().toString(),
		 * listaNodos.get(i).getCosto()); }//Prueba
		 */
		return listaNodos;
	}

	public boolean Estado_Meta(Estado estado) {
		boolean ok = false;
		if (estado.getIdD().isEmpty()) {
			ok = true;
		}
		return ok;
	}

	public boolean poda(LinkedList<NodoBusqueda> nodo) {
		boolean ok = false;
		for (int i = 0; i < nodo.size(); i++) {
			if (tablaPoda.containsKey(nodo.get(i).getEstado().toString())) {
				if (tablaPoda.get(nodo.get(i).getEstado().toString()) > nodo.get(i).getValor()) {
					tablaPoda.put(nodo.get(i).getEstado().toString(), nodo.get(i).getValor());
				} else {
					ok = true;
				}
			} else {
				tablaPoda.put(nodo.get(i).getEstado().toString(), nodo.get(i).getValor());
			}
		}
		return ok;
	}

	public Stack<NodoBusqueda> Busqueda_Acotada(Estado estado, Problema problema, int max_prof, int estrategia)
			throws Exception {

		try {
			Stack<NodoBusqueda> rutaSolucion;
			NodoBusqueda nodoActual = new NodoBusqueda(null, estado, 0, "", 0);
			frontera.insertar(nodoActual);
			boolean solucion = false;
			LinkedList<Estado> listaSucesores;
			LinkedList<NodoBusqueda> listaNodos = null;

			while (!solucion && frontera.getNodosFrontera().size() != 0) {
				nodoActual = frontera.getPrimerN();
				if (Estado_Meta(nodoActual.getEstado()))
					solucion = true;
				else if (nodoActual.getProfundidad() < max_prof) {
					listaSucesores = ee.suc(nodoActual.getEstado());
					listaNodos = CrearListaNodos(nodoActual, listaSucesores, estrategia);
					if (poda(listaNodos)) {
						for (int i = 0; i < listaNodos.size(); i++) {
							if (tablaPoda.get(listaNodos.get(i).getEstado().toString()) < listaNodos.get(i).getValor())
								listaNodos.remove(i);
						}

					}
					// Prueba*/
				}
				frontera.insertarLista(listaNodos);
			}
			if (solucion)
				rutaSolucion = CrearSolucion(nodoActual);
			else
				rutaSolucion = null;
			return rutaSolucion;
		} catch (Exception e) {
			throw e;
		}

	}

	private Stack<NodoBusqueda> CrearSolucion(NodoBusqueda nodo) {
		Stack<NodoBusqueda> sol = new Stack<NodoBusqueda>();
		while (nodo != null) {
			sol.push(nodo);
			nodo = nodo.getNodoBusq();
		}
		return sol;
	}

}
