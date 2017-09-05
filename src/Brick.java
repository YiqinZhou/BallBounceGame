import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {
	
	
   public int type;
   public ImageView image;
   public int hitTimes;
  
  
   public ImageView[] bricks;
   Image Brick3Image = new Image(getClass().getClassLoader().getResourceAsStream("brick3.gif"));
   Image Brick2Image = new Image(getClass().getClassLoader().getResourceAsStream("brick4.gif"));
   Image Brick1Image = new Image(getClass().getClassLoader().getResourceAsStream("brick10.gif"));
   
   Image Power0Image = new Image(getClass().getClassLoader().getResourceAsStream("sizepower.gif"));
   Image Power1Image = new Image(getClass().getClassLoader().getResourceAsStream("pointspower.gif"));
   Image Power2Image = new Image(getClass().getClassLoader().getResourceAsStream("laserpower.gif"));
   
   
   public Brick(int type, ImageView image) {
	   this.type=type;
	   this.image=image;
	   this.hitTimes=0;
   }
   
   public PowerUp powerUp() {
	   Random dice=new Random();
	   int type=dice.nextInt(2-0);
	   
	   if (type==0) {
		   PowerUp power=new PowerUp(type,new ImageView(Power0Image));
		   return power;
	   }
	   
	   if (type==1) {
		   PowerUp power=new PowerUp(type,new ImageView(Power1Image));
		   return power;
	   }
	   
	   if (type==2) {
		   PowerUp power=new PowerUp(type,new ImageView(Power2Image));
		   return power;
	   }
	   
	   
	  return new PowerUp(type,new ImageView(Power0Image));
	   
   }







}
