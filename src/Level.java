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

public class Level {

	public static final String BALL_IMAGE = "ball.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int KEY_INPUT_SPEED = 20;
	public static final double GROWTH_RATE = 1.1;
	public static final int BOUNCER_MIN_SIZE = 10;

	public int life;
	public int score;
	public Text scoreText;
	public static Timeline animation;
	public Brick[] bricks;
	public int[] brickConfig = { 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 3, 2, 2, 2, 2, 2, 3, 2, 1,1,1,1,1,1,1,1,1 };

	// some things we need to remember during our game
	private Scene myScene;
	private ImageView myBouncer1;
	private ImageView paddle;
	private Point2D myVelocity1;
	private Random dice = new Random();
	

	// pass back to the main
	public int pass = -1;

	Image Brick3Image = new Image(getClass().getClassLoader().getResourceAsStream("brick3.gif"));
	Image Brick2Image = new Image(getClass().getClassLoader().getResourceAsStream("brick4.gif"));
	Image Brick1Image = new Image(getClass().getClassLoader().getResourceAsStream("brick10.gif"));
	
	public Group root;

	public Level(Timeline animation) {
		this.animation = animation;

	}

	public Scene setupGame(int width, int height, Paint background) {
		// create one top level collection to organize the things in the scene
		root = new Group();
		// create a place to see the shapes
		myScene = new Scene(root, width, height, background);
		// make some shapes and set their properties
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		myBouncer1 = makeBouncer(image, width, height);

		// set up bricks

		bricks = setUpBricks(brickConfig);
		for (int i = 0; i < bricks.length; i++) {
			root.getChildren().add(bricks[i].image);
		}

		// set up paddle

		paddle = setUpPaddle();

		// order added to the group is the order in which they are drawn
		root.getChildren().add(myBouncer1);
		root.getChildren().add(paddle);

		// set up scoreboard

		score = 0;
		life = 3;

		scoreText = setUpScore(score, life);
		root.getChildren().add(scoreText);

		// respond to input
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	// Change properties of shapes to animate them
	// Note, there are more sophisticated ways to animate shapes, but these simple
	// ways work fine to start.
	public void step(double elapsedTime) {
		// update attributes

		if (myBouncer1.getX() < 0
				|| myBouncer1.getX() > myScene.getWidth() - myBouncer1.getBoundsInLocal().getWidth()) {
			myVelocity1 = new Point2D(-myVelocity1.getX(), myVelocity1.getY());
		}
		if (myBouncer1.getY() < 0) {
			myVelocity1 = new Point2D(myVelocity1.getX(), -myVelocity1.getY());
		}

		// ball touches the bottom
		if (myBouncer1.getY() > myScene.getHeight() - myBouncer1.getBoundsInLocal().getHeight()) {
			life = life - 1;
			if (life == 0) {
				animation.stop();
				pass = 0;

			}
			scoreText.setText("Score: " + score + "   Life: " + life);
			myBouncer1.setX(SIZE / 2);
			myBouncer1.setY(SIZE - 50 - 12);
			myVelocity1 = new Point2D(0, 0);
			paddle.setX(SIZE / 2 - 30);
			paddle.setY(SIZE - 50);

		}

		// ball hits the paddle
		if (myBouncer1.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
			myVelocity1 = new Point2D(myVelocity1.getX(), -myVelocity1.getY());

		}

		// ball hits the bricks

		int count = 0;

		for (int i = 0; i < bricks.length; i++) {
			if (bricks[i].image.isVisible() == false) {
				count = count + 1;
			}
			if (myBouncer1.getBoundsInParent().intersects(bricks[i].image.getBoundsInParent())
					&& bricks[i].image.isVisible() == true) {
				// hit by once and cleared, 10 points
				if (bricks[i].type==1) {
					bricks[i].image.setVisible(false);
					bricks[i].hitTimes=1;
					score = score + 10;
					scoreText.setText("Score: " + score + "   Life: " + life);

				}
				
				//hit three times to clear, each hit worths 10 points, final clearance 30 points
				if (bricks[i].type==2) {
					bricks[i].hitTimes+=1;
					if (bricks[i].hitTimes==3) {
						bricks[i].image.setVisible(false);
						score=score+30;
						scoreText.setText("Score: " + score + "   Life: " + life);
					}
					else {
						score=score+10;
						scoreText.setText("Score: " + score + "   Life: " + life);
					}
					
				}
				
				
				//drop Power-ups, each worths 20 points
				if (bricks[i].type==3) {
					bricks[i].image.setVisible(false);
					bricks[i].hitTimes=1;
					score = score + 20;
					scoreText.setText("Score: " + score + "   Life: " + life);
					
					PowerUp power=bricks[i].powerUp();
					power.image.setX(bricks[i].image.getX()+15);
					power.image.setY(bricks[i].image.getY()+20);
					root.getChildren().add(power.image);
					
					
				}

			}

		}

		// all bricks clear
		if (count == bricks.length) {
			animation.stop();
			pass = 1;

		}

		moveBouncer(myBouncer1, myVelocity1, elapsedTime);

	}

	// Create a bouncer from a given image
	private ImageView makeBouncer(Image image, int screenWidth, int screenHeight) {
		ImageView result = new ImageView(image);
		// make sure it stays a circle
		int size = BOUNCER_MIN_SIZE;
		result.setFitWidth(size);
		result.setFitHeight(size);
		// make sure it stays within the bounds
		result.setX(SIZE / 2);
		result.setY(SIZE - 50 - 12);
		myVelocity1 = new Point2D(0, 0);
		return result;
	}

	// Move a bouncer based on its velocity
	private void moveBouncer(ImageView bouncer, Point2D velocity, double elapsedTime) {
		bouncer.setX(bouncer.getX() + velocity.getX() * elapsedTime);
		bouncer.setY(bouncer.getY() + velocity.getY() * elapsedTime);
	}

	// What to do each time a key is pressed
	private void handleKeyInput(KeyCode code) {

		if (code == KeyCode.RIGHT && paddle.getX() <= myScene.getWidth() - paddle.getBoundsInLocal().getWidth()) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		}
		if (code == KeyCode.LEFT && paddle.getX() >= 0) {
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
		}
		if (code == KeyCode.S) {
			myVelocity1 = new Point2D(getRandomInRange(80, 100), getRandomInRange(-100, -80));
		}

	}

