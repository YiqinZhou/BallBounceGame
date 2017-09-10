import javafx.scene.image.ImageView;

public class Paddle {
    private ImageView image;
    private double Xord;
    private double Yord;
    private double width;
    private boolean catchby;
    
    public Paddle(ImageView image, double Xord, double Yord, double width) {
    	    this.image=image;
    	    this.Xord=Xord;
    	    this.Yord=Yord;
    	    this.width=width;
    	    image.setX(Xord);
    	    image.setY(Yord);
    	    image.setFitWidth(width);
    	    this.catchby=false;
    	    
    }
    
    public ImageView getImage() {
    	    return this.image;
    }
    
	public void setYord(double Yord) {
		image.setY(Yord);
		this.Yord=Yord;
	}
	
	public void setXord(double Xord) {
		image.setX(Xord);
		this.Xord=Xord;
	}
	
	public void setCatchby(boolean catchby) {
		this.catchby=catchby;
	}
	

	
	public double getYord() {
		return this.Yord;
	}
	
	public double getXord() {
		return this.Xord;
	}
	
	public boolean getCatchby() {
		return this.catchby;
	}
}
