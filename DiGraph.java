//In FractalGrapher.App
package FractalGrapher;

import java.util.ArrayList;

/*
 * The DiGraph class, a specialized Directed Graph data structure used to store
 * and mutate connections between nodes during the fractal generation process.
 * Maintains a final list attractors to be used by the program. An adjacency matrix
 * is used rather than jagged list due to small size of array.
 */
public class DiGraph {
	
	private AttractingPoint[] _attractors;
	private int[][] _adMatrix; //Adjacency Matrix, connections indicated by nonzero values
	
	/*
	 * Constructor method. Finalizes list of attractors by creating an array and
	 * creates an adjacency matrix for storing connections between nodes.
	 */
	public DiGraph(ArrayList<AttractingPoint> list){
		_attractors = new AttractingPoint[list.size()];
		for(int i = 0; i < list.size(); i++){
			_attractors[i] = list.get(i);
		}
		_adMatrix = new int[_attractors.length][_attractors.length];
		
	}
	
	/* 
	 * Mutator method. Sets directed connection between two attractors by changing
	 * matrix value to 1
	 */
	public void setConnection(int start, int end){
		_adMatrix[start][end] = 1;
	}
	
	/* Removes connection between two attractors by setting matrix value to 0 */
	public void removeConnection(int start, int end){
		_adMatrix[start][end] = 0;
	}
	
	/* Accessor method. Returns AttractingPoint wrapper class */
	public AttractingPoint getAttractor(int index){
		return _attractors[index];
	}
	
	/* 
	 * Accessor method. Returns list of indices for which specific attractors the 
	 * input attractor is connected to .
	 */
	public int[] getAllConnections(int index){
		ArrayList<Integer> returnVals = new ArrayList<Integer>();
		
		for(int i = 0; i < _attractors.length; i++){
			if(_adMatrix[index][i] != 0){
				returnVals.add(i);
			}
		}
		
		int[] returnArray = new int[returnVals.size()];
	    for (int i=0; i < returnArray.length; i++){
	        returnArray[i] = returnVals.get(i).intValue();
	    }
		
		return returnArray;
	}

	/* Accessor method. Returns number of attractors used in the graph */
	public int getNumAttractors(){
		return _attractors.length;
	}
	
	/* Accessor method. Returns the number of attractors input is connected to */
	public int getNumConnections(int attractor){
		int counter = 0;
		for(int i = 0; i < _attractors.length; i++){
			if(_adMatrix[attractor][i] != 0){
				counter ++;
			}
		}
		return counter;
	}
	
	/* Checks to see if a connected exists from start --> end direction*/
	public boolean isConnected(int start, int end){
		if (_adMatrix[start][end] == 0){
			return false;
		}
		else{
			return true;
		}
	}

}