	// What to do each time a key is pressed
	private void handleMouseInput(double x, double y) {

	}

	private int getRandomInRange(int min, int max) {
		return min + dice.nextInt(max - min) + 1;
	}

	public Brick[] setUpBricks(int[] brickConfig) {

		Brick[] bricks = makeBrick(brickConfig);

		for (int i = 0; i < bricks.length; i++) {
			bricks[i].image.setFitHeight(20);
			bricks[i].image.setFitWidth(30);
			bricks[i].image.setX((i % 10) * 40);
			bricks[i].image.setY((i / 10) * 20 + 20);

		}

		return bricks;

	}

	public Brick[] makeBrick(int[] brickConfig) {

		bricks = new Brick[brickConfig.length];
		for (int i = 0; i < brickConfig.length; i++) {
			if (brickConfig[i] == 1) {
				bricks[i] = new Brick(1, new ImageView(Brick1Image));

			}

			if (brickConfig[i] == 2) {
				bricks[i] = new Brick(2, new ImageView(Brick2Image));

			}

			if (brickConfig[i] == 3) {
				bricks[i] = new Brick(3, new ImageView(Brick3Image));

			}

		}

		return bricks;

	}

	public ImageView setUpPaddle() {
		Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream("paddle.gif"));
		ImageView paddle = makePaddle(paddleImage);

		return paddle;
	}

	private ImageView makePaddle(Image image) {
		ImageView result = new ImageView(image);

		// make sure it stays within the bounds
		result.setX(SIZE / 2 - 30);
		result.setY(SIZE - 50);
		return result;

	}

	public Text setUpScore(int score, int life) {
		score = 0;
		life = 3;
		Text scoreText = new Text();
		scoreText.setFont(Font.font(10));
		scoreText.setText("Score: " + score + "   Life: " + life);
		scoreText.setX(10);
		scoreText.setY(10);
		return scoreText;
	}

}
