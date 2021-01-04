//In FractalGrapher.App
package FractalGrapher;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
 * The FractalBoard class is where the magic happens. This class deals with the logic
 * and graphics of fractal generation. The class relies heavily on an instance of the
 * DiGraph class, a specialized directed graph that stores connections between the 
 * attractors. This class logically implements the random walk process before placing
 * points to approximate the fractal directly on the plane. It relies heavily on user 
 * input, and it draws heavily from its sister class, OptionPane to morph variables.
 */
public class FractalBoard {

	private Pane _fractalPane;

	private double _contractionRate;
	
	
	private ArrayList<AttractingPoint> _testAttractingPoints;
	private DiGraph _attractors;
	
	private int _currentAttractor;
	private int _contractionType;
	private int _recursionLevel;
	
	private ArrayList<Shape> _attractorGraphics;
	private ArrayList<Shape> _gridLines;
	
	private Shape[][] _arrows;
	private Shape _attractorHighlight;
	private Shape _convexHull;
	
	/*
	 * Constructor method. Instantiates all necessary arrays to store variables and graphics.
	 * Sets up board, creates shapes to be used for later, and sets default values to variables
	 * that are later morphed by the user.
	 */
	public FractalBoard(Pane pane){
		_testAttractingPoints = new ArrayList<AttractingPoint>();
		_attractorGraphics = new ArrayList<Shape>();
		_gridLines = new ArrayList<Shape>();
		
		this.setUpBoard(pane);
		this.setUpAttractorHighlight(pane);
		_fractalPane = pane;
		
		_currentAttractor = -1;
		_contractionRate = 0;
		_contractionType = 0;
		_recursionLevel = 0;
		
	}
	
	/*
	 * The main method which creates the fractal. Based on the recursion level, a single point 
	 * randomly chooses a node to travel to (based on spinner method), travels a portion of distance
	 * towards attractor based on contraction rate, then repets the process. Placing thousands of such
	 * points starts to generated the fractal.
	 */
	public void randomWalk(int numOfPoints){
		int counter = 0;
		Color pointColor = new Color(0,0,0,0);
		
		while(counter < numOfPoints){
			double xPos = (Math.random()*Constants.BOARD_SIDE_LENGTH) + 25;
			double yPos = (Math.random()*Constants.BOARD_SIDE_LENGTH) + 25;
			
			//Where CONVEX HULL is used. Makes sure points are placed within hull to make smoother image
			if(_convexHull.contains(xPos, yPos)){
				double[] contractionList = this.contractionList();
				int prevAttractor = (int) (Math.random()*_attractors.getNumAttractors());
				
				double[] currentColor = new double[3];
				
				//Used for a smooth color gradient. Shifts RGB vals around like coordinates
				currentColor[0] = _attractors.getAttractor(prevAttractor).getColor().getRed();
				currentColor[1] = _attractors.getAttractor(prevAttractor).getColor().getGreen();
				currentColor[2] = _attractors.getAttractor(prevAttractor).getColor().getBlue();
				
				//Initital contraction
				xPos = xPos + (contractionList[0]*(_attractors.getAttractor(prevAttractor).getX() - xPos));
				yPos = yPos + (contractionList[0]*(_attractors.getAttractor(prevAttractor).getY() - yPos));
				
				for(int i = 1; i < contractionList.length; i++){
					//Spinner to decide which node to travel to next
					prevAttractor = this.spinner(prevAttractor);
					
					//Creating a color gradient
					currentColor[0] = currentColor[0] + 
							(contractionList[i]*(_attractors.getAttractor(prevAttractor).getColor().getRed() - currentColor[0]));
					currentColor[1] = currentColor[1] + 
							(contractionList[i]*(_attractors.getAttractor(prevAttractor).getColor().getGreen() - currentColor[1]));
					currentColor[2] = currentColor[2] + 
							(contractionList[i]*(_attractors.getAttractor(prevAttractor).getColor().getBlue() - currentColor[2]));
					
					//Moving point towards node
					xPos = xPos + (contractionList[i]*(_attractors.getAttractor(prevAttractor).getX() - xPos));
					yPos = yPos + (contractionList[i]*(_attractors.getAttractor(prevAttractor).getY() - yPos));
				}
				
				//Rescales color to make sure valid
				for(int i = 0; i < 3; i++){
					if(currentColor[i] > 1){
						currentColor[i] = 1;
					}
					if(currentColor[i] < 0){
						currentColor[i] = 0;
					}
				}
				
				pointColor = new Color(currentColor[0],currentColor[1],currentColor[2],1);
				
				_fractalPane.getChildren().add(new Circle(xPos,yPos, .5, pointColor));
				counter++;
			}
		}
	}
	
