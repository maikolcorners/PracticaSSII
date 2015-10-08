package PSSII;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
	static Scanner leer =new Scanner(System.in);
	static public void main(String[] args) {

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
					String highway = "", oneway = "", info = "";
					while (it.hasNext()) {
						t = it.next();
						if (t.getKey().equals("name"))
							info = t.getValue();
						if (t.getKey().equals("highway"))
							highway = t.getValue();
						if (t.getKey().equals("oneway"))
							oneway = t.getValue();
					}
					if (highway.equals("pedestrian") || highway.equals("trunk") || highway.equals("residential")) {
						if (oneway.equals("yes")) {
							addEnlace((Way) entity, false, info);
						} else if (oneway.equals("no")) {
							addEnlace((Way) entity, true, info);
						}
					}
				}
			}//Tarea 1

			private void addEnlace(Way entity, boolean dobleSentido, String informacion) {
				List<WayNode> ListaWayNodes = entity.getWayNodes();
				Enlaces nAdyacente;
				WayNode waynode1, waynode2;								
				waynode1 = ListaWayNodes.get(0);
				Nodo nodo1, nodo2;
				for (int i = 1; i < ListaWayNodes.size(); i++) {
					waynode2 = ListaWayNodes.get(i);
					nodo1=cGrafo.getNodo(waynode1.getNodeId());
					nodo2=cGrafo.getNodo(waynode2.getNodeId());					
					nAdyacente = new Enlaces(nodo1,nodo2, informacion);
					cGrafo.getNodo(waynode1.getNodeId()).getNodosAdyacentes().add(nAdyacente);
					if (dobleSentido) {
						nAdyacente = new Enlaces(nodo1,nodo2, informacion);
						cGrafo.getNodo(waynode1.getNodeId()).getNodosAdyacentes().add(nAdyacente);
					}
					waynode1 = waynode2;

				}

			}

			public void release() {
			}

			public void complete() {
			}

		};		
		System.out.println("Descargando mapa del sitio web...");
		RunnableSource reader = new XmlDownloader(-3.9426, -3.9101, 38.9978, 38.9685,
				"http://www.openstreetmap.org/api/0.6");
		System.out.println("El mapa ha sido descargado.");

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		System.out.println("Se estan procesando los datos del archivo xml...");
		readerThread.start();
		System.out.println("Comenzamos");
		
		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				/* No hacer nada */
			}
		}		
		imprimirDatos();
		
	}
	public static void imprimirDatos (){
		System.out.println(".Mostrar los Nodos Adyacentes de un Nodo."
				+ "\n");			
		System.out.println("Introduce id del Nodo\n");
		int id=leer.nextInt();
		for(int i=0;i<cGrafo.getNodo(id).getNodosAdyacentes().size();i++)
			System.out.println(cGrafo.getNodo(id).getNodosAdyacentes().get(i).getId());		
	}
}