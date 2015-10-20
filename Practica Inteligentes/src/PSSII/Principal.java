package PSSII;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
		long tini, tiempo;
		//Tarea 1
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
		

		RunnableSource reader = new XmlDownloader(-4.0122,-3.8540, 39.0299, 38.9434,"http://www.openstreetmap.org/api/0.6");
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
		imprimirDatos();
		tini = System.nanoTime();
		algoritmo();
		tiempo = System.nanoTime() - tini;
		System.out.println("Tiempo: "+tiempo);
		
	}
	public static void algoritmo(){	
		Random rndm = new Random(); 
        rndm.setSeed(1000);  
		Frontera frontera=new Frontera();
		System.out.println("Introduce id del Nodo");
		long id=leer.nextLong();
		Estado estado = new Estado(id, cGrafo.getNodo(id).getLatitud(),cGrafo.getNodo(id).getLongitud());
		NodoBusqueda nodoPrimero = new NodoBusqueda(null,estado, 0, "", 0);		
		frontera.insertar(nodoPrimero);
		LinkedList<NodoAdyacente> listaAdyacentes;
		//LinkedList<NodoBusqueda> listaNodoBAdyacentes = new LinkedList<NodoBusqueda>();
		try{
		for(;;){			
			NodoBusqueda nodoSiguiente=frontera.getPrimerN();
			System.out.println(nodoSiguiente.getEstado().getId());
			listaAdyacentes= cGrafo.getNodo(nodoSiguiente.getEstado().getId()).getNodosAdyacentes();			 
			Collections.shuffle(listaAdyacentes, rndm);
			for(int i=0;i<listaAdyacentes.size();i++){
				long idS=listaAdyacentes.get(i).getIdA();
				Estado estado1=new Estado(idS,cGrafo.getNodo(idS).getLatitud(),cGrafo.getNodo(idS).getLongitud());
				frontera.insertar(new NodoBusqueda(nodoSiguiente,estado1,0,"",0));
			}			
		}
		}catch(Exception e){
			System.out.println("Error: "+e.getMessage());
		}
	}
	public static void imprimirDatos (){
		
		System.out.println("Mostrar los Nodos Adyacentes de un Nodo.");			
		System.out.println("Introduce id del Nodo");
		long id=leer.nextLong();
		System.out.println("Nodo:"+id+"(lat "+cGrafo.getNodo(id).getLatitud()+",lon "+cGrafo.getNodo(id).getLongitud()+")");
		for(int i=0;i<cGrafo.getNodo(id).getNodosAdyacentes().size();i++){
			System.out.println("[Nodo: "+id+" --> Nodo Adyacente: "+cGrafo.getNodo(id).getNodosAdyacentes().get(i).getIdA()+" Distancia:"+cGrafo.getNodo(id).calcularDistanciaNodoAdy(cGrafo.getNodo(cGrafo.getNodo(id).getNodosAdyacentes().get(i).getIdA()))+"]\n");
		}					
	}
}