	/*
	 * One of the main methods of the class. Determines which attractor a point will move
	 * towards after each successful walk. After a point moves towards "current attractor",
	 * it can move to any one of the attractors connections (think about travelling on a directed
	 * graph). The next path chosed is determined by weighted probabilities of each attractor.
	 */
	public int spinner(int currentAttractor){ 
		
		//Sums up all relative probabilities and creates are partition based on each attractor
		int[] possibilities = _attractors.getAllConnections(currentAttractor);
		double[] partition = new double[possibilities.length];
		
		partition[0] = _attractors.getAttractor(possibilities[0]).getProbability();
		for(int i = 1; i < partition.length; i++){
			partition[i] = _attractors.getAttractor(possibilities[i]).getProbability()
					+ partition[i-1];
		}
		
		//RNG of point somewhere in partition
		double spin = Math.random() * partition[partition.length-1];	
		int counter = -1;
		
		//Counter determined by where in partion the random number is. Returns index as such
		for(int i = 0; i < partition.length; i++){
			if(spin < partition[i]){
				counter++;
			}
		}
		
		return possibilities[counter];
		
	}
	
	/* Accessor method. Returns cropping bounds used when taking a screenshot */
	public Rectangle getBounds(){
		return new Rectangle(25,25,600,600);
	}
	
	/* 
	 * Accessor method. Returns the current attractor (wrapper class) the program
	 * is focusing on.
	 */
	public int getCurrentAttractor(){
		return _currentAttractor;
	}

	/* Accessor method. Returns number attractors on the graph */
	public int getNumAttractors(){
		return _attractors.getNumAttractors();
	}
	
	/* Accessor method. Returns the number of connections an attractor is connected to */
	public int getNumConnections(int attractor){
		return _attractors.getNumConnections(attractor);
	}
	
	/* Mutator method. Sets how many time a placed point will iterated the contraction */
	public void setRecursionLevel(int i){
		_recursionLevel = i;
	}
	
	/* Mutator method. Sets type of contraction the random walk will use */
	public void setContractionType(int i){
		_contractionType = i;
	}
	
	/* Mutator method. Morphs contraction rate used during random walks */
	public void setContractionRate(double x){
		_contractionRate = x;
	}
	/* Mutator method. Sets the current attractor which program is focusing on */
	public void setCurrentAttractor(int i){
		_currentAttractor = i;
	}
	
	/* Mutator method. Morphs relative probability value of attractor */
	public void setProbability(double x, int attractor){
		_attractors.getAttractor(attractor).setProbability(x);
	}
	
	/*
	 * The main method for setting attractors. Logically adds attractors to a test
	 * list and grphically placed dots on the graph to signify attractor placement.
	 */
	public void setAttractingPoint(double x, double y, Color color){
		if (x >= Constants.BOARD_SHIFT_X && x <= (Constants.BOARD_SHIFT_X+600)
				&& y >= Constants.BOARD_SHIFT_Y && y <= (Constants.BOARD_SHIFT_Y+600)){
			Circle attractor = new Circle(x,y,6);
			attractor.setFill(color);
			
			_attractorGraphics.add(attractor);
			_testAttractingPoints.add(new AttractingPoint(x,y, attractor, color));
			_fractalPane.getChildren().add(attractor);
			_currentAttractor++;
		}
	}
	
