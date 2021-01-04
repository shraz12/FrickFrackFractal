//In FractalGrapher.App
package FractalGrapher;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/*
 * The OptionsPanes class. Deals with all the GUI aspects of the fractal generators.
 * The crux of this classed are the 4 setUpPage methods, which allow the user to morph
 * properties of the attractors and generate the fractal. There is a heavy use of EventHandlers
 * in the class to allow buttons, check boxes, color picker, and text fields react to 
 * user input.
 */
public class OptionsPane {
	private FractalBoard _board;
	private Pane _optionsPane;
	
	private TextField[] _page1Inputs;
	private Button[] _page1Buttons;
	private Label[] _page1Labels;
	private ColorPicker _page1Color;
	
	private TextField[] _page2Inputs;
	private Button[] _page2Buttons;
	private Label[] _page2Labels;
	private CheckBox[] _page2CheckBoxes;
	
	private TextField[] _page3Inputs;
	private Button[] _page3Buttons;
	private Label[] _page3Labels;
	private ComboBox[] _page3DropDowns;
	private String[] _contractionOptions;
	
	private TextField[] _page4Inputs;
	private Button[] _page4Buttons;
	private Label[] _page4Labels;
	private CheckBox[] _page4CheckBoxes;
	
	private int _currentPage;
	
	/*
	 * Constructor method. Instatiates all the necessary GUI arrays that store the nodes
	 * and sets up page 1 for the user to place attractors.
	 */
	public OptionsPane(Pane pane, FractalBoard board){
		_optionsPane = pane;
		_board = board;
		_currentPage = 1;
		this.makeGUIArrays();
		this.setUpPage1(_optionsPane);
		
	}
	
	/* 
	 * Accessor method. Called by FractalGenerator to determine effect of mouse clicks on 
	 * board.
	 */
	public int getCurrentPage(){
		return _currentPage;
	}
	
	/* 
	 * Accessor method. Returns the color of the attractor node when called. Used to determine
	 * the gradient color of the fractal.
	 */
	public Color getCurrentColor(){
		return _page1Color.getValue();
	}
	
	/*
	 * Called in constructor. Instantiates all the necessary arrays that store the GUI nodes in 
	 * their respective data types.
	 */
	private void makeGUIArrays(){
		_page1Inputs = new TextField[2];
		_page1Buttons = new Button[3];
		_page1Labels = new Label[5];
		
	
		_page2Inputs = new TextField[2];
		_page2Buttons = new Button[3];
		_page2Labels = new Label[5];
		_page2CheckBoxes = new CheckBox[2];
		
		_page3Inputs = new TextField[4];
		_page3Buttons = new Button[4];
		_page3Labels = new Label[5];
		_page3DropDowns = new ComboBox[1];
		
		_page4Inputs = new TextField[2];
		_page4Buttons = new Button[3];
		_page4Labels = new Label[5];
		_page4CheckBoxes = new CheckBox[3];
		
		String[] options = {"Constant (x)", "Exponential (x^n)", "Harmonic Series (x/n)", "Fibonacci (x/F(n))"};
		_contractionOptions = options;
	}
	
	/*
	 * Sets up the GUI components for page 1, the place attractors page. Creates all
	 * the necessary nodes and adds them to the page, storing each node in an array based
	 * on class.
	 */
	private void setUpPage1(Pane pane){
		for (int i=0; i< 2; i++){ // Index 0 xPos, Index 1 yPos
			TextField coord = new TextField();
			this.placeObject(coord, 650, 200+(80*i));
			coord.setMaxWidth(80);
			_page1Inputs[i] = coord;
		}
		
		_page1Labels[0] = new Label("Set X Coordinate (0,1)");
		this.placeObject(_page1Labels[0], 650, 175);
		_page1Labels[1] = new Label("Set Y Coordinate (0,1)");
		this.placeObject(_page1Labels[1], 650, 255);
		
		_page1Buttons[0] = new Button("Set Attracting Point");
		this.placeObject(_page1Buttons[0], 650, 340);
		_page1Buttons[0].setOnAction(new SetCoords());
		
		_page1Buttons[1] = new Button ("Next Page");
		this.placeObject(_page1Buttons[1], 780, 550);
		_page1Buttons[1].setOnAction(new Page1Switch());
		
		_page1Labels[2] = new Label("Place at least 1 attractor");
		_page1Labels[2].setTranslateX(650);
		_page1Labels[2].setTranslateY(500);
		_page1Labels[2].setTextFill(Color.RED);
		
		_page1Labels[3] = new Label("Set Attractors");
		_page1Labels[3].setTextFill(Color.CORNFLOWERBLUE);
		_page1Labels[3].setScaleX(2.5);
		_page1Labels[3].setScaleY(2.5);
		this.placeObject(_page1Labels[3], 740, 80);
		
		_page1Color = new ColorPicker(Color.BLACK);
		this.placeObject(_page1Color, 650, 435);

		
		_page1Labels[4] = new Label("Choose Attractor Color");
		this.placeObject(_page1Labels[4], 650, 400);
		
		_page1Buttons[2] = new Button("Undo");
		this.placeObject(_page1Buttons[2], 690, 550);
		_page1Buttons[2].setOnAction(new Undo());
	}
	
