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
	private static double lonOeste;
	private static double latNorte;
	private static double latSur;
	private static double lonEste;
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
						addEnlace((Way) entity, info); 
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
		System.out.println("Introduzca Longitud Oeste ");
		lonOeste = leer.nextDouble();	
		System.out.println("Introduzca Longitud Este ");
		lonEste = leer.nextDouble();
		System.out.println("Introduzca Latitud Norte: ");
		latNorte = leer.nextDouble();	
		System.out.println("Introduzca Latitud Sur: ");
		latSur = leer.nextDouble();
		
		

		RunnableSource reader = new XmlDownloader(lonOeste, lonEste, latNorte, latSur,
				"http://www.openstreetmap.org/api/0.6");
		System.out.println("El Espacio de Estado se ha descargado.");

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		System.out.println("Se estan procesando los datos del archivo xml...");
		readerThread.start();
		System.out.println("Comenzamos");
		

		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				
			}
		}		
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
		tarea5(nodoO, listaNodoR);
		
	}

	public static void tarea5(long nodoO, LinkedList<Nodo> listaNodoR) throws Exception {
		System.out.println("Tipo de estrategia:\n"+
				"1.En Anchura.\n" +
				"2.En Profundida Acotada.\n"+
				"3.En Profundida Iterativa.\n"+
				"4.En Costo.\n"+ 				
				"5.En Voraz.\n" +
				"6.A*\n");
		int estrategia = leer.nextInt();
		System.out.println("Profundidad Iterativa.");
		int inc = leer.nextInt();
		System.out.println("Profundidad Maxima.");
		int prof = leer.nextInt();
		boolean hacerpoda=true;
		System.out.println("�Desea realizar poda?.\n"
				+ "1.Si\n"
				+ "2.No\n");
		int poda = leer.nextInt();
		String podaA="";
		switch (poda) {
		case 1:
			hacerpoda=true;
			podaA="Con";
			break;
		case 2:
			hacerpoda=false;
			podaA="Sin";			
			break;	
		}	
		EspacioEstados ee = new EspacioEstados(cGrafo, lonOeste, lonEste, latNorte, latSur);
		problema = new Problema(ee);
		Estado estadoinicial = new Estado(cGrafo.getNodo(nodoO), listaNodoR);
		tini = System.nanoTime();
		Stack<NodoBusqueda> solucion = problema.Busqueda(estadoinicial, problema,inc, prof, estrategia,hacerpoda);
		tiempo = System.nanoTime() - tini;			
		ImprimirSolucion iS = new ImprimirSolucion(solucion, estrategia,prof,podaA);
		iS.generargpx(tiempo,problema);		
	}

	public static void tarea4(long nodoO, LinkedList<Nodo> listaNodoR) throws Exception {
		System.out.println("Tipo de estrategia:\n" + "1.En Anchura.\n" + "2.En Profundidad.\n" + "3.En Costo.\n"
				+ "4.En Profundida Iterativa.\n");
		int estrategia = leer.nextInt();
		System.out.println("Profundidad Iterativa.\n");
		int inc = leer.nextInt();
		System.out.println("Profundidad Maxima.\n");
		int prof = leer.nextInt();
		boolean hacerpoda=true;
		System.out.println("�Desea realizar poda?.\n"
				+ "1.Si\n"
				+ "2.No\n");
		int poda = leer.nextInt();
		String podaA="";
		switch (poda) {
		case 1:
			hacerpoda=true;
			podaA="Con";
			break;
		case 2:
			hacerpoda=false;
			podaA="Sin";
			break;	
		}
		EspacioEstados ee = new EspacioEstados(cGrafo, lonOeste, lonEste, latNorte, latSur);
		problema = new Problema(ee);
		Estado estadoinicial = new Estado(cGrafo.getNodo(nodoO), listaNodoR);
		Stack<NodoBusqueda> solucion = problema.Busqueda(estadoinicial, problema,inc, prof, estrategia,hacerpoda);
		ImprimirSolucion iS = new ImprimirSolucion(solucion, estrategia, prof,podaA);
		iS.generargpx(tiempo,problema);

	}

	public static void tarea3(long nodoO, LinkedList<Nodo> listaNodoR) throws Exception {
		EspacioEstados ee = new EspacioEstados(cGrafo, lonOeste, lonEste, latNorte, latSur);

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