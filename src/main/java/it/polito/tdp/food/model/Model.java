package it.polito.tdp.food.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	
	FoodDao dao;
	SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	List<String> vertici;
	List<String> soluzione;
	
	String partenza;
	double pesoTot;

	
	public Model() {
		dao= new FoodDao();
		vertici= new LinkedList<>();
		
	}
	
	public void creaGrafo(int calories) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		vertici=dao.getVertici(calories);
		Graphs.addAllVertices(grafo, vertici);
		
		for(Adiacenza a: dao.getAdiacenze()) {
			if(grafo.vertexSet().contains(a.getP1())&& grafo.vertexSet().contains(a.getP2())) {
				Graphs.addEdge(grafo,a.getP1(),a.getP2(), a.getPeso());
			}
		}
	}
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<String> getVertici(){
		return vertici;
	}
	public boolean grafoCreato() {
		if(grafo==null) {
			return false;
		}
		return true;
	}
	public List<Vicino> getVicini(String portion){
		List<Vicino> vicini= new LinkedList<>();
		for(String s: Graphs.neighborListOf(grafo, portion)) {
			vicini.add(new Vicino(s, (int)grafo.getEdgeWeight(grafo.getEdge(s, portion))));
		}
		return vicini;
	}
	public List<String> doRicorsione(String partenza, int N) {
		this.partenza=partenza;
		soluzione= new LinkedList<>();
		LinkedList<String> parziale= new LinkedList<>();
		parziale.add(partenza);
		pesoTot=0;
		
		cerca(0, N, parziale, 0);
		
		return soluzione;
	}

	private void cerca(int livello, int n, LinkedList<String> parziale, int pesoP) {
		
		//condizione di terminazione
		if(livello>n) {
			return;
		}
		if(livello==n ) {
			if(pesoP>pesoTot || pesoTot==0) {
				soluzione= new LinkedList<>(parziale);
				pesoTot=pesoP;
			}
			return;
		}
		String ultimo= parziale.get(parziale.size()-1);
		for(String s: Graphs.neighborListOf(grafo, ultimo)) {
			
			if(!parziale.contains(s)) {
				pesoP+= (int)grafo.getEdgeWeight(grafo.getEdge(s, ultimo));
				parziale.add(s);
				cerca(livello+1,n, parziale,pesoP);
				parziale.remove(s);
				pesoP-=(int)grafo.getEdgeWeight(grafo.getEdge(s, ultimo));
			}
		}
		
	}
	public double calcolaPeso() {
	return this.pesoTot;
	}
}
