//In FractalGrapher.App
package FractalGrapher;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
 * The AttractingPoint class, a wrapper class that stores individual properties of each
 * attractor placed by the user on the first page. Probability, graphics, color, and 
 * coordinates are stored within the class.
 */
public class AttractingPoint {
	
	private double[] _coordinates;
	private double _probability;
	private Circle _attractorGraphics;
	private Color _color;
	
	/* Constructor method. Instantiates all variables according data passed it. */
	public AttractingPoint(double xPos, double yPos, Circle circle, Color color ){
		_coordinates = new double[2];
		_coordinates[0] = xPos;
		_coordinates[1] = yPos;
		_probability = 1; //Probability default is 1
		_attractorGraphics = circle;
		_color = color;
	}
	
	/* Mutator method. Sets color of attractor */
	public void setColor(Color color){
		_color = color;
	}
	/* Mutator method. Sets probability of attractor */
	public void setProbability(double x){
		_probability = x;
	}
	/* Accessor method. Returns color of attractor */
	public Color getColor(){
		return _color;
	}
	
	/* Accessor method. Returns x coordinate of attractor */
	public double getX(){
		return _coordinates[0];
	}
	/* Accessor method. Returns y coordinate of attractor */
	public double getY(){
		return _coordinates[1];
	}

	/* Accessor method. Returns graphical component of attractor */
	public Circle getGraphics(){
		return _attractorGraphics;
	}
	
	/* Accessor method. Returns probability of attractor */
	public double getProbability(){
		return _probability;
	}
	

}
