package PSSII;

import java.util.LinkedList;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Problema {
	private CrearGrafo cGrafo;
	private Frontera frontera;

	public Problema(CrearGrafo ccGrafo) {
		this.cGrafo = ccGrafo;
		frontera = new Frontera();
	}

	public CrearGrafo getGrafo() {
		return cGrafo;
	}

	public LinkedList<NodoAdyacente> Sucesores(long idNodo) throws Exception {
		LinkedList<NodoAdyacente> adyacentes = null;
		if (cGrafo.getTablaNodos().containsKey(idNodo)) {
			adyacentes = cGrafo.getNodo(idNodo).getNodosAdyacentes();
			Random rndm = new Random();
			rndm.setSeed(1000);
			Collections.shuffle(adyacentes, rndm);
		}
		return adyacentes;
	}

	public LinkedList<NodoBusqueda> CrearListaNodos(NodoBusqueda nodoPadre, LinkedList<NodoAdyacente> listaAdyacentes,
			int estrategia) {

		LinkedList<NodoBusqueda> listaNodos = new LinkedList<NodoBusqueda>();
		long idNodo;
		NodoBusqueda nodoHijo;
		Estado estado;

		for (int i = 0; i < listaAdyacentes.size(); i++) {
			idNodo = listaAdyacentes.get(i).getIdA();
			estado = new Estado(idNodo, cGrafo.getNodo(idNodo).getLatitud(), cGrafo.getNodo(idNodo).getLongitud());

			nodoHijo = new NodoBusqueda(nodoPadre, estado, nodoPadre.getCosto() + listaAdyacentes.get(i).getDistancia(),
					listaAdyacentes.get(i).getInformacion(), nodoPadre.getProfundidad() + 1);

			nodoHijo.getValorE(estrategia, nodoPadre);
			listaNodos.add(nodoHijo);

		}
		return listaNodos;
	}

	/*
	 * public Stack <NodoBusqueda> Busqueda(long idOrigen, long idDestino,
	 * Problema problema,int inc_prof, int max_prof, int estrategia) throws
	 * Exception { int act_prof=inc_prof; Stack <NodoBusqueda> solucion=null;
	 * boolean ok=false; while(act_prof<=max_prof&&!ok){
	 * solucion=Busqueda_Acotada(idOrigen, idDestino, problema, act_prof,
	 * estrategia); long a=solucion.firstElement().getEstado().getId();
	 * if(a==idDestino){ ok=true; } act_prof=act_prof+inc_prof; } return
	 * solucion; }
	 */
	public Stack<NodoBusqueda> Busqueda_Acotada(long idOrigen, long idDestino, Problema problema, int max_prof,
			int estrategia) throws Exception {

		try {
			Stack<NodoBusqueda> rutaSolucion;

			Estado estado = new Estado(idOrigen, problema.getGrafo().getNodo(idOrigen).getLatitud(),
					problema.getGrafo().getNodo(idOrigen).getLongitud());
			NodoBusqueda nodoActual = new NodoBusqueda(null, estado, 0, "", 0);
			frontera.insertar(nodoActual);
			boolean solucion = false;
			LinkedList<NodoAdyacente> listaAdyacentes;
			LinkedList<NodoBusqueda> listaNodos = null;

			while (!solucion && frontera.getNodosFrontera().size() != 0) {
				nodoActual = frontera.getPrimerN();
				if (nodoActual.getEstado().getId() == idDestino)
					solucion = true;
				else if (nodoActual.getProfundidad() < max_prof) {
					listaAdyacentes = Sucesores(nodoActual.getEstado().getId());
					listaNodos = CrearListaNodos(nodoActual, listaAdyacentes, estrategia);
				}
				frontera.insertarLista(listaNodos);
			}
			if (solucion)
				rutaSolucion = CrearSolucion(nodoActual);
			else
				rutaSolucion = null;
			String estra="";
			if(estrategia==1){
				estra="Anchura";
			}
			if(estrategia==1){
				estra="Profundidad";
			}
			if(estrategia==1){
				estra="Costo";
			}
			generargpx(rutaSolucion,estra, max_prof);
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

	public void generargpx(Stack<NodoBusqueda> solucion, String srtEstrategia, int max_prof)
			throws IOException {

		long id_Origen, id_Destino;
		double costo;
		int num_nodos;

		NodoBusqueda nodo;
		RandomAccessFile salida;
		salida = new RandomAccessFile("solucion.txt", "rw");
		for (int i = 0; i < salida.length(); i++)
			salida.writeBytes(" ");
		salida.close();
		salida = new RandomAccessFile("solucion.txt", "rw");

		num_nodos = solucion.size();
		nodo = solucion.peek();
		id_Origen = nodo.getEstado().getId();
		System.out.printf("" + nodo.getEstado().getId());

		
		while (!solucion.isEmpty()) {
			nodo = solucion.pop();
			System.out.printf(" - " + nodo.getEstado().getId());
			salida.writeBytes("\n\nEstado --> "+ nodo.getEstado().getId()+
					" Coordenadas: (" +nodo.getEstado().getLatitud()+", "
							+ nodo.getEstado().getLongitud()+")");		
		}

		id_Destino = nodo.getEstado().getId();
		costo = nodo.getCosto();

		salida.writeBytes("\n\nDatos de la solucion.");
		salida.writeBytes("\nNodo Origen:  " + id_Origen + ".");
		salida.writeBytes("\nNodo Destino: " + id_Destino + ".");
		salida.writeBytes("\nEstrategia:   " + srtEstrategia + ".");
		salida.writeBytes("\nProfundidad Maxima:   " + max_prof + ".");
		salida.writeBytes("\nCoste de la Solucion: " + costo + " metros.");
		salida.writeBytes("\nComplejidad Espacial: " + num_nodos + " nodos.");	
		System.out.println();
	}
}
