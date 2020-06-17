package objects.movable;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Translate;
import objects.movable.weapon.CannonBall;
import timer.MyAnimationTimer;

public class Boat extends MovableObject {
                public double angle;
    private final Group root;
	public static class BoatDestination implements Destination {
		private Point3D destination;
		private double delta;
		
		public BoatDestination ( Point3D destination, double delta ) {
			this.destination = destination;
			this.delta = delta;
		}
		
		@Override public boolean reached ( double x, double y, double z ) {
			double dx = Math.abs ( this.destination.getX ( ) - x );
			double dy = Math.abs ( this.destination.getY ( ) - y );
			double dz = Math.abs ( this.destination.getZ ( ) - z );
                        
			if ( (dx <= this.delta && dy <= this.delta && dz <= this.delta)) {
				return true;
			} else {
                            if(dy >= 0){
                                
                            }
				return false;
			}
		}
	}
	
	private static Affine getPosition ( double angle, double distance ) {
		Affine identity = new Affine (  );
		
		identity.appendRotation ( angle, new Point3D ( 0, 0, 0 ), new Point3D ( 0, 1, 0 ) );
		identity.appendTranslation ( 0, 0, distance );
		
		return identity;
	}
	
	private static Point3D getSpeed ( double angle, double distance, double speed ) {
		Affine position = Boat.getPosition ( angle, distance );
		
		return new Point3D (
				-position.getTx ( ),
				-position.getTy ( ),
				-position.getTz ( )
		).normalize ( ).multiply ( speed );
	}
	
	private static BoatDestination getBoatDestination ( double angle, double zDestination, double delta ) {
		Affine position = Boat.getPosition ( angle, zDestination );
		Point3D destination = new Point3D (
				position.getTx ( ),
				position.getTy ( ),
				position.getTz ( )
		);
		
		return new BoatDestination ( destination, delta );
	}
	
	@Override public void onDestinationReached ( ) { 
            //TODO: PROVERITI DA LI JE STIGAO DO OSTRVA ILI JE POTONUO
            if(this.speed.getY() > 0)
                super.onCollision();
        }
	
	public Boat ( Group parent, double width, double height, double depth, Color color, double distance, double angle, double speed, double destination, double delta ) {
		super (
				parent,
				Boat.getPosition ( angle, distance ),
				Boat.getSpeed ( angle, distance, speed ),
				new Point3D ( 0, 0, 0 ),
				Boat.getBoatDestination ( angle, destination + depth / 2, delta )
		);
                this.angle = angle;
                this.root = parent;
                
                Group boat = new Group();
                Box cabin = new Box(10./15*width, 7./15*height, 14./47*depth);
                cabin.getTransforms().add(new Translate(0, -height, 1.*cabin.getDepth()/2));
                cabin.setMaterial(new PhongMaterial(Color.DARKGRAY));
                
                Box body = new Box(width, 8./15*height, 35./47*depth);
                body.getTransforms().add(new Translate(0, -8./15*height, 0));
                body.setMaterial(new PhongMaterial(Color.GRAY));
                
                MeshView front = new MeshView();
                TriangleMesh frontMesh = new TriangleMesh();
                frontMesh.getPoints().addAll(
                        (float)width/2,    (float)0.,             (float)0.,
                        (float)0.,         (float)(-8./15*height),  (float)(-12./47*depth),
                        (float)-width/2,   (float)0.,             (float)0., // donja strana
                        (float)-width/2,   (float)(-8./15*height),  (float)0.,
                        (float)0.,          (float)(-8./15*height),  (float)(-12./47*depth),
                        (float)width/2,    (float)(-8./15*height),  (float)0., // gornja strana
                        (float)-width/2,   (float)0.,             (float)0.,
                        (float)0.,          (float)(-8./15*height),  (float)(-12./47*depth),
                        (float)-width/2,   (float)(-8./15*height),  (float)0.,  // leva strana
                        (float)width/2,    (float)0.,             (float)0.,
                        (float)0.,         (float)(-8./15*height),  (float)(-12./47*depth),
                        (float)width/2,    (float)(-8./15*height),  (float)0.,  // desna strana
                        (float)width/2,     (float)0.,              (float)0.,
                        (float)width/2,     (float)(-8./15*height),              (float)0.,
                        (float)-width/2,     (float)(-8./15*height),              (float)0.,
                        (float)-width/2,     (float)0.,              (float)0.  // zadnja strana
                        
                );
                frontMesh.getTexCoords().addAll(0,0,0,1,1,1,1,0);
                for(int i = 0; i < 4; i++){
                    frontMesh.getFaces().addAll(
                            i*3, 0, i*3+1, 1, i*3+2, 2,
                            i*3, 0, i*3+2, 2, i*3+1, 1
                    );
                }
                frontMesh.getFaces().addAll(
                        4*3, 0, 4*3+1, 1, 4*3+2, 2,
                        4*3, 0, 4*3+2, 2, 4*3+1, 1,
                        4*3+1, 1, 4*3+2,2, 4*3+3,3,
                        4*3+1, 1, 4*3+3,3, 4*3+2,2
                );
                front.setMesh(frontMesh);
                front.setMaterial(new PhongMaterial(Color.GRAY));
                front.getTransforms().add(new Translate(0, -4./15*height,-17.5/47*depth));
                
                boat.getChildren().addAll(cabin, body, front);
		super.getChildren ( ).addAll ( boat );
	}     

    @Override
    public void onCollision() {
        if(this.speed.getY() == 0){
            this.speed = new Point3D(0, 0.05, 0);
        }
        else
            super.onCollision();
        //TODO: PODESITI VREME TONJENJA
    }
    
    public boolean underSea(){
        if( this.position.getTy() - 15.0 >= 0 ){
            super.onDestinationReached();
            return true;
        }
        return false;
    }
    
    public BoatBall fire(MyAnimationTimer timer){
        double distance = Math.sqrt(Math.pow(this.position.getTz(), 2) + Math.pow(this.position.getTx(), 2));
        double speed = 5;
        double time = distance / speed;
        double alpha = 135;//Math.asin(distance / (speed*speed)) * 2;
        double yspeed = speed * Math.sin(alpha) - 0.01*distance/speed;
        
        double x = this.speed.getX();
        double y = this.speed.getY();
        double z = this.speed.getZ();
        
        double angle = z != 0 ? Math.abs(Math.atan(x/z)) * 180.0 / Math.PI : 90.0; 
        BoatBall ball = new BoatBall(
                this.root,
                Color.RED,
                15,
                this.speed.multiply(25 * distance / 500).add(0, -1 - Math.random()*.1, 0),
                yspeed* 30 * distance/500,
                0.01,
                timer);
        ball.getTransforms().addAll(this.position);
        this.root.getChildren().add(ball);
        return ball;
    }
        
}