	/*
	 * Sets up the GUI components for page 2, the attractor properties page. Creates all
	 * the necessary nodes and adds them to the page, storing each node in an array based
	 * on class.
	 */
	private void setUpPage2(Pane pane){
		_currentPage = 2;
		_board.setAttractorHighlight(0);
		
		_page2Labels[0] = new Label("Set Connections \n and Probability");
		_page2Labels[0].setTextFill(Color.PURPLE);
		_page2Labels[0].setScaleX(2.5);
		_page2Labels[0].setScaleY(2.5);
		this.placeObject(_page2Labels[0], 735, 80);
		
		_page2Labels[1] = new Label("Attractor " + Integer.toString(_board.getCurrentAttractor() + 1) + " of "
				+ Integer.toString(_board.getNumAttractors()));
		_page2Labels[1].setStyle("-fx-font-weight: bold");
		this.placeObject(_page2Labels[1], 720, 170);
		
		_page2CheckBoxes[0]= new CheckBox("Connect to All");
		this.placeObject(_page2CheckBoxes[0], 650, 230);
		_page2CheckBoxes[0].setOnAction(new AutoPaths0());
		
		_page2CheckBoxes[1]= new CheckBox("Connect to All But Self");
		this.placeObject(_page2CheckBoxes[1], 650, 290);
		_page2CheckBoxes[1].setOnAction(new AutoPaths1());
		
		_page2Buttons[0] = new Button("Clear All");
		_page2Buttons[0].setOnAction(new ClearPathsBtn());
		this.placeObject(_page2Buttons[0], 650, 350);
		
		_page2Inputs[0] = new TextField("1");
		_page2Inputs[0].setMaxWidth(80);
		this.placeObject(_page2Inputs[0], 650, 430);
		
		_page2Labels[4] = new Label("Set Relative Probability (0,1)");
		this.placeObject(_page2Labels[4], 650, 405);
		
		//User Error messages
		_page2Labels[2] = new Label("Please set at least 1 connection");
		_page2Labels[2].setTextFill(Color.RED);
		this.placeObject(_page2Labels[2], 650, 475);
		_page2Labels[2].setVisible(false);
		
		_page2Labels[3] = new Label("Please type in a valid probability \nbetween 0 and 1");
		_page2Labels[3].setTextFill(Color.RED);
		this.placeObject(_page2Labels[3],650,495);
		_page2Labels[3].setVisible(false);
		
		//Checks to see which attractor user is currently editing. Adds button based on result.
		if(_board.getCurrentAttractor() == (_board.getNumAttractors()-1)){
			_page2Buttons[2] = new Button ("Set Global Properties");
			this.placeObject(_page2Buttons[2], 700, 555);
			_page2Buttons[2].setOnAction(new Page2Switch());
		}
		else{
			_page2Buttons[1] = new Button ("Next Attractor");
			this.placeObject(_page2Buttons[1], 725, 555);
			_page2Buttons[1].setOnAction(new Page2Recycle());
		}
	}
	