	/*
	 * The main method for creating DIRECTED paths between two attractors (flowing in
	 * a single direction). Morphs the adjacency of the DiGraph to logically set connection
	 * and creates an arrow or path to show the user which connections have been set. 
	 */
	public void setPath(int startingAttractor, int endingAttractor){
		double x1 = _attractors.getAttractor(startingAttractor).getX();
		double y1 = _attractors.getAttractor(startingAttractor).getY();
		if(startingAttractor == endingAttractor){
			_attractors.setConnection(startingAttractor, endingAttractor);
			
			Arc arc = new Arc(x1,y1 - 20,20,20,315,270);
			arc.setFill(Color.TRANSPARENT);
			arc.setStroke(Color.BLACK);
			
			_arrows[startingAttractor][endingAttractor] = arc;
			
		}
		else{
			double x2 = _attractors.getAttractor(endingAttractor).getX();
			double y2 = _attractors.getAttractor(endingAttractor).getY();
			_attractors.setConnection(startingAttractor, endingAttractor);
			
			Line line = new Line(x1,y1,x2,y2);
			
			//The Arrowhead
			double mag = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
			double[] vec1 = {(14*(x1-x2))/mag,(14*(y1-y2))/mag};
			double[] vec2 = {(7*(y2-y1))/mag,(7*(x1-x2))/mag};
			Polygon arrowhead = new Polygon(x2,y2, x2+ vec1[0] + vec2[0], 
					y2 + vec1[1] + vec2[1], x2+ vec1[0] - vec2[0], y2+  vec1[1] - vec2[1]);			
			
			_arrows[startingAttractor][endingAttractor] = Shape.union(line, arrowhead);
			
		}
		_fractalPane.getChildren().add(_arrows[startingAttractor][endingAttractor]);
		_attractors.getAttractor(startingAttractor).getGraphics().toFront();
		_attractors.getAttractor(endingAttractor).getGraphics().toFront();
	}
	
	/*
	 * Moves the attractor highlight shape to the attractor specified by an index 
	 * given as the argument.
	 */
	public void setAttractorHighlight(int index){
		double xPos = _attractors.getAttractor(index).getX();
		double yPos = _attractors.getAttractor(index).getY();
		_attractorHighlight.setLayoutX(xPos - 325);
		_attractorHighlight.setLayoutY(yPos - 325);
		_attractorHighlight.setVisible(true);
	}
	
	/* Shows graph's grid lines by toggling visibilty to true */
	public void showGridLines(){
		for(int i = 0; i < _gridLines.size(); i++){
			_gridLines.get(i).setVisible(true);
		}
	}
	
	/* Shows attracting points by toggling visibilty to true */
	public void showAttractorGraphics(){
		for (int i = 0; i < _attractorGraphics.size(); i++){
			_attractorGraphics.get(i).setVisible(true);
		}
	}
	
	/* Shows convex hull shape by changing fill to a visible color */
	public void showConvexHull(){
		_convexHull.setFill(Color.DARKCYAN);
	}
	
	/*
	 * Graphically hides graphics of the graph's grid lines by setting the visibility to
	 * false.
	 */
	public void hideGridLines(){
		for(int i = 0; i < _gridLines.size(); i++){
			_gridLines.get(i).setVisible(false);
		}
	}
	
	/*
	 * Graphically hides graphics of each attracting point by setting the visibility to
	 * false.
	 */
	public void hideAttractorGraphics(){
		for (int i = 0; i < _attractorGraphics.size(); i++){
			_attractorGraphics.get(i).setVisible(false);
		}
	}

	/*
	 * Toggles visibility of convex hull by changing color to transparent (so as to preserve
	 * shape intersection properties which may potentially not exist if visibility property
	 * toggled to false)
	 */
	public void hideConvexHull(){
		_convexHull.setFill(Color.TRANSPARENT);
	}
	
