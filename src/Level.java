import javafx.animation.KeyFrame;
import javafx.geometry.Point2D;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import java.util.ArrayList;
import java.util.Random;

public class Level {

	private static final int paddleApartFromBottom = 50;
	public static final String BALL_IMAGE = "ball.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.WHITE;
	public int KEY_INPUT_SPEED = 20;
	public static final double GROWTH_RATE = 1.1;
	public static final double BALL_SIZE = 10;
	public static final double BIG_BALL_SIZE = 20;

	public Image ball_image;
	public int life;
	public int score;
	public Text scoreText;
	public ArrayList<PowerUp> currentPowerUp = new ArrayList<PowerUp>();

	public Brick[] bricks;
	public int[] brickConfig = { 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 3, 2, 2, 2, 2, 2, 3, 2, 1, 1, 3, 3, 1, 1, 1, 1,
			1 };

	// some things we need to remember during our game
	private Scene myScene;
	private ImageView myBouncer1;
	private Paddle paddle;
	private Point2D myVelocity1;

	// for release extra paddle function
	private Point2D rememberVelocity;

	private Random dice = new Random();

	// to control the game variable
	private boolean gameStart = false;

	// for speeding up extra paddle function
	private String previousCode = "nothing";
	private double leftVelocity;
	private double rightVelocity;

	// pass back to the main
	public int pass = -1;

	Image Brick3Image = new Image(getClass().getClassLoader().getResourceAsStream("brick3.gif"));
	Image Brick2Image = new Image(getClass().getClassLoader().getResourceAsStream("brick4.gif"));
	Image Brick1Image = new Image(getClass().getClassLoader().getResourceAsStream("brick10.gif"));

	// constructor of level
	public Group root;
	private Timeline animation;
	private static Stage stage;
	private int levelNumber;

	public Level(Timeline animation, Stage stage, int levelNumber) {
		this.animation = animation;
		this.stage = stage;
		this.levelNumber = levelNumber;

	}

	public Scene setupGame(int width, int height, Paint background) {
		// create one top level collection to organize the things in the scene
		root = new Group();
		// create a place to see the shapes
		myScene = new Scene(root, width, height, background);

		// set up bricks

		bricks = setUpBricks(brickConfig);
		for (int i = 0; i < bricks.length; i++) {
			root.getChildren().add(bricks[i].getImage());
		}

		// set up paddle

		paddle = setUpPaddle(40);

		// Set up ball
		ball_image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		myBouncer1 = makeBouncer(ball_image, BALL_SIZE);
		myBouncer1.setX(SIZE / 2 - BALL_SIZE / 2);
		myBouncer1.setY(SIZE - paddleApartFromBottom - BALL_SIZE);
		myVelocity1 = new Point2D(0, 0);

		// order added to the group is the order in which they are drawn
		root.getChildren().add(myBouncer1);
		root.getChildren().add(paddle.getImage());

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

		moveBouncer(myBouncer1, myVelocity1, elapsedTime);

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
			paddle.setXord(SIZE / 2 - paddle.getImage().getFitWidth() / 2);
			paddle.setYord(SIZE - paddleApartFromBottom);
			myBouncer1.setX(SIZE / 2 - myBouncer1.getFitWidth() / 2);
			myBouncer1.setY(SIZE - paddleApartFromBottom - myBouncer1.getFitHeight());
			gameStart = false;
			myVelocity1 = new Point2D(0, 0);

		}

		if (!myBouncer1.getBoundsInParent().intersects(paddle.getImage().getBoundsInParent())) {
			paddle.setCatchby(false);
			rememberVelocity = new Point2D(myVelocity1.getX(), myVelocity1.getY());
		}

		// ball hits the paddle
		if (myBouncer1.getBoundsInParent().intersects(paddle.getImage().getBoundsInParent())) {

			if (score < 200 || score >= 300) {
				myVelocity1 = new Point2D(myVelocity1.getX(), -myVelocity1.getY());
			}

			// extra paddle power: hold the ball and release afterwards
			if (score >= 200 && score < 300 && paddle.getCatchby() == false) {

				myVelocity1 = new Point2D(0, 0);
			}
			if (score >= 200 && score < 300 && paddle.getCatchby() == true) {
				myVelocity1 = new Point2D(myVelocity1.getX(), -myVelocity1.getY());
			}

		}

		// ball hits the bricks

		int count = 0;

