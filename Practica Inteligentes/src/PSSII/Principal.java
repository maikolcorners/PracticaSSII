package PSSII;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.v0_6.XmlDownloader;

public class Principal {
	static CrearGrafo cGrafo = new CrearGrafo();
	static Problema problema;
	static Scanner leer = new Scanner(System.in);
	private static double lonmin;
	private static double latmax;
	private static double latmin;
	private static double lonmax;
	private static long tini;
	private static long tiempo;

	static public void main(String[] args) throws Exception {
		Sink sinkImplementation = new Sink() {
			public void initialize(Map<String, Object> metaData) {
			};

			public void process(EntityContainer entityContainer) {
				Nodo nodo;
				Entity entity = entityContainer.getEntity();
				if (entity instanceof Node) {
					nodo = new Nodo(((Node) entity).getId(), ((Node) entity).getLatitude(),
							((Node) entity).getLongitude());
					cGrafo.getTablaNodos().put(nodo.getId(), nodo);
				} else if (entity instanceof Way) {
					Iterator<Tag> it = entity.getTags().iterator();
					Tag t;
					String highway = "", info = "";
					while (it.hasNext()) {
						t = it.next();
						if (t.getKey().equals("name"))
							info = t.getValue();
						if (t.getKey().equals("highway"))
							highway = t.getValue();
					}
					if (highway.equals("pedestrian") || highway.equals("trunk") || highway.equals("residential")) {
						addEnlace((Way) entity, info); // Tarea 2
					}
				}
			}

			private void addEnlace(Way entity, String informacion) {
				List<WayNode> ListaWayNodes = entity.getWayNodes();
				NodoAdyacente nAdyacente;
				WayNode waynode1, waynode2;
				double distancia;
				waynode1 = ListaWayNodes.get(0);
				Nodo nodoP, nodoA;
				for (int i = 1; i < ListaWayNodes.size(); i++) {
					waynode2 = ListaWayNodes.get(i);
					nodoP = cGrafo.getNodo(waynode1.getNodeId());
					nodoA = cGrafo.getNodo(waynode2.getNodeId());
					distancia = nodoP.calcularDistanciaNodoAdy(nodoA);
					nAdyacente = new NodoAdyacente(nodoA.getId(), distancia, informacion);
					cGrafo.getNodo(nodoP.getId()).getNodosAdyacentes().add(nAdyacente);
					nAdyacente = new NodoAdyacente(nodoP.getId(), distancia, informacion);
					cGrafo.getNodo(nodoA.getId()).getNodosAdyacentes().add(nAdyacente);
					waynode1 = waynode2;
				}
			}

			public void release() {
			}

			public void complete() {
			}

		};
		System.out.println("Coordenadas del Espacio de Estado:");
		System.out.println("Introduzca Latitud Maxima ");
		latmax = -3.9326000;
		System.out.println("Introduzca Longitud Minima ");
		lonmin = 38.9836000;
		System.out.println("Introduzca Latitud Minima: ");
		latmin = -3.9217000;
		System.out.println("Introduzca Latitud Maxima: ");
		lonmax = 38.9883900;

		RunnableSource reader = new XmlDownloader(latmax, latmin, lonmax, lonmin,
				"http://www.openstreetmap.org/api/0.6");
		System.out.println("El mapa ha sido descargado.");

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		System.out.println("Se estan procesando los datos del archivo xml...");
		readerThread.start();
		System.out.println("Comenzamos");
		// Fin Tarea 1

		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				/* No hacer nada */
			}
		}
		System.out.println("Selecciona las siguientes opciones:\n" + "1.Tarea3.\n" + "2.Tarea4.\n" + "3.Tarea5.\n");
		int opc = leer.nextInt();

		System.out.print("Id Nodo origen: \n");
		long nodoO = leer.nextLong();
		LinkedList<Nodo> listaNodoR = new LinkedList<Nodo>();
		System.out.print("Introduzca el numero de nodos objetivos.\n");
		int numNR = leer.nextInt();
		for (int i = 0; i < numNR; i++) {
			System.out.print("Id Nodo Objetivo:\n");
			long nodoD = leer.nextLong();
			listaNodoR.add(cGrafo.getNodo(nodoD));
		}

		switch (opc) {
		case 1:
			tarea3(nodoO, listaNodoR);
			break;
		case 2:
			tarea4(nodoO, listaNodoR);
			break;
		case 3:
			tarea5(nodoO, listaNodoR);
			break;
		}
	}

	public static void tarea5(long nodoO, LinkedList<Nodo> listaNodoR) throws Exception {
		System.out.println("Tipo de estrategia:\n" + "1.En Anchura.\n" + "2.En Profundidad.\n" + "3.En Costo.\n"
				+ "4.En Profundida Iterativa.\n" + "5.En Voraz.\n" + "6.A*\n");
		int estrategia = leer.nextInt();
		System.out.println("Profundidad maxima.\n");
		int prof = leer.nextInt();
		EspacioEstados ee = new EspacioEstados(cGrafo, latmax, latmin, lonmax, lonmin);
		problema = new Problema(ee);
		Estado estadoinicial = new Estado(cGrafo.getNodo(nodoO), listaNodoR);
		tini = System.nanoTime();
		Stack<NodoBusqueda> solucion = problema.Busqueda_Acotada(estadoinicial, problema, prof, estrategia);
		tiempo = System.nanoTime() - tini;
		ImprimirSolucion iS = new ImprimirSolucion(solucion, estrategia, prof);
		iS.generargpx(tiempo);
	}

	public static void tarea4(long nodoO, LinkedList<Nodo> listaNodoR) throws Exception {
		System.out.println("Tipo de estrategia:\n" + "1.En Anchura.\n" + "2.En Profundidad.\n" + "3.En Costo.\n"
				+ "4.En Profundida Iterativa.\n");
		int estrategia = leer.nextInt();
		System.out.println("Profundidad maxima.\n");
		int prof = leer.nextInt();
		EspacioEstados ee = new EspacioEstados(cGrafo, latmax, latmin, lonmax, lonmin);
		problema = new Problema(ee);
		Estado estadoinicial = new Estado(cGrafo.getNodo(nodoO), listaNodoR);
		Stack<NodoBusqueda> solucion = problema.Busqueda_Acotada(estadoinicial, problema, prof, estrategia);
		ImprimirSolucion iS = new ImprimirSolucion(solucion, estrategia, prof);
		iS.generartxt();

	}

	public static void tarea3(long nodoO, LinkedList<Nodo> listaNodoR) throws Exception {
		EspacioEstados ee = new EspacioEstados(cGrafo, latmax, latmin, lonmax, lonmin);

		LinkedList<Nodo> nodoR = new LinkedList<Nodo>();
		for (int i = 0; i < listaNodoR.size(); i++) {
			nodoR.add(cGrafo.getNodo(listaNodoR.get(i).getId()));
		}
		Estado estadoinicial = new Estado(cGrafo.getNodo(nodoO), nodoR);
		LinkedList<Estado> sucesores = ee.suc(estadoinicial);

		for (int i = 0; i < sucesores.size(); i++) {
			System.out.print("Nodo origen: " + nodoO + " --> [" + sucesores.get(i).getIdO().getId() + ",[");
			for (int j = 0; j < sucesores.get(i).getIdD().size(); j++) {
				System.out.print(" " + sucesores.get(i).getIdD().get(j).getId() + " ");
			}
			System.out.println("]]," + cGrafo.getNodo(nodoO).getNodosAdyacentes().get(i).getDistancia() + "");
		}

	}
}