	/*
	 * Removes a path both graphically and logically on the DiGraph, by replacing an arrow
	 * with a null object.
	 */
	public void removePath(int startingAttractor, int endingAttractor){
		_fractalPane.getChildren().remove(_arrows[startingAttractor][endingAttractor]);
		_arrows[startingAttractor][endingAttractor] = null;
		_attractors.removeConnection(startingAttractor, endingAttractor);
	}
	
	/*
	 * Removes that graphics of the path (the arrows), but the logic. Used when switching 
	 * attractors to save connections while updating graphics.
	 */
	public void removePathGraphics(){
		for (Shape[] shapeList : _arrows){
			for(Shape arrow : shapeList){
				_fractalPane.getChildren().remove(arrow);
			}
		}
	}
	
	/*
	 * Removes attractor highligh by toggling visibilty to false. Used for graphics, not logic.
	 */
	public void removeAttractorHighlight(){
		_attractorHighlight.setVisible(false);
	}
	
	/*
	 * Called by OptionsPane when moving to the second page. Used to finalize list of attractors
	 * to avoid potential problems later on in the program of removing/adding points. Also calculates
	 * convex hull later, which is used later in program.
	 */
	public void toRandomWalkDefs(){
		_attractors = new DiGraph(_testAttractingPoints);
		_arrows = new Shape[_attractors.getNumAttractors()][_attractors.getNumAttractors()];
		_currentAttractor = 0;
		_convexHull = this.calculateConvexHull();
		_fractalPane.getChildren().add(_convexHull);

	}
	
	/*
	 * Called by the FractalGenerator class during the "set path" phase of generation. Based
	 * on coordinates of mouse click (it close enough), the method adds/removes a path to the
	 * respective attractor depending on the state it is already in.
	 */
	public void drawPathMouse(double xClick, double yClick){
		
		int closestPoint = 0;
		double closestRad = 1000;
		double xPos = 0;
		double yPos = 0;
		
		//determining the point with closest distance to click
		for(int i = 0; i < _attractors.getNumAttractors(); i++){
			xPos = _attractors.getAttractor(i).getX();
			yPos = _attractors.getAttractor(i).getY();
			
			if(((Math.pow((xPos- xClick), 2) + Math.pow((yPos- yClick), 2)) <= closestRad)){
				closestRad = ((Math.pow((xPos- xClick), 2) + Math.pow((yPos- yClick), 2)));
				closestPoint = i;
			}	
		}
		
		//Adding or removing the path
		if (closestRad <= Constants.CLICK_RADIUS){
			if (_attractors.isConnected(_currentAttractor, closestPoint)){
				this.removePath(_currentAttractor, closestPoint);
			}
			else{
				this.setPath(_currentAttractor, closestPoint);
			}
		}
	}
	
	/*
	 * Removes last attractor added to the _testAttractingPoints list, as well as the 
	 * attractor graphics from the board.
	 */
	public void undoLastAttractor(){
		if(_testAttractingPoints.size() > 0){
			_fractalPane.getChildren().remove(_testAttractingPoints.get(_testAttractingPoints.size()-1).getGraphics());
			_testAttractingPoints.remove(_testAttractingPoints.size() - 1);
			_currentAttractor --;
		}
	}
	
