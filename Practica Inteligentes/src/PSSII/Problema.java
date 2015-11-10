package PSSII;

import java.util.LinkedList;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.LinkedList;

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

	/*public LinkedList<NodoAdyacente> Sucesores(long idNodo) throws Exception {
		LinkedList<NodoAdyacente> adyacentes = null;
		if (cGrafo.getTablaNodos().containsKey(idNodo)) {
			adyacentes = cGrafo.getNodo(idNodo).getNodosAdyacentes();
			Random rndm = new Random();
			rndm.setSeed(1000);
			Collections.shuffle(adyacentes, rndm);
		}
		return adyacentes;
	}*/

	public LinkedList<Estado> suc(Estado es) {
		LinkedList<NodoAdyacente> v = es.getIdO().getNodosAdyacentes();
		LinkedList<Estado> vE = new LinkedList<Estado>();		
		for (int i = 0; i < v.size(); i++) {
			LinkedList<Nodo> v2 = new LinkedList<Nodo>();
			for (int k = 0; k < es.getIdD().size(); k++) {				
				if(cGrafo.getNodo(v.get(i).getIdA()) != es.getIdD().get(k)){
					v2.add(es.getIdD().get(k));
				}
			}
			vE.add(new Estado(cGrafo.getNodo(v.get(i).getIdA()), v2));
		}
		return vE;
	}	

	/*public LinkedList<NodoAdyacente> SucesoresNew(Estado estado) throws Exception {
		LinkedList<NodoAdyacente> adyacentes = null;		
		LinkedList<Long> visitados = null;	
		if (cGrafo.getTablaNodos().containsKey(estado.getIdO())) {
			visitados.add(estado.getIdO());			
			adyacentes = cGrafo.getNodo(estado.getIdO()).getNodosAdyacentes();
			Random rndm = new Random();
			rndm.setSeed(1000);
			Collections.shuffle(adyacentes, rndm);
			for(int i=0;i<adyacentes.size();i++){
				for(int j=0;j<visitados.size();j++){
					if(visitados.get(i)==)
						adyacentes.get(i).getIdA()
				}
			}
		}
		return adyacentes;
	}*/

	public LinkedList<NodoBusqueda> CrearListaNodos(NodoBusqueda nodoPadre, LinkedList<Estado> listaAdyacentes,
			int estrategia) {

		LinkedList<NodoBusqueda> listaNodos = new LinkedList<NodoBusqueda>();
				NodoBusqueda nodoHijo;
		Estado estado;		

		for (int i = 0; i < listaAdyacentes.size(); i++) {			
			estado = new Estado(listaAdyacentes.get(i).getIdO(), listaAdyacentes.get(i).getIdD());

			nodoHijo = new NodoBusqueda(nodoPadre, estado, nodoPadre.getCosto() + nodoPadre.getEstado().getIdO().getNodosAdyacentes().get(i).getDistancia(),
					nodoPadre.getEstado().getIdO().getNodosAdyacentes().get(i).getInformacion(), nodoPadre.getProfundidad() + 1);

			nodoHijo.getValorE(estrategia, nodoPadre);
			listaNodos.add(nodoHijo);

		}
		return listaNodos;
	}
	public boolean esValido(Estado estado){
		
		return false;
	}
	public boolean esObjetivo(Estado estado){
		boolean ok=false;
		for(int i=0;i<estado.getIdD().size();i++){
			if(estado.getIdO()==estado.getIdD().get(i)){
				ok=true;
			}
		}
		return ok;
	}
	public boolean Estado_Meta(Estado estado){
		boolean ok=false;
		if(estado.getIdD().isEmpty()){
			ok=true;
		}
		return ok;
	}

	public void Busqueda_Acotada(Estado estado, Problema problema, int max_prof, int estrategia) throws Exception {

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
					listaSucesores = suc(nodoActual.getEstado());
					listaNodos = CrearListaNodos(nodoActual, listaSucesores, estrategia);
				}
				frontera.insertarLista(listaNodos);
				System.out.println(nodoActual.getEstado().getIdO()+" "+nodoActual.getValor());
				
			}
			if (solucion)
				rutaSolucion = CrearSolucion(nodoActual);
			else
				rutaSolucion = null;
			String estra = "";
			if (estrategia == 1) {
				estra = "Anchura";
			}
			if (estrategia == 2) {
				estra = "Profundidad";
			}
			if (estrategia == 3) {
				estra = "Costo";
			}
			generartxt(rutaSolucion, estra, max_prof);
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

	public void generartxt(Stack<NodoBusqueda> solucion, String srtEstrategia, int max_prof) throws IOException {

		long id_Origen, id_Destino;
		double costo;
		int num_nodos;

		NodoBusqueda nodo;
		RandomAccessFile salida = null;
		salida = new RandomAccessFile("solucion.txt", "rw");

		num_nodos = solucion.size();
		nodo = solucion.peek();
		id_Origen = nodo.getEstado().getIdO().getId();
		salida.writeBytes("\n\nEstado --> Id Nodo Coordenadas: (Latitud Nodo, Longitud Nodo)");
		while (!solucion.isEmpty()) {
			nodo = solucion.pop();
			salida.writeBytes("\n\nEstado --> " + nodo.getEstado().getIdO().getId() + " Coordenadas: ("
					+ nodo.getEstado().getIdO().getLatitud() + ", " + nodo.getEstado().getIdO().getLongitud() + ")");
		}

		id_Destino = nodo.getEstado().getIdO().getId();
		costo = nodo.getCosto();

		salida.writeBytes("\n\nDatos de la solucion.");
		salida.writeBytes("\nNodo Origen:  " + id_Origen + ".");
		salida.writeBytes("\nNodo Destino: " + id_Destino + ".");
		salida.writeBytes("\nEstrategia:   " + srtEstrategia + ".");
		salida.writeBytes("\nProfundidad Maxima:   " + max_prof + ".");
		salida.writeBytes("\nCoste de la Solucion: " + costo + " metros.");
		salida.writeBytes("\nComplejidad Espacial: " + num_nodos + " nodos.");
		salida.close();
		System.out.println("Se ha generado correctamente el fichero .txt");
	}
}
