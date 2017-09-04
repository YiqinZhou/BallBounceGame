import javafx.animation.KeyFrame;
import javafx.geometry.Point2D;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class ExampleBounce extends Application {
  
    public static final int SIZE = 400;
    public static final Paint BACKGROUND = Color.WHITE;
    public static final String TITLE = "BreakOut";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  
    public Timeline animation;  
    public Level level1;
    public TransitionScenes transition=new TransitionScenes();
    public int[] brickConfig= {1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3};
    public ImageView[] bricks;
    
    @Override
    public void start (Stage s) {
        animation = new Timeline();

    	    level1=new Level(animation);
        // attach scene to the stage and display it
        Scene scene = level1.setupGame(SIZE, SIZE, BACKGROUND);
        s.setScene(scene);
        s.setTitle(TITLE);
        s.show();
        // attach "game loop" to timeline to play it
        
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> level1.step(SECOND_DELAY));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        
        System.out.println("stop");
        
        //pass level1
        if (level1.pass==true) {
        	    transition.winScene();
        }
        
        if (level1.pass==false) {
        	    transition.loseScene();
        }
        
 
    }
	
	 // Create the game's "scene": what shapes will be in the game and their starting properties
    
    
	public static void main (String[] args) {
        
        launch(args);
    }

    
  

}
