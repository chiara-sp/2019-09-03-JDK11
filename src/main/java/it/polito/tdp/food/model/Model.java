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
}