	/*
	 * Sets up the GUI components for page 3, the global properties page. Creates all
	 * the necessary nodes and adds them to the page, storing each node in an array based
	 * on class.
	 */
	private void setUpPage3(Pane pane){
		_currentPage = 3;
		
		_page3Labels[0] = new Label("Set Contraction \nRate and \nRecursions");
		_page3Labels[0].setTextAlignment(TextAlignment.CENTER);
		_page3Labels[0].setTextFill(Color.DEEPPINK);
		_page3Labels[0].setScaleX(2.5);
		_page3Labels[0].setScaleY(2.5);
		this.placeObject(_page3Labels[0], 735, 80);
		
		_page3Inputs[0] = new TextField();
		_page3Inputs[0].setMaxWidth(80);
		this.placeObject(_page3Inputs[0], 650, 245);
		
		_page3Labels[1] = new Label("Contraction Rate");
		this.placeObject(_page3Labels[1], 650, 210);
		
		_page3Labels[2] = new Label("Type of Contraction");
		this.placeObject(_page3Labels[2], 650, 300);
		
		_page3DropDowns[0] = new ComboBox<String>(FXCollections.observableArrayList(_contractionOptions));
		_page3DropDowns[0].setPromptText("Pick Contraction Type");
		this.placeObject(_page3DropDowns[0], 645, 335);
		
		_page3Labels[3] = new Label("Level of Recursions (Whole Numbers)");
		this.placeObject(_page3Labels[3], 650, 390);
		
		_page3Inputs[1] = new TextField();
		_page3Inputs[1].setMaxWidth(80);
		this.placeObject(_page3Inputs[1], 650, 425);
		
		_page3Labels[4] = new Label("Please select a contraction \ntype and put in valid inputs");
		_page3Labels[4].setTextFill(Color.RED);
		this.placeObject(_page3Labels[4],650, 475);
		_page3Labels[4].setVisible(false);
		
		_page3Buttons[0] = new Button ("Fractalize");
		this.placeObject(_page3Buttons[0], 725, 540);
		_page3Buttons[0].setOnAction(new Page3Switch());
	}
	
	/*
	 * Sets up the GUI components for page 4, the fractal generation page. Creates all
	 * the necessary nodes and adds them to the page, storing each node in an array based
	 * on class.
	 */
	private void setUpPage4(Pane pane){
		_currentPage = 4;
		
		_page4Labels[0] = new Label("Generate Fractal");
		_page4Labels[0].setTextFill(Color.MEDIUMAQUAMARINE);
		_page4Labels[0].setScaleX(2.5);
		_page4Labels[0].setScaleY(2.5);
		this.placeObject(_page4Labels[0], 730, 80);
		
		_page4Inputs[0] = new TextField();
		_page4Inputs[0].setMaxWidth(80);
		this.placeObject(_page4Inputs[0], 740, 170);
		
		_page4Labels[1] = new Label("Points to Add");
		_page4Labels[1].setStyle("-fx-font-weight: bold");
		this.placeObject(_page4Labels[1], 735, 140);
		
		_page4Buttons[0] = new Button("Add Points");
		this.placeObject(_page4Buttons[0], 735, 230);
		_page4Buttons[0].setOnAction(new AddPoints());
		
		_page4CheckBoxes[0] = new CheckBox("Show Grid Lines");
		_page4CheckBoxes[0].setSelected(true);
		_page4CheckBoxes[0].setOnAction(new ShowGridLines());
		this.placeObject(_page4CheckBoxes[0], 650, 300);
		
		_page4CheckBoxes[1] = new CheckBox("Show Attractors");
		_page4CheckBoxes[1].setSelected(true);
		_page4CheckBoxes[1].setOnAction(new ShowAttractors());
		this.placeObject(_page4CheckBoxes[1], 650, 350);
		
		_page4CheckBoxes[2] = new CheckBox("Show Attractor Hull");
		_page4CheckBoxes[2].setOnAction(new ShowHull());
		this.placeObject(_page4CheckBoxes[2], 650, 400);
		
		_page4Buttons[1] = new Button("Screenshot");
		_page4Buttons[1].setOnAction(new Screenshot());
		
		this.placeObject(_page4Buttons[1], 735, 460);
	}
	
	/*
	 * Method which tries casting the input in the coordinates box as two doubles
	 * both between 0 and 1. Returns a boolean based on the success.
	 */
	private boolean tryDoubleCasting(String xPos, String yPos ){
		boolean returnVal = true;
		try{
			double xTest = Double.valueOf(xPos);
			double yTest = Double.valueOf(xPos);
		}
		catch(Exception e){
			returnVal = false;
		}
		return returnVal;
	}
	
