import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick {
  
   public ImageView[] bricks;
   Image Brick3Image = new Image(getClass().getClassLoader().getResourceAsStream("brick3.gif"));
   Image Brick2Image = new Image(getClass().getClassLoader().getResourceAsStream("brick4.gif"));
   Image Brick1Image = new Image(getClass().getClassLoader().getResourceAsStream("brick10.gif"));
   
   public ImageView[] makeBrick(int[] brickConfig) {
       

	   bricks=new ImageView[brickConfig.length];
	   for (int i=0;i<brickConfig.length;i++) {
		   if (brickConfig[i]==1) {
			   bricks[i]=new ImageView(Brick1Image);
			  

			   
		   }
		   
		   if (brickConfig[i]==2) {
			   bricks[i]=new ImageView(Brick2Image);

		   }
		   
		   if (brickConfig[i]==3) {
			   bricks[i]=new ImageView(Brick3Image);

			   
		   }
		   
	   }
	   
	   return bricks;
	   
   }


}
