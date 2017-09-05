
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TransitionScenes{
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int SIZE = 400;
	
	public Scene loseScene() {
		// create one top level collection to organize the things in the scene
        Group root = new Group();
        // create a place to see the shapes
        Scene myScene = new Scene(root, SIZE,SIZE, BACKGROUND);
        
        
        Text text = new Text();
        text.setFont(Font.font(30));
        text.setText("YOU LOSE! ");
        text.setX(10);
        text.setY(10);
        
        root.getChildren().add(text);
        
        return myScene;
		
	}
	
	public Scene winScene() {
        Group root = new Group();
        // create a place to see the shapes
        Scene myScene = new Scene(root, SIZE,SIZE, BACKGROUND);
        
        
        Text text = new Text();
        text.setFont(Font.font(30));
        text.setText("YOU LOSE! ");
        text.setX(10);
        text.setY(10);
        
        root.getChildren().add(text);
        
        return myScene;
		
	}

}
