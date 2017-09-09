import javafx.scene.image.ImageView;

public class PowerUp {
	private int type;
	private ImageView image;
	private double Xord;
	private double Yord;
	
	public PowerUp(int type, ImageView image,double Xord, double Yord) {
		this.type=type;
		this.image=image;
		this.Xord=Xord;
		this.Yord=Yord;
		image.setX(Xord);
		image.setY(Yord);
	}
	


	public void createMotion() {
		 this.Yord=Yord+2;
		 image.setY(Yord);
		
	}
	
	public ImageView getImage() {
		return this.image;
	}
	
	public double getYord() {
		return this.Yord;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setYord(double Yord) {
		this.image.setY(Yord);
		this.Yord=Yord;
	}
	
	public void setXord(double Xord) {
		this.image.setX(Xord);
		this.Xord=Xord;
	}
	


}
