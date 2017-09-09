import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {
	
	
   private int type;
   private ImageView image;
   private boolean previousHit;
   private double opacity;
  
  
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
	   this.previousHit=false;
	   this.opacity=1.0;
   }
   
   public int getType() {
	   return this.type;
   }
   
   public ImageView getImage() {
	   return this.image;
   }
   
   public boolean getPreviousHit() {
	   return this.previousHit;
   }
   
   public double getOpacity() {
	   return this.opacity;
   }
   
   public void setPreviousHit(boolean ok) {
	   this.previousHit=ok;
   }
   
   public void setOpacity(double opacity) {
	   this.opacity=opacity;
	   this.image.setOpacity(opacity);
   }
 
   
   public PowerUp powerUp(double Xord, double Yord) {
	   Random dice=new Random();
	   int type=dice.nextInt(2-0);
	     
	   if (type==0) {
		   PowerUp power=new PowerUp(type,new ImageView(Power0Image), Xord, Yord);
		   return power;
	   }
	   
	   
	   if (type==1) {
		   PowerUp power=new PowerUp(type,new ImageView(Power1Image), Xord, Yord);
		   return power;
	   }
	   
	   if (type==2) {
		   PowerUp power=new PowerUp(type,new ImageView(Power2Image), Xord, Yord);
		   return power;
	   }
	   
	   
	   
	  return new PowerUp(type,new ImageView(Power0Image),Xord, Yord);
	   
   }







}
