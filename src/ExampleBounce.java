import javafx.animation.KeyFrame;
import javafx.geometry.Point2D;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class ExampleBounce extends Application {
    
	//Set up properties
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.HONEYDEW;
	public static final String TITLE = "BreakOut";
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	public Timeline animation;
	public Level level1;
	
	public ImageView[] bricks;
 
	//Manage different scenes
	static Stage stage;
	TransitionScenes transition = new TransitionScenes();
	Scene scene1, scene2,scene3;
	Button button1,button2,button3;
	Level current;

	

	@Override
	public void start(Stage primaryStage) {
		animation = new Timeline();
        
	
		stage = primaryStage;
	
	
	        
	     
	     
		
		level1 = new Level(animation,stage,1);
		// attach scene to the stage and display it
		Scene scene1 = level1.setupGame(SIZE, SIZE, BACKGROUND);
		//Scene scene1=createNewScene(1,level1);
		
		stage.setScene(scene1);
		stage.setTitle(TITLE);
		stage.show();
		showPopUp(1);
		showPopUp(2);
		showPopUp(3);
		
		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> level1.step(SECOND_DELAY));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		
	}
	
	public void createNewScene(int levelNumber) {
		animation = new Timeline();
	     
	     
		
		level1 = new Level(animation,stage,levelNumber);
		// attach scene to the stage and display it
		Scene scene1 = level1.setupGame(SIZE, SIZE, BACKGROUND);
		//Scene scene1=createNewScene(1,level1);
		
		stage.setScene(scene1);
		stage.setTitle(TITLE);
		stage.show();
		showPopUp(1);
		
		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> level1.step(SECOND_DELAY));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		
	}
	
	public void ButtonClicked(ActionEvent e)
    {
        if (e.getSource()==button1) {
        	    createNewScene(1);
           }
        
    }

	

	
	
	public void showPopUp(int type) {
		Popup popup=new Popup();
		popup.setX(450+type*100);
		popup.setY(500);
		
		
		button1=new Button("Level 1");
		button1.setOnAction(e-> ButtonClicked(e));
		popup.getContent().add(button1);
		popup.show(stage);
		
	}

	


	// Create the game's "scene": what shapes will be in the game and their starting
	// properties

	public static void main(String[] args) {

		launch(args);
		

	}

}