	/*
	 * Method which tries casting the input in the Relative Probability box as a 
	 * double between 0 and 1. Returns a boolean based on the success.
	 */
	private boolean tryProbabilityCast(String test){
		boolean returnVal = true;
		double prob = -1;
		
		try{
			prob = Double.valueOf(test);
		}
		catch(Exception e){
			returnVal = false;
		}
		
		if(prob < 0 || prob > 1){
			returnVal = false;
		}
		
		return returnVal;
	}
	
	/*
	 * Method which tries casting the input in the Contraction Rate box as a double. 
	 * Returns a boolean based on the success.
	 */
	private boolean tryCastingContraction(String test){
		boolean returnVal = true;
		double num = -1;
		
		try{
			num = Double.valueOf(test);
		}
		catch(Exception e){
			returnVal = false;
		}
		
		return returnVal;
	}
	
	/*
	 * Method which tries casting the input in the recursion box as a positive 
	 * integer. Returns a boolean based on the success.
	 */
	private boolean tryCastingRecursions(String test){
		boolean returnVal = true;
		int num = -1;
		
		try{
			num = Integer.valueOf(test);
		}
		catch(Exception e){
			returnVal = false;
		}
		if(num <= 0){
			returnVal = false;
		}
		
		return returnVal;
	}
	
	/*
	 * Method called for easier placement of nodes on the screen by progammer. Wraps
	 * multiple functions together.
	 */
	private void placeObject(Node node, double xPos, double yPos){
		node.setTranslateX(xPos);
		node.setTranslateY(yPos);
		_optionsPane.getChildren().add(node);
	}
	
	/* Page 1 Private Classes */
	