	/*
	 * Returns a polygon which can be viewed as the convex hull of the set of attractors. 
	 * This method is incredibly important for point placement; while points can be placed 
	 * anywhere on screen when using constant contraction rates, point placement within the 
	 * hull is salient in creating smoother and more distinguishable fractals. This method 
	 * uses an algorithm called Jarvis' March (or Gift Wrapping algorithm) to create the convex
	 * hull.
	 */
	private Shape calculateConvexHull(){
		//Jarvis' March algorithm, time complexity max of O(n^2)
		Shape hull = new Polygon();
		double[][] coords = new double[_attractors.getNumAttractors()][2];
		
		ArrayList<Double> hullCoordsX = new ArrayList<Double>();
		ArrayList<Double> hullCoordsY = new ArrayList<Double>();
		
		//Algorithm creates a degenerate object for 1 or 2 nodes, so throws cases out
		if(_attractors.getNumAttractors() == 1 || _attractors.getNumAttractors() == 2){
			hull = new Rectangle(Constants.BOARD_SHIFT_X, Constants.BOARD_SHIFT_Y,
				Constants.BOARD_SIDE_LENGTH,Constants.BOARD_SIDE_LENGTH);
		}
		else{
			//Makes a coordinate list
			for(int i = 0; i < _attractors.getNumAttractors(); i++){
				coords[i][0] = _attractors.getAttractor(i).getX();
				coords[i][1] = _attractors.getAttractor(i).getY();
			}
			
			//Finds attractor with smallest X coordinate
			double minX = 10000;
			int minXIndex = -1;
			
			for(int i = 0; i < coords.length; i++){
				if (coords[i][0] < minX){
					minX = coords[i][0];
					minXIndex = i;
				}
			}
			
			double[] testVec1 = new double[2];
			double[] testVec2 = new double[2];
			double[] angles = new double[coords.length];
			
			hullCoordsX.add(coords[minXIndex][0]);
			hullCoordsY.add(coords[minXIndex][1]);
			
			//Getting the second point in the hull
			for(int i = 0; i < coords.length; i++){
				testVec1[0] = 0;
				testVec1[1] = -hullCoordsY.get(0);
				
				testVec2[0] = coords[i][0] - hullCoordsX.get(0);
				testVec2[1] = coords[i][1] - hullCoordsY.get(0);
				
				angles[i] = this.getAngle(testVec1,testVec2);
			}
			
			hullCoordsX.add(coords[this.getMinIndex(angles)][0]);
			hullCoordsY.add(coords[this.getMinIndex(angles)][1]);
			
			//Getting the other points in hull, by "wrapping" around the most exterior points
			while((hullCoordsX.get(0) - hullCoordsX.get(hullCoordsX.size()-1) != 0 || 
					hullCoordsY.get(0) - hullCoordsY.get(hullCoordsY.size()-1) != 0)){
				for(int i = 0; i < coords.length; i++){
					testVec1[0] =  hullCoordsX.get(hullCoordsX.size() - 1) -  
							hullCoordsX.get(hullCoordsX.size() - 2);
					testVec1[1] =  hullCoordsY.get(hullCoordsY.size() - 1) -  
							hullCoordsY.get(hullCoordsY.size() - 2);
					
					testVec2[0] = coords[i][0] - hullCoordsX.get(hullCoordsX.size() - 1);
					testVec2[1] = coords[i][1] - hullCoordsY.get(hullCoordsY.size() - 1);
					
					angles[i] = this.getAngle(testVec1,testVec2);
				}
				
				hullCoordsX.add(coords[this.getMinIndex(angles)][0]);
				hullCoordsY.add(coords[this.getMinIndex(angles)][1]);
			}
			
			//Creating the hull 
			double[] hullCoordsFinal = new double[2* hullCoordsX.size()];
			for(int i = 0; i < hullCoordsX.size(); i++){
				hullCoordsFinal[2*i] = hullCoordsX.get(i);
				hullCoordsFinal[(2*i) + 1] = hullCoordsY.get(i);
			}
			
			hull = new Polygon(hullCoordsFinal);
		}
		
		hull.setFill(Color.TRANSPARENT);
		hull.setOpacity(.5);
		return hull;
	}
	
	/*
	 * One of the most important methods in this class. Based on a number given by the user,
	 * method creates and returns a list of contraction rates which determine how quickly/slowly
	 * a point moves towards attractors during the random walk.
	 */
	private double[] contractionList(){
		
		//Length of recursion list determined by user
		double[] contractions = new double[_recursionLevel];
		
		switch(_contractionType){
			case 1: //Constant
				for(int i = 0; i < _recursionLevel; i++){
					contractions[i] = _contractionRate;
				}
				break;
				
			case 2: //Exponential
				for(int i = 0; i < _recursionLevel; i++){
					contractions[i] = (double) Math.pow(_contractionRate, i);
				}
				break;
			
			case 3: //Harmonic
				for(int i = 0; i < _recursionLevel; i++){
					contractions[i] = (double) _contractionRate/(i+1);
				}
				break;
				
			case 4: //Fibonacci
				double fn1 = 1;
				double fn2 = 1;
				double holder = 0;
				
				for(int i = 0; i < _recursionLevel; i++){
					contractions[i] = _contractionRate/fn1;
					holder = fn1;
					fn1 = fn2;
					fn2 += holder;
				}
				break;
		}
		return contractions;
	}
	
