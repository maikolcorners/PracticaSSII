package PSSII;
import java.util.Iterator;
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
	static Scanner leer =new Scanner(System.in);
	static public void main(String[] args) throws Exception {		
		Sink sinkImplementation = new Sink() {
			public void initialize(Map<String, Object> metaData) {
			};
			public void process(EntityContainer entityContainer) {
				Nodo nodo;
				Entity entity = entityContainer.getEntity();				
				if (entity instanceof Node) {					
					nodo = new Nodo((Node) entity);
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
						addEnlace((Way) entity, info); //Tarea 2						
					}
				}
			}//Tarea 1 Fin
			//Tarea 2
			private void addEnlace(Way entity, String informacion) {
				List<WayNode> ListaWayNodes = entity.getWayNodes();
				NodoAdyacente nAdyacente;
				WayNode waynode1, waynode2;	
				double distancia;
				waynode1 = ListaWayNodes.get(0);	
				Nodo nodoP, nodoA;
				for (int i = 1; i < ListaWayNodes.size(); i++) {
					waynode2 = ListaWayNodes.get(i);
					nodoP=cGrafo.getNodo(waynode1.getNodeId());
					nodoA=cGrafo.getNodo(waynode2.getNodeId());
					distancia=nodoP.calcularDistanciaNodoAdy(nodoA);
					nAdyacente = new NodoAdyacente(nodoA.getId(),distancia, informacion);
					cGrafo.getNodo(nodoP.getId()).getNodosAdyacentes().add(nAdyacente);
					nAdyacente = new NodoAdyacente(nodoP.getId(),distancia, informacion);
					cGrafo.getNodo(nodoA.getId()).getNodosAdyacentes().add(nAdyacente);					
					waynode1 = waynode2;
				}
			}
			//Tarea 2 Fin

			public void release() {
			}

			public void complete() {
			}

		};
		//Tarea 1
		System.out.println("Descargando mapa del sitio web...");
		

		RunnableSource reader = new XmlDownloader(-3.93201,-3.92111, 38.98396, 38.98875,"http://www.openstreetmap.org/api/0.6");
		System.out.println("El mapa ha sido descargado.");

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		System.out.println("Se estan procesando los datos del archivo xml...");
		readerThread.start();
		System.out.println("Comenzamos");
		//Fin Tarea 1
		
		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				/* No hacer nada */
			}
		}		
		
		System.out.println("Selecciona las siguientes opciones:\n"
				+ "1.Imprimir datos del Nodo.\n"
				+ "2.Imprimir ruta entre dos nodos.\n");
		int opc=leer.nextInt();
		switch(opc){
		case 1:
			imprimirDatos();
			break;		
		case 2:
			solucion();
			break;
		}	
	}
	public static void solucion() throws Exception{
		System.out.print("Id Nodo origen: \n");
		int nodoO=765309500;
		System.out.print("Id Nodo destino: \n");
		int nodoD=504656555;
		System.out.println("Tipo de estrategia:\n"
				+ "1.En Anchura.\n"
				+ "2.En Profundidad.\n"
				+ "3.En Costo.\n");
		int estrategia=leer.nextInt();
		System.out.println("Profundidad maxima.\n");
		int prof=leer.nextInt();
		Stack <NodoBusqueda>solucion = null;
		problema=new Problema (cGrafo);
		solucion=problema.Busqueda_Acotada(nodoO, nodoD,problema, prof, estrategia);			
	}	
	public static void imprimirDatos () throws Exception{		
		System.out.println("Mostrar los Nodos Adyacentes de un Nodo.");			
		System.out.println("Introduce id del Nodo");
		long id=leer.nextLong();
		System.out.println("Nodo:"+id+"(lat "+cGrafo.getNodo(id).getLatitud()+",lon "+cGrafo.getNodo(id).getLongitud()+")");
		for(int i=0;i<cGrafo.getNodo(id).getNodosAdyacentes().size();i++){			
			System.out.println("[Nodo: "+id+" --> Nodo Adyacente: "+cGrafo.getNodo(id).getNodosAdyacentes().get(i).getIdA()+"]");
		}					
	}
}