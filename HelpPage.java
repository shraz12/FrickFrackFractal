//In FractalGrapher.App
package FractalGrapher;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/*
 * The HelpPage class. When created, opens up a new screen and makes a copy of UserGuide
 * (with better formatting), so as to allow the user easier access to the how-to guide.
 */
public class HelpPage {

	private BorderPane _helpRoot;
	private Pane _textPane;
	private Text[] _text;
	
	/* Constructor method. Instantiates a pane and other variables and adds text to pane */
	public HelpPage(){
		_helpRoot = new BorderPane();
		_textPane = new Pane();
		_text = new Text[15];
		this.setText();
	}
	
	/* Accessor method. Returns root pane when called. */
	public BorderPane getRoot(){
		return _helpRoot;
	}
	
	/*
	 * Creates text objects to store the entire UserGuide and adds it to _textPane, which is
	 * in turn added to a ScrollPane to allow for ease of scrolling. Each part of the user guide
	 * is stored as a different object in the text list.
	 */
	public void setText(){
		ScrollPane helpText = new ScrollPane();
		
		_text[0] = new Text("User Guide");
		_text[0].setFill(Color.MEDIUMPURPLE);
		_text[0].setScaleX(2.5);
		_text[0].setScaleY(2.5);
		this.placeObject(_text[0], 60, 20);
		
		_text[1] = new Text("Table Of Contents:");
		this.placeHeader(_text[1], 80);
		
		_text[2] = new Text("	I. Overview"
				+ "\n	II. Page 1: Setting Attracting Points"
				+ "\n	III. Page 2: Setting Paths"
				+ "\n	IV. Page 3: Global Properties"
				+ "\n	V. Page 4: Generating the Fractal"
				+ "\n	VI. Examples");
		this.placeObject(_text[2], 0, 110);
		
		_text[3] = new Text("I. Overview");
		this.placeHeader(_text[3], 250);
		
		
		_text[4] = new Text("Welcome to FrickFrackFractal! This Java app generates fractals through a method called Chaos Games, "
				+ "which is a more specific case of an overarching form of generation called Iterated Function Systems (IFS). "
				+ "This method of fractal generation relies on points being shifted around by a set of contraction maps "
				+ "(functions that shrink distances between points over multiple iterations). Points are placed on a map and "
				+ "move towards attracting points (which I call attractors) based on a mix of randomness and parameters set by "
				+ "the user. Placing thousands of points eventually creates (or rather, approximates) a fractal. A perfect "
				+ "fractal would be created by the placement of infinite points which are allowed shift around infinitely, but "
				+ "we are able to substitute this with random point placement and a truncated amount of movement. Out of chaos "
				+ "comes order. Note that not all sets of parameters are guaranteed to create fractals, some will create clouds, "
				+ "white noise, or a degenerate set of points.");
		this.placeObject(_text[4], 0, 280);
		_text[4].setWrappingWidth(Constants.STAGE_WIDTH-60);
		
		_text[5] = new Text("II. Page 1: Setting Attracting Points");
		this.placeHeader(_text[5], 460);
		
		_text[6] = new Text("Page 1 is where the user places attractors for fractal generation. The user places attractors in one of 2 ways: "
				+ "by clicking directly on the board with a mouse or inputting exact XY coordinates in the options and clicking "
				+ "“Set Attracting Point”. Please normalize coordinates to [0,1], as the calculator only accepts XY such values and "
				+ "will reject non numerical or out of bounds coordinates. At least one point must be placed, and point placement can"
				+ "be undone with the undo button. \n \n"
				+ "Page 1 also contains a color picker (under “Choose Attractor Color”). If the user desires to create a multicolored "
				+ "fractal, they set each attractor to different colors. The computer does the work of creating a smooth gradient colored "
				+ "fractal based on user choices. The default is black and creates a black and white color fractal. \n \n"
				+ "Note: It is HIGHLY recommended that the user places at least 3 attractors on the graph. Placing 1 or 2 attractors "
				+ "will create degenerate fractals on account of the fact that the most “depth” that can be created by connecting 1 "
				+ "or 2 points is a simple line (as opposed to a polygon with 3+ points).");
		this.placeObject(_text[6], 0, 490);
		_text[6].setWrappingWidth(Constants.STAGE_WIDTH-60);
		
		_text[7] = new Text("III. Page 2: Setting Paths");
		this.placeHeader(_text[7], 750);
		
		_text[8] = new Text("The fractals generated are what I call “first order fractals”, because each movement of each point placed for the "
				+ "random walk is based directly on the previous attractor it moved towards (as opposed to being based on the previous "
				+ "2,3,... attractors it moved towards). Currently, there is no support for higher order fractals. \n \n"
				+ "The current node for which connections are being set is highlighted in yellow. The user clicks on the other nodes to "
				+ "connect the highlighted node to them (reclicking deletes the connection). There are some buttons to delete and auto-add "
				+ "connections for ease. To prevent the issue of points being locked out of movement, at least one connection must be set "
				+ "per attractor. \n \n"
				+ "There is also a box for changing relative probability (titled “Set Relative Probability”) with a default of 1. This is "
				+ "a rather advanced setting and deals with how frequently a point travels towards an attractor when traveling to that "
				+ "attractor is a possibility. The end result is a fractal with inconsistent density of points in space. Setting probability "
				+ "to 0 effectively removes the point, and the box does not accept nonnumerical values or values outside of [0,1].");
		this.placeObject(_text[8], 0, 780);
		_text[8].setWrappingWidth(Constants.STAGE_WIDTH-60);
		
		_text[9] = new Text("IV. Page 3: Global Properties");
		this.placeHeader(_text[9], 1040);
		
		_text[10] = new Text("This page deals with global properties of the fractal and contractive functions being implemented. The contraction rate "
				+ "deals with how quickly points move towards attractor, and it is standard to put the rate between 0 and 1. Negative numbers "
				+ "are allowed, but cause repulsion, and numbers greater than 1 will cause points to overshoot past the attractor. Setting "
				+ "contraction rate to 0 will result in white noise and fail to create a fractal. \n \n"
				+ "The user also selects a type of contraction on a drop down menu to continue. There are currently four types: Constant, "
				+ "Exponential, Harmonic, and Fibonacci. Each contracts at a different rate as described in the menu. Constant is the standard "
				+ "type of contraction, and very little is known about the other types of contractions. There are many more rates of contraction "
				+ "that are not supported at the moment. \n \n"
				+ "The last property is “Level of Recursions” which controls the detail of the fractal by telling each point placed how many "
				+ "random walks to take before moving on to the next point. The standard level is ~12, and it is not recommended the the user "
				+ "goes above 17 or below 7 to balance out detail with computation time. Only integers with a value greater than 0 are accepted.");
		this.placeObject(_text[10], 0, 1070);
		_text[10].setWrappingWidth(Constants.STAGE_WIDTH-60);
		
		_text[11] = new Text("V. Page 4: Generating the Fractal");
		this.placeHeader(_text[11], 1350);
		
		_text[12] = new Text("Whew! That was some work, but we’re finally ready to generate the fractal. Input the amount of points you would like to add "
				+ "to the graph (recommended ~10,000) and press “Add Points”. Watch the fractal start to form before your eyes by repeatedly pressing "
				+ "“Add Points”. You can change the number of points you wish to add (no non integer values, values less than 0, or non-numerical "
				+ "values will be accepted). You can toggle the visibility of the grid, attracting points, and attractor hull (used for point placement) "
				+ "as desired.");
		this.placeObject(_text[12], 0, 1380);
		_text[12].setWrappingWidth(Constants.STAGE_WIDTH-60);
		
		_text[13] = new Text("VI. Examples");
		this.placeHeader(_text[13], 1480);
		
		_text[14] = new Text("PENTAGONAL N-FLAKE"
				+ "\n\nCoordinates: "
				+ "\n	Attractor 1: (.5, .95)"
				+ "\n	Attractor 2: (.0244717, .6045085)"
				+ "\n	Attractor 3: (.9755283, .6045085)"
				+ "\n	Attractor 4: (.2061074, .0454915)"
				+ "\n	Attractor 5: (.7938926, .0454915)"
				+ "\n\nConnections:"
				+ "\n	Each attractor connects to every other attractor (including self)"
				+ "\n\nContraction Rate/Type:"
				+ "\n	Linear"
				+ "\n\nRate: 1/phi ~ .6180350	(phi is the Golden ratio)"
				+ "\n\nRecursions:"
				+ "\n 	12 - 14"
				+ "\n\nSIERPINSKI TRIANGLE"
				+ "\n\nCoordinates: "
				+ "\n	Attractor 1: (.1, .1)"
				+ "\n	Attractor 2: (.9, .1)"
				+ "\n	Attractor 3: (.5, .79282)"
				+ "\n\nConnections:"
				+ "\n	Each attractor connects to every other attractor (including self)"
				+ "\n\nContraction Rate/Type:"
				+ "\n	Linear"
				+ "\n\nRate: .5"
				+ "\n\nRecursions:"
				+ "\n	12 - 14"
				+ "\n\nOVERLAPPING SIERPINSKI TRIANGLES (Very similar to above, just use exponential contraction)"
				+ "\n\nCoordinates: "
				+ "\n	Attractor 1: (.1, .1)"
				+ "\n	Attractor 2: (.9, .1)"
				+ "\n	Attractor 3: (.5, .79282)"
				+ "\n\nConnections:"
				+ "\n	Each attractor connects to every other attractor (including self)"
				+ "\n\nContraction Rate/Type:"
				+ "\n	Exponential"
				+ "\n\nRate: .5"
				+ "\n\nRecursions:"
				+ "\n	12 - 14"
				+ "\n\n4 LEAF CLOVER"
				+ "\n\nCoordinates:"
				+ "\n	Attractor 1: (.1, .1)"
				+ "\n	Attractor 2: (.9, .1)"
				+ "\n	Attractor 3: (.1, .9)"
				+ "\n	Attractor 4: (.9, .9)"
				+ "\n	Attractor 5: (.5, .5)"
				+ "\n\nConnections:"
				+ "\n	Attractors 1 connects to: 2,4,5"
				+ "\n	Attractor 2 connects to: 3,4,5"
				+ "\n	Attractor 3 connects to: 1,2,5"
				+ "\n	Attractor 4 connects to: 1,3,5"
				+ "\n	Attractor 5 connects to: 1,2,3,4"
				+ "\n\nContraction Rate/Type:"
				+ "\n	Linear"
				+ "\n\nRate: .5"
				+ "\n\nRecursions:"
				+ "\n	12 - 14"
				+ "\n\nCANTOR SET"
				+ "\n\nCoordinates:"
				+ "\n	Attractor 1: (.1, .1)"
				+ "\n	Attractor 2: (.9, .9)"
				+ "\n\nConnections:"
				+ "\n	Each attractor connects to every other attractor (including self)"
				+ "\n\nContraction Rate/Type:"
				+ "\n	Linear"
				+ "\n\nRate: 2/3 ~ .6666667"
				+ "\n\nRecursions:"
				+ "\n	12 - 14");
		
		this.placeObject(_text[14], 0, 1510);
		_text[14].setWrappingWidth(Constants.STAGE_WIDTH-60);
		
		//Setting up scroll bars on help screen
		helpText.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		helpText.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		
		helpText.setVmax(1500);
		
		helpText.setPadding(new Insets(30));
		helpText.setFitToWidth(true);

		helpText.setContent(_textPane);
		_helpRoot.setCenter(helpText);
	}
	
	/* Wraps functions together to allow easier placement of nodes (text) on the pane */
	private void placeObject(Node node, double xPos, double yPos){
		node.setLayoutX(xPos);
		node.setLayoutY(yPos);
		_textPane.getChildren().add(node);
	}
	
	/* Wraps functions together to allow easier placement of bold text on pane. */
	private void placeHeader(Text text, double yPos){
		this.placeObject(text, 0, yPos);
		text.setStyle("-fx-font-weight: bold");
	}
	
}