	/*
	 * Creates a highlight shape that is placed around the current attractor during the second
	 * page. Visibility is toggled to false so that it is not viewable until called by the program
	 * at a later point in time.
	 */
	private void setUpAttractorHighlight(Pane pane){
		Ellipse holder1 = new Ellipse(325, 345, 3,10);
		Ellipse holder2 = new Ellipse(325, 305, 3,10);
		_attractorHighlight = holder1;
		_attractorHighlight = Shape.union(_attractorHighlight, holder2);
		
		for(int i = 0; i < 3; i++){
			_attractorHighlight.setRotate(45);
			_attractorHighlight = Shape.union(_attractorHighlight, holder1);
			_attractorHighlight = Shape.union(_attractorHighlight, holder2);
		}
		
		_attractorHighlight.setFill(Color.GOLD);
		_attractorHighlight.setOpacity(.5);
		pane.getChildren().add(_attractorHighlight);
		_attractorHighlight.setVisible(false);
	}
	
	/*
	 * Sets up the initial graphics of the board, including graph with gridlines. Saves
	 * gridlines in a list so they can be removed/added back as needed in the final stages
	 * of the fractal generation.
	 */
	private void setUpBoard(Pane pane){
		Rectangle border1 = new Rectangle(Constants.BOARD_SHIFT_X, Constants.BOARD_SHIFT_Y,
				Constants.BOARD_SIDE_LENGTH,Constants.BOARD_SIDE_LENGTH);
		border1.setFill(Color.BLACK);
		Rectangle border2 = new Rectangle(Constants.BOARD_SHIFT_X+2,Constants.BOARD_SHIFT_Y+2,
				Constants.BOARD_SIDE_LENGTH-4,Constants.BOARD_SIDE_LENGTH-4);
		border2.setFill(Color.WHITE);
		pane.getChildren().addAll(border1,border2);
		
		for (int i = 1; i < 10; i++){
			Line gridLine = new Line(25+(60*i),25,25+(60*i),625);
			gridLine.setFill(Color.AZURE);
			gridLine.setStrokeWidth(.1);
			pane.getChildren().add(gridLine);
			_gridLines.add(gridLine);
			
		}
		
		for (int i = 1; i < 10; i++){
			Line gridLine = new Line(25,25+(60*i),625,25+(60*i));
			gridLine.setFill(Color.AZURE);
			gridLine.setStrokeWidth(.1);
			pane.getChildren().add(gridLine);
			_gridLines.add(gridLine);
		}
	}

	/* Returns the angle between two 2D vectors through dot product formula, angle in radians */
	private Double getAngle(double[] vec1, double[] vec2){
		double magVec1 = Math.sqrt(Math.pow(vec1[0], 2) + Math.pow(vec1[1], 2));
		double magVec2 = Math.sqrt(Math.pow(vec2[0], 2) + Math.pow(vec2[1], 2));
		double dotProd = (vec1[0] * vec2[0]) + (vec1[1] * vec2[1]);
		return Math.acos(dotProd / (magVec1 * magVec2)); //in radians
	}

	/* Accessor method. Returns the index of the smallest number in an array */
	private int getMinIndex(double[] list){
		int returnVal = -1;
		double minVal = 1000000;
		
		for (int i = 0; i < list.length; i++){
			if(list[i] < minVal){
				returnVal = i;
				minVal = list[i];
			}
		}
		return returnVal;
	}
}
