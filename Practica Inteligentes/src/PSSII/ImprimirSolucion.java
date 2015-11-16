package PSSII;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Stack;

public class ImprimirSolucion {
	Stack<NodoBusqueda> solucion;
	private String estrategia;
	private int max_prof;

	public ImprimirSolucion(Stack<NodoBusqueda> solucion,int srtEstrategia,int max_prof) {		
		this.solucion = solucion;
		switch(srtEstrategia){
			case 1:
				this.estrategia="Anchura";
				break;
			case 2:
				this.estrategia="Profundidad";
				break;
			case 3:
				this.estrategia="Costo";
				break;
			case 4:
				this.estrategia="Profundidad Iterativa";
				break;
			case 5:
				this.estrategia="Voraz";
				break;
			case 6:
				this.estrategia="A*";
				break;		
		}	
		this.max_prof=max_prof;
	}

	public Stack<NodoBusqueda> getSolucion() {
		return solucion;
	}

	public void setSolucion(Stack<NodoBusqueda> solucion) {
		this.solucion = solucion;
	}public void generartxt(long tiempo) throws IOException {

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
			salida.writeBytes("\n\nEstado --> (" + nodo.getEstado().getIdO().getId());
			for(int i=0;i<nodo.getEstado().getIdD().size();i++){
				if(i==0){
					salida.writeBytes("[");
				}
				if(i<nodo.getEstado().getIdD().size()-1){
					salida.writeBytes(nodo.getEstado().getIdD().get(i).getId()+",");
				}else{
					salida.writeBytes(nodo.getEstado().getIdD().get(i).getId()+"]");
				}				
			}
			salida.writeBytes(")");
		}

		id_Destino = nodo.getEstado().getIdO().getId();
		costo = nodo.getCosto();

		salida.writeBytes("\n\nDatos de la solucion.");
		salida.writeBytes("\nNodo Origen:  " + id_Origen + ".");
		salida.writeBytes("\nNodo Destino: " + id_Destino + ".");
		salida.writeBytes("\nEstrategia:   " + estrategia + ".");
		salida.writeBytes("\nProfundidad Maxima:   " + max_prof + ".");
		salida.writeBytes("\nCoste de la Solucion: " + costo + " metros.");
		salida.writeBytes("\nComplejidad Espacial: " + num_nodos + " nodos.");
		salida.writeBytes("\nComplejidad Temporal: " + tiempo + " nanosegundos</ele>");
		salida.close();
		System.out.println("Se ha generado correctamente el fichero .txt");
	}
	
	public void generargpx(long tiempo)
			throws IOException {		
		double costo;
		int num_nodos;

		NodoBusqueda nodo;
		RandomAccessFile salida;
		salida = new RandomAccessFile("solucion.gpx", "rw");
		for (int i = 0; i < salida.length(); i++)
			salida.writeBytes("");
		salida.close();
		salida = new RandomAccessFile("solucion.gpx", "rw");

		num_nodos = solucion.size();
		nodo = solucion.peek();
		

		salida.writeBytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		salida.writeBytes("\n<gpx version=\"1.0\">");
		salida.writeBytes("\n\t<name>Ruta gpx</name>");
		salida.writeBytes("\n\t<wpt lat=\"" + nodo.getEstado().getIdO().getLatitud() + "\" lon=\""
				+ nodo.getEstado().getIdO().getLongitud() + "\">");
		salida.writeBytes("\n\t\t<ele>0</ele>");
		salida.writeBytes("\n\t\t<name>Origen</name>\n\t</wpt>");
		salida.writeBytes("\n\t<trk><name>Ruta gpx</name><number>1</number>");
		while (!solucion.isEmpty()) {
			nodo = solucion.pop();			
			salida.writeBytes("\n\t\t<trkpt lat=\"" + nodo.getEstado().getIdO().getLatitud() + "\" lon = \""
					+ nodo.getEstado().getIdO().getLongitud() + "\">");
			salida.writeBytes("\n\t\t\t<ele>0</ele>");
			salida.writeBytes("\n\t\t</trkpt>");
		}
		
		costo = nodo.getCosto();

		salida.writeBytes("\n\t\t<desc><b>Datos de la solucion</b>");
		salida.writeBytes("\n\t\t\t<ele>Nodo Origen: " + nodo.getEstado().getIdO().getId() + "</ele>");
		for(int i=0;i<nodo.getEstado().getIdD().size();i++){
			salida.writeBytes("\n\t\t\t<ele>Nodo Objetivo: " + nodo.getEstado().getIdD().get(i).getId()+ "</ele>");
		}		
		salida.writeBytes("\n\t\t\t<ele>Estrategia: " + estrategia + "</ele>");
		salida.writeBytes("\n\t\t\t<ele>Profundidad Maxima: " + max_prof + "</ele>");
		salida.writeBytes("\n\t\t\t<ele>Coste de la Solucion: " + costo + " metros</ele>");
		salida.writeBytes("\n\t\t\t<ele>Complejidad Espacial: " + num_nodos + " nodos</ele>");
		salida.writeBytes("\n\t\t\t<ele>Complejidad Temporal: " + tiempo + " nanosegundos</ele>");
		salida.writeBytes("\n\t\t</desc>");

		salida.writeBytes("\n\t</trk>");
		salida.writeBytes("\n\t<wpt lat=\"" + nodo.getEstado().getIdO().getLatitud() + "\" lon=\""
				+ nodo.getEstado().getIdO().getLongitud() + "\">");
		salida.writeBytes("\n\t\t<ele>0</ele>");
		salida.writeBytes("\n\t\t<name>Destino</name>\n\t</wpt>");
		salida.writeBytes("\n</gpx>");
		salida.close();
		System.out.println("El fichero .gpx se ha imprimido correctamente ");
	}

}

