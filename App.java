//In FractalGrapher.App
package FractalGrapher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * The topmost class. When called, starts stage and creates a PaneOrganizer to
 * get the app going. More information about program and design choices found in
 * the README.
 */
public class App extends Application {
	
	/*
	 * Start method, used in place constructor. Creates PaneOranizer and passes through
	 * a Scene object to start program.
	 */
	@Override
	public void start(Stage stage){
		PaneOrganizer organizer = new PaneOrganizer();
		Scene scene = new Scene(organizer.getRoot(),Constants.STAGE_WIDTH,Constants.STAGE_HEIGHT);
		stage.setScene(scene);stage.setTitle("FrickFrackFractal");
		stage.setResizable(true);
		stage.show();
	}
	
	/*
	 * Mainline arg
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
