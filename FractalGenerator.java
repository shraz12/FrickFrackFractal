//In FractalGrapher.App
package FractalGrapher;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/*
 * The FractalGenerator class. While having very limited functionality within the class
 * itself, the main purpose is to tie together the FractalBoard and OptionsPane class to
 * allow the user to communicate directly with the fractal board throughout the generation
 * process. A more top-level class.
 */
public class FractalGenerator {
	
	private Pane _fractalPane;
	private BorderPane _root;
	private FractalBoard _board;
	private OptionsPane _options;

	/*
	 * The constructor. Instantiates the both FractalBoard and OptionsPane to get the
	 * prorgam running. Also sets up an EventHandler to deal with mouse events and adds
	 * the whole fractal board and GUI to root pane.
	 */
	public FractalGenerator(BorderPane root){
		_root = root;
		_fractalPane = new Pane();
		_board = new FractalBoard(_fractalPane);
		_options = new OptionsPane(_fractalPane, _board);
		_root.setCenter(_fractalPane);
		
		_fractalPane.addEventFilter(MouseEvent.MOUSE_CLICKED, new MouseHandler());
		_fractalPane.setFocusTraversable(true);
		_fractalPane.requestFocus();
		
	}
	
	/*
	 * MouseHandler private class, which allowss the user to interact with the
	 * board on which the fractal is graphed. Based on the stage in the fractal
	 * generation process, this class allows the user to use the mouse to place
	 * points and set paths.
	 */
	private class MouseHandler implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent e){
			double xClick = e.getX();
			double yClick = e.getY();
			int page = _options.getCurrentPage();
			
			switch(page){
			
			case 1:
				Color color = _options.getCurrentColor();
				_board.setAttractingPoint(xClick, yClick, color);
				break;
				
			case 2:
				_board.drawPathMouse(xClick, yClick);
				break;
			
			}

			e.consume();
		}
	}
	
	
}
