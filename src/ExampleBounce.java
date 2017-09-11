import javafx.animation.KeyFrame;
import javafx.geometry.Point2D;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class ExampleBounce extends Application {

	// Set up properties
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.HONEYDEW;
	public static final String TITLE = "BreakOut";
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	public Timeline animation;
	public Level level1;

	public ImageView[] bricks;

	// Manage different scenes
	static Stage stage;
	Stage dialog;

	Scene scene1, scene2, scene3;
	Button button1, button2, button3, start;
	Level current;
	Popup popup;

	private static final String WORDS = "Welcome to Breakout Game! Clear all the bricks using a ball. You only have three lives each level. Good luck!"
			+ "Press S to start the game. Press Left or Right to move the paddle accordingly. Press 'start' when you are ready!";

	@Override
	public void start(Stage primaryStage) {
		animation = new Timeline();

		stage = primaryStage;

		level1 = new Level(animation, stage, 1);
		// attach scene to the stage and display it
		Scene scene1 = level1.setupGame(SIZE, SIZE, BACKGROUND);
		// Scene scene1=createNewScene(1,level1);

		stage.setScene(scene1);
		stage.setTitle(TITLE);
		stage.show();
		primaryPopUp();

		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> level1.step(SECOND_DELAY));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();

	}

	public void createNewScene(int levelNumber) {
		animation = new Timeline();

		level1 = new Level(animation, stage, levelNumber);
		// attach scene to the stage and display it
		Scene scene1 = level1.setupGame(SIZE, SIZE, BACKGROUND);
		// Scene scene1=createNewScene(1,level1);

		stage.setScene(scene1);
		stage.setTitle(TITLE);
		stage.show();

		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> level1.step(SECOND_DELAY));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();

	}

	public void ButtonClicked(ActionEvent e) {
		if (e.getSource() == button1) {
			createNewScene(1);
			popup.hide();

		}
		if (e.getSource() == button2) {
			createNewScene(2);
			popup.hide();
		}

		if (e.getSource() == button3) {
			createNewScene(3);
			popup.hide();
		}

		if (e.getSource() == start) {
			dialog.hide();
		}

	}

	public void showPopUp(int type) {
		popup = new Popup();
		popup.setX(500);
		popup.setY(500);

		if (type == 1) {
			button1 = new Button("Level 1");
			button1.setOnAction(e -> ButtonClicked(e));
			popup.getContent().add(button1);
		}

		if (type == 2) {
			button2 = new Button("Level 2");
			button2.setOnAction(e -> ButtonClicked(e));
			popup.getContent().add(button2);
		}

		if (type == 3) {
			button3 = new Button("Level 3");
			button3.setOnAction(e -> ButtonClicked(e));
			popup.getContent().add(button3);
		}

		popup.show(stage);

	}

	public void primaryPopUp() {

		Label label = new Label(WORDS);
		label.setWrapText(true);
		start = new Button("Start!");
		start.setOnAction(e -> ButtonClicked(e));

		dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(label);
		dialogVbox.getChildren().add(start);
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	public void loseScene(int level) {
		// create one top level collection to organize the things in the scene
		Group root = new Group();
		// create a place to see the shapes
		Scene myScene = new Scene(root, SIZE, SIZE, BACKGROUND);

		Text text = new Text();
		text.setFont(Font.font(30));
		text.setText("YOU LOSE! TRY AGAIN! ");
		text.setX(30);
		text.setY(100);

		root.getChildren().add(text);

		stage.setScene(myScene);

		showPopUp(level);

	}

	public void winScene(int level) {
		// create one top level collection to organize the things in the scene
		Group root = new Group();
		// create a place to see the shapes
		Scene myScene = new Scene(root, SIZE, SIZE, BACKGROUND);

		if (level != 3) {
			Text text = new Text();
			text.setFont(Font.font(20));
			text.setText("YOU WIN! MOVE TO NEXT LEVEL! ");
			text.setX(30);
			text.setY(100);

			root.getChildren().add(text);

			stage.setScene(myScene);

			showPopUp(level + 1);
		}

		else {
			Text text = new Text();
			text.setFont(Font.font(20));
			text.setText("CONGRATULATIONS! YOU FINISHED! ");
			text.setX(30);
			text.setY(100);
			root.getChildren().add(text);

			stage.setScene(myScene);

		}

	}

	// Create the game's "scene": what shapes will be in the game and their starting
	// properties

	public static void main(String[] args) {

		launch(args);

	}

}
