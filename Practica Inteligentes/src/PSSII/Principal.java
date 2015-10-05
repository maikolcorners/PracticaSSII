package PSSII;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	static public void main(String[] args) {

		Sink sinkImplementation = new Sink() {
			public void initialize(Map<String, Object> metaData) {
			};

			public void process(EntityContainer entityContainer) {
				Nodo nodo;

				Entity entity = entityContainer.getEntity();
				if (entity instanceof Node) {
					System.out.println("Node " + entity.getId());
					nodo = new Nodo(entity.getId(), ((Node) entity).getLongitude(), ((Node) entity).getLatitude());
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
			}

			private void addEnlace(Way entity, boolean dobleSentido, String informacion) {
				List<WayNode> ListaWayNodes = entity.getWayNodes();
				WayNode waynode1, waynode2;
				double distancia;
				double latitud1, longitud1, latitud2, longitud2;
				NodoAdyacente nAdyacente;
				waynode1 = ListaWayNodes.get(0);
				for (int i = 0; i < ListaWayNodes.size(); i++) {
					waynode2 = ListaWayNodes.get(i);
					latitud1 = cGrafo.getNodo(waynode1.getNodeId()).getLatitud();
					longitud1 = cGrafo.getNodo(waynode1.getNodeId()).getLongitud();
					latitud2 = cGrafo.getNodo(waynode2.getNodeId()).getLatitud();
					longitud2 = cGrafo.getNodo(waynode2.getNodeId()).getLongitud();
					distancia = cGrafo.calcularDistancia(latitud1, longitud1, latitud2, longitud2);
					nAdyacente = new NodoAdyacente(waynode2.getNodeId(), distancia, informacion);
					cGrafo.getNodo(waynode1.getNodeId()).getNodosAdyacentes().add(nAdyacente);
					if (dobleSentido) {
						nAdyacente = new NodoAdyacente(waynode1.getNodeId(), distancia, informacion);
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
	}
}