		for (int i = 0; i < bricks.length; i++) {
			if (bricks[i].getImage().isVisible() == false) {
				count = count + 1;
			}
			if (myBouncer1.getBoundsInParent().intersects(bricks[i].getImage().getBoundsInParent())
					&& bricks[i].getImage().isVisible() == true) {
				// hit by once and cleared, 10 points

				if (bricks[i].getType() == 1) {
					bricks[i].getImage().setVisible(false);

					score = score + 10;
					scoreText.setText("Score: " + score + "   Life: " + life);

				}

				// hit three times to clear, each hit worths 10 points, final clearance 30
				// points
				// hit once become
				if (bricks[i].getType() == 2) {
					// brick is never hit
					if (bricks[i].getOpacity() == 1.0 && bricks[i].getPreviousHit() == false) {
						score = score + 10;
						scoreText.setText("Score: " + score + "   Life: " + life);
						bricks[i].setOpacity(0.6);
					}

					// brick is hit once
					else if (bricks[i].getOpacity() == 0.6 && bricks[i].getPreviousHit() == false) {
						score = score + 10;
						scoreText.setText("Score: " + score + "   Life: " + life);
						bricks[i].setOpacity(0.3);
					}

					// brick is hit twice
					else if (bricks[i].getOpacity() == 0.3 && bricks[i].getPreviousHit() == false) {
						bricks[i].getImage().setVisible(false);
						score = score + 30;
						scoreText.setText("Score: " + score + "   Life: " + life);
					}

				}

				// drop Power-ups, each worths 20 points
				if (bricks[i].getType() == 3) {
					bricks[i].getImage().setVisible(false);

					score = score + 20;
					scoreText.setText("Score: " + score + "   Life: " + life);

					double Xord = bricks[i].getImage().getX() + 15;
					double Yord = bricks[i].getImage().getY() + 20;
					PowerUp power = bricks[i].powerUp(Xord, Yord);
					currentPowerUp.add(power);
					root.getChildren().add(power.getImage());

				}

				bricks[i].setPreviousHit(true);

			}

			else {
				bricks[i].setPreviousHit(false);
			}

		}

		// Check every existing PowerUp
		for (int i = 0; i < currentPowerUp.size(); i++) {

			// update PowerUp position
			PowerUp current = currentPowerUp.get(i);
			current.createMotion();

			// if reach the bottom
			if (current.getYord() > myScene.getHeight() - current.getImage().getBoundsInLocal().getHeight()) {
				root.getChildren().remove(current.getImage());
				currentPowerUp.remove(current);
			}

			// Check collision, get function working
			if (current.getImage().getBoundsInParent().intersects(paddle.getImage().getBoundsInParent())) {

				root.getChildren().remove(current.getImage());
				currentPowerUp.remove(current);

				// make paddle longer
				if (current.getType() == 0) {
					root.getChildren().remove(paddle.getImage());
					paddle = setUpPaddle(80);
					root.getChildren().add(paddle.getImage());
				}

				// make ball move faster
				if (current.getType() == 1) {
					myVelocity1 = new Point2D(myVelocity1.getX() * 1.5, myVelocity1.getY() * 1.5);
				}

				// make ball bigger
				if (current.getType() == 2) {
					double Xord = myBouncer1.getX();
					double Yord = myBouncer1.getY();
					root.getChildren().remove(myBouncer1);
					myBouncer1 = makeBouncer(ball_image, BIG_BALL_SIZE);
					myBouncer1.setX(Xord);
					myBouncer1.setY(Yord);
					root.getChildren().add(myBouncer1);

				}
			}
		}

