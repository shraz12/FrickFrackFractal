//In FractalGrapher.App
package FractalGrapher;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*
 * The PaneOrganizer class, a top level class which deals with graphics and proper placement of 
 * graphics on screen. Also creates some general functions (opening a help page, quit button), 
 * which function independently from the actual FractalGenerator class.
 */
public class PaneOrganizer {
	
	private BorderPane _root;
	private FractalGenerator _fractalGenerator;
	
	/*
	 * Constructor method. Creates a root to which all graphics, save the HelpPage graphics,
	 * are attached to. Sets up respective EventHandlers and creates an instance of FractalGenerator
	 * to get program going.
	 */
	public PaneOrganizer(){
		_root = new BorderPane();
		_fractalGenerator = new FractalGenerator(_root);
		_root.setStyle("-fx-background-color: white");
		this.makeGlobalGraphics();
		
		_root.addEventFilter(KeyEvent.KEY_PRESSED, new KeyHandler());
		_root.setFocusTraversable(true);
		_root.requestFocus();
	}
	
	/* Accessor method. Returns root pane containing all the graphics */
	public BorderPane getRoot(){
		return _root;
	}
	
	/*
	 * Makes and adds graphics, namely a button and help text, which are visible throughout
	 * fractal generation process.
	 */
	private void makeGlobalGraphics(){
		//Making the quit button
		HBox bottomPane = new HBox();
		_root.setBottom(bottomPane);
		bottomPane.setAlignment(Pos.BOTTOM_RIGHT);
		Button btn = new Button("Quit");
		btn.setOnAction(new ClickHandler());
		
		//Help label
		Label label = new Label("Press F1 or H for help");
		label.setTranslateY(-10);
		label.setStyle("-fx-font-weight: bold");
		bottomPane.getChildren().add(label);
		bottomPane.getChildren().add(btn);
		bottomPane.setSpacing(20);
	}
	
	/*
	 * EventHandler which responds based on keys pressed by the user. At any time in the fractal
	 * generation process, when called, opens up a new window containing an inbuilt user guide.
	 */
	private class KeyHandler implements EventHandler<KeyEvent>{
		@Override 
		public void handle(KeyEvent e){
			KeyCode key = e.getCode();
			if(key == KeyCode.F1 || key == KeyCode.H){
				HelpPage help = new HelpPage();
				Stage stage = new Stage();
				Scene scene = new Scene(help.getRoot(),Constants.STAGE_WIDTH,Constants.STAGE_HEIGHT);
				stage.setScene(scene);stage.setTitle("FrickFrackFractal Help");
				stage.show();
			}
		}
	}
	
	/* Private EventHandler class attached to quit button. Exists all aspects of the program */
	private class ClickHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Platform.exit();
		}
	}

}
