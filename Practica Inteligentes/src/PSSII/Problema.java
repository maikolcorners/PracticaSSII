package PSSII;

import java.util.LinkedList;
import java.util.Stack;

public class Problema {
	private CrearGrafo cG;
	private Frontera frontera;
	private EspacioEstados ee;

	public Problema(EspacioEstados ee) {
		this.ee = ee;
		this.cG = ee.getcG();
		frontera = new Frontera();
	}
	/*
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
	*/
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

		}
		return listaNodos;
	}
	public boolean Estado_Meta(Estado estado) {
		boolean ok = false;
		if (estado.getIdD().isEmpty()) {
			ok = true;
		}
		return ok;
	}

	public Stack<NodoBusqueda> Busqueda_Acotada(Estado estado, Problema problema, int max_prof, int estrategia) throws Exception {

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