		// all bricks clear
		if (count == bricks.length) {
			animation.stop();
			pass = 1;

		}

	}

	// Create a bouncer from a given image
	private ImageView makeBouncer(Image image, double size) {
		ImageView result = new ImageView(image);
		// make sure it stays a circle
		result.setFitWidth(size);
		result.setFitHeight(size);

		return result;
	}

	// Move a bouncer based on its velocity
	private void moveBouncer(ImageView bouncer, Point2D velocity, double elapsedTime) {
		bouncer.setX(bouncer.getX() + velocity.getX() * elapsedTime);
		bouncer.setY(bouncer.getY() + velocity.getY() * elapsedTime);
	}

	// What to do each time a key is pressed
	private void handleKeyInput(KeyCode code) {

		// start the game
		if (code == KeyCode.S && gameStart == false) {
			myVelocity1 = new Point2D(getRandomInRange(100, 120), getRandomInRange(-120, -100));
			gameStart = true;

		}

		// Paddle extra function: when score reaches 300, paddle can be moved up and
		// down, BUT CANNOT BE WHEN GAME HASN'T STARTED
		if (score >= 300 && gameStart == true) {
			if (code == KeyCode.UP && paddle.getYord() >= 0) {
				paddle.setYord(paddle.getYord() - KEY_INPUT_SPEED);

			}
			if (code == KeyCode.DOWN
					&& paddle.getYord() <= myScene.getHeight() - paddle.getImage().getBoundsInLocal().getHeight()) {
				paddle.setYord(paddle.getYord() + KEY_INPUT_SPEED);

			}
			if (code == KeyCode.RIGHT
					&& paddle.getXord() <= myScene.getWidth() - paddle.getImage().getBoundsInLocal().getWidth()) {
				paddle.setXord(paddle.getXord() + KEY_INPUT_SPEED);
			}
			if (code == KeyCode.LEFT && paddle.getXord() >= 0) {
				paddle.setXord(paddle.getXord() - KEY_INPUT_SPEED);
			}
		}

		// paddle extra function: when score between 200-300, paddle can catch the ball
		// and
		// release it only after R is pressed
		if (score >= 200 && score < 300 && gameStart == true) {
			if (myBouncer1.getBoundsInParent().intersects(paddle.getImage().getBoundsInParent())) {
				if (code == KeyCode.R) {
					myVelocity1 = new Point2D(rememberVelocity.getX(), -rememberVelocity.getY());

					paddle.setCatchby(true);
				}
				if (code == KeyCode.RIGHT
						&& paddle.getXord() <= myScene.getWidth() - paddle.getImage().getBoundsInLocal().getWidth()) {
					paddle.setXord(paddle.getXord() + KEY_INPUT_SPEED);
					myBouncer1.setX(myBouncer1.getX() + KEY_INPUT_SPEED);

				}
				if (code == KeyCode.LEFT && paddle.getXord() >= 0) {

					paddle.setXord(paddle.getXord() - KEY_INPUT_SPEED);
					myBouncer1.setX(myBouncer1.getX() - KEY_INPUT_SPEED);

				}
			} else {
				if (code == KeyCode.RIGHT
						&& paddle.getXord() <= myScene.getWidth() - paddle.getImage().getBoundsInLocal().getWidth()) {
					paddle.setXord(paddle.getXord() + KEY_INPUT_SPEED);

				}
				if (code == KeyCode.LEFT && paddle.getXord() >= 0) {
					paddle.setXord(paddle.getXord() - KEY_INPUT_SPEED);

				}

			}
		}

		// paddle extra function: speeding up in one direction

		if (score >= 100 && score < 200 && gameStart == true) {
			if (code == KeyCode.LEFT && paddle.getXord() >= 0) {
				if (previousCode == "left") {
					leftVelocity += 2;
					paddle.setXord(paddle.getXord() - leftVelocity);

				} else {
					leftVelocity = KEY_INPUT_SPEED;
					paddle.setXord(paddle.getXord() - leftVelocity);
					previousCode = "left";
				}
			}

			if (code == KeyCode.RIGHT
					&& paddle.getXord() <= myScene.getWidth() - paddle.getImage().getBoundsInLocal().getWidth()) {
				if (previousCode == "right") {
					rightVelocity += 2;
					paddle.setXord(paddle.getXord() + rightVelocity);

				} else {
					rightVelocity = KEY_INPUT_SPEED;
					paddle.setXord(paddle.getXord() + rightVelocity);
					previousCode = "right";
				}
			}
		}

		if (score < 100) {

			if (code == KeyCode.RIGHT
					&& paddle.getXord() <= myScene.getWidth() - paddle.getImage().getBoundsInLocal().getWidth()) {
				paddle.setXord(paddle.getXord() + KEY_INPUT_SPEED);

			}
			if (code == KeyCode.LEFT && paddle.getXord() >= 0) {
				paddle.setXord(paddle.getXord() - KEY_INPUT_SPEED);

			}

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
			bricks[i].getImage().setFitHeight(20);
			bricks[i].getImage().setFitWidth(30);
			bricks[i].getImage().setX((i % 10) * 40);
			bricks[i].getImage().setY((i / 10) * 20 + 20);

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

	public Paddle setUpPaddle(double size) {
		Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream("paddle.gif"));
		ImageView paddleImageView = new ImageView(paddleImage);
		Paddle paddle = new Paddle(paddleImageView, SIZE / 2 - size / 2, SIZE - paddleApartFromBottom, size);

		return paddle;
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