	/*
	 * Simple event handler which removes the last attractor placed, either on the board
	 * directly or by coordinate setting.
	 */
	private class Undo implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			_board.undoLastAttractor();
		}
	}
	
	/*
	 * Event handler which takes the two inputs from the TextFields meant for coordinates
	 * and adds attractor based on coordinates (rescales from normalized unit square). Checks
	 * validity of input first and does nothing if input is nonnumerical.
	 */
	private class SetCoords implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			if(tryDoubleCasting(_page1Inputs[0].getText(),_page1Inputs[1].getText())){
				double xCoord = Double.valueOf(_page1Inputs[0].getText());
				double yCoord = Double.valueOf(_page1Inputs[1].getText());
				if (xCoord <= 1 && xCoord >= 0 && yCoord <= 1 && yCoord >= 0){
					_board.setAttractingPoint(25+ (xCoord*Constants.BOARD_SIDE_LENGTH), 
							25+((1-yCoord)*Constants.BOARD_SIDE_LENGTH), _page1Color.getValue());
					_page1Inputs[0].setText("");
					_page1Inputs[1].setText("");
				}
			}
			e.consume();
		}
	}
	
	/*
	 * Checks validity of inputs (meaning at least one attractor is placed), and if possible,
	 * moves to page 2 graphics. 
	 */
	private class Page1Switch implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			//Shows error message if no attractors are placed
			if(_board.getCurrentAttractor() == -1){
				if(!_optionsPane.getChildren().contains(_page1Labels[2])){
					_optionsPane.getChildren().add(_page1Labels[2]);
				}
			}
			else{
				_optionsPane.getChildren().removeAll(_page1Inputs);
				_optionsPane.getChildren().removeAll(_page1Buttons);
				_optionsPane.getChildren().removeAll(_page1Labels);
				_optionsPane.getChildren().remove(_page1Color);
				_board.toRandomWalkDefs();
				setUpPage2(_optionsPane);
			}
		}
	}
	
	/* Page 2 Private Classes */
	
	/*
	 * Event handler which recycles the graphics and GUI of page 2 when moving on to the
	 * next attractor (excluding last attractor). After checking validity of inputs, 
	 * updates the FractalBoard and moves to next attracting point.
	 */
	private class Page2Recycle implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			_page2Labels[2].setVisible(false);
			_page2Labels[3].setVisible(false);
			
			if(tryProbabilityCast(_page2Inputs[0].getText()) && 
					(_board.getNumConnections(_board.getCurrentAttractor()) != 0)){
				//Updating fractal board
				double prob = Double.valueOf(_page2Inputs[0].getText());
				_board.setProbability(prob, _board.getCurrentAttractor());
				
				//Recycling GUI/graphics
				_board.removePathGraphics();
				_page2Inputs[0].setText("1");
				_page2CheckBoxes[0].setSelected(false);
				_page2CheckBoxes[1].setSelected(false);
				
				_board.setAttractorHighlight(_board.getCurrentAttractor() + 1);
				_board.setCurrentAttractor(_board.getCurrentAttractor() + 1);
				
				//Creates new button in place of Next Attractor if moving to last attractor
				if(_board.getCurrentAttractor() == (_board.getNumAttractors()-1)){
					_page2Buttons[2] = new Button ("Set Global Properties");
					placeObject(_page2Buttons[2], 700, 555);
					_page2Buttons[2].setOnAction(new Page2Switch());
					_optionsPane.getChildren().remove(_page2Buttons[1]);
				}
				
				_page2Labels[1].setText("Attractor " + Integer.toString(_board.getCurrentAttractor() + 1) + " of "
				+ Integer.toString(_board.getNumAttractors()));
			}
			
			//Sets user error messages to visible
			else{
				if(!tryProbabilityCast(_page2Inputs[0].getText())){
					_page2Labels[3].setVisible(true);
				}
				if(!(_board.getNumConnections(_board.getCurrentAttractor()) != 0)){
					_page2Labels[2].setVisible(true);
				}
			}
		}
	}
	
	/*
	 * Called in place of Page2Recycle private class if the user is modifying the last attractor
	 * (visible underneath title of page). Removes all the page 2 graphics can creates 3rd page
	 * graphics after checking validity of inputs and updating FractalBoard.
	 */
	private class Page2Switch implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){

			_page2Labels[2].setVisible(false);
			_page2Labels[3].setVisible(false);
			if(tryProbabilityCast(_page2Inputs[0].getText()) && 
					(_board.getNumConnections(_board.getCurrentAttractor()) != 0)){
				double prob = Double.valueOf(_page2Inputs[0].getText());
				//Updating board
				_board.setProbability(prob, _board.getCurrentAttractor());
				_board.removePathGraphics();
				
				//Removes page 2 graphics
				_optionsPane.getChildren().removeAll(_page2Labels);
				_optionsPane.getChildren().removeAll(_page2Inputs);
				_optionsPane.getChildren().removeAll(_page2CheckBoxes);
				_optionsPane.getChildren().removeAll(_page2Buttons);
				
				_board.removeAttractorHighlight();
				_board.setCurrentAttractor(0);
				
				setUpPage3(_optionsPane);
			}
			//Sets user error messages to visible
			else{
				if(!tryProbabilityCast(_page2Inputs[0].getText())){
					_page2Labels[3].setVisible(true);
				}
				if(!(_board.getNumConnections(_board.getCurrentAttractor()) != 0)){
					_page2Labels[2].setVisible(true);
				}
			}
		}
	}

	/*
	 * When called, this event handler removes every path, including self, the the current 
	 * attractor is connected to.
	 */
	private class ClearPathsBtn implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			for(int i = 0; i < _board.getNumAttractors(); i++){
				_board.removePath(_board.getCurrentAttractor(), i);
			}
			for(int i = 0; i < _page2CheckBoxes.length; i++){
				_page2CheckBoxes[i].setSelected(false);
			}
		}
	}
	
	/*
	 * This event handler auto sets paths when clicked. Connects current attractor
	 * to every other attractor. Called by respective CheckBox.
	 */
	private class AutoPaths0 implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			//To avoid problem of writing same path twice
			for(int i = 0; i < _board.getNumAttractors(); i++){
				_board.removePath(_board.getCurrentAttractor(), i); 
			}
			_page2CheckBoxes[1].setSelected(false);
			for(int i = 0; i < _board.getNumAttractors(); i++){
				_board.setPath(_board.getCurrentAttractor(), i);
			}		
		}
	}
	
	/*
	 * This event handler auto sets paths when clicked. Connects current attractor
	 * to every other attractor and removes connection from self. Called by respective
	 * CheckBox.
	 */
	private class AutoPaths1 implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			//To avoid problem of writing same path twice
			for(int i = 0; i < _board.getNumAttractors(); i++){
				_board.removePath(_board.getCurrentAttractor(), i); 
			}
			_page2CheckBoxes[0].setSelected(false);
			for(int i = 0; i < _board.getNumAttractors(); i++){
				_board.setPath(_board.getCurrentAttractor(), i);
			}
			_board.removePath(_board.getCurrentAttractor(), _board.getCurrentAttractor());
		}
	}
	
	/* Page 3 Private Classes */
	
	/*
	 * Handles the switching from Page 3 to Page 4. Checks validity of inputs on the 
	 * page, if allowed, updates the FractalBoard in respective ways.
	 */
	private class Page3Switch implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			_page3Labels[4].setVisible(false);
			if((_page3DropDowns[0].getValue() != null) && tryCastingContraction(_page3Inputs[0].getText())
					&& tryCastingRecursions(_page3Inputs[1].getText())){
				
				//Updating FractalBoard with proper user variables
				_board.setContractionRate(Double.valueOf(_page3Inputs[0].getText()));
				_board.setRecursionLevel(Integer.valueOf(_page3Inputs[1].getText()));
				
				switch((String)_page3DropDowns[0].getValue()){
				
					case "Constant (x)":
						_board.setContractionType(1);
						break;
						
					case "Exponential (x^n)":
						_board.setContractionType(2);
						break;
						
					case "Harmonic Series (x/n)":
						_board.setContractionType(3);
						break;
						
					case "Fibonacci (x/F(n))":
						_board.setContractionType(4);
						break;
				}
				
				//Removing page 3 GUI
				_optionsPane.getChildren().removeAll(_page3Inputs);
				_optionsPane.getChildren().removeAll(_page3Buttons);
				_optionsPane.getChildren().removeAll(_page3Labels);
				_optionsPane.getChildren().removeAll(_page3DropDowns);
				
				setUpPage4(_optionsPane);
				
			}
			else{
				_page3Labels[4].setVisible(true);
			}
		}
	}
	
	/* Page 4 Private Classes */
	
	/*
	 * Takes a screenshot when the respective button is clicked. Crops image to just the
	 * fractal board and allows user to save image to a custom location.
	 */
	private class Screenshot implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			
			WritableImage oldImage = _optionsPane.snapshot(new SnapshotParameters(), null);
			PixelReader reader = oldImage.getPixelReader();
			WritableImage newImage = new WritableImage(reader, Constants.BOARD_SHIFT_X, 
					Constants.BOARD_SHIFT_Y, Constants.BOARD_SIDE_LENGTH, Constants.BOARD_SIDE_LENGTH);
		
			FileChooser fileChooser = new FileChooser();
		    fileChooser.getExtensionFilters().add( new ExtensionFilter("PNG", "*.png"));
			fileChooser.setTitle("Save Fractal Image");
			fileChooser.setInitialFileName("fractal.png");
			
			Stage chooseSaveLoc = new Stage();
			File file = fileChooser.showSaveDialog(chooseSaveLoc);
			
			if(file != null){
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(newImage, null), "png", file);
					}
				catch (IOException exception) {
				    }
			}
		}
	}
	
	/*
	 * Event handler which shows/removes the convex hull when the respective CheckBox
	 * is checked.
	 */
	private class ShowHull implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			if(_page4CheckBoxes[2].isSelected()){
				_board.showConvexHull();
			}
			else{
				_board.hideConvexHull();
			}
		}
	}
	
	/*
	 * Event handler which shows/removes the attracting points on the graph when the
	 * respective CheckBox is checked.
	 */
	private class ShowAttractors implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			if(_page4CheckBoxes[1].isSelected()){
				_board.showAttractorGraphics();
			}
			else{
				_board.hideAttractorGraphics();
			}
		}
	}

	/*
	 * Event handler which shows/removes grid lines on the graph when the respective 
	 * CheckBox is checked.
	 */
	private class ShowGridLines implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			if(_page4CheckBoxes[0].isSelected()){
				_board.showGridLines();
			}
			else{
				_board.hideGridLines();
			}
		}
	}
	
	/*
	 * This event handler is called when the add points button is clicked to add the
	 * the points which approximate the fractal. Checks validity of input and adds the
	 * points if possible
	 */
	private class AddPoints implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e){
			boolean canAdd = true;
			int numPoints = -1;
			
			try{
				numPoints = Integer.valueOf(_page4Inputs[0].getText());
			}
			catch(Exception x){
				canAdd = false;
			}
			if(numPoints < 0){
				canAdd = false;
			}
			
			if(canAdd){
				_board.randomWalk(numPoints);
			}
		}
	}
	
}
