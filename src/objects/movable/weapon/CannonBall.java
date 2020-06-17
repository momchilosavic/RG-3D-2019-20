package objects.movable.weapon;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import objects.Cannon;
import objects.Trail;
import objects.movable.MovableObject;
import timer.MyAnimationTimer;

public class CannonBall extends Weapon {
        private double radius;
        private Group root;
	public static class CannonBallDestination implements Destination {
		private double radius;
		
		public CannonBallDestination ( double radius ) {
			this.radius = radius;
		}
		
		@Override public boolean reached ( double x, double y, double z ) {
			return ( y - this.radius ) >= 0;
		}
	}
	
	private static Point3D getSpeed ( double ySpeed, double xAngle, double yAngle ) {
		Point3D speedVector = new Point3D ( 0, ySpeed, 0 );
		Rotate rotateX = new Rotate ( xAngle, Rotate.X_AXIS );
		Rotate rotateY = new Rotate ( yAngle, Rotate.Y_AXIS );
		speedVector = rotateX.transform ( speedVector );
		speedVector = rotateY.transform ( speedVector );
		return speedVector;
	}
	
	private static Affine getPosition ( double cannonHeight, double ventHeight, double xAngle, double yAngle ) {
		Affine identity = new Affine ( );
		
		identity.appendRotation ( yAngle, Point3D.ZERO, Rotate.Y_AXIS );
		identity.appendTranslation ( 0, -cannonHeight/2, 0 );
		identity.appendRotation ( xAngle, Point3D.ZERO, Rotate.X_AXIS );
		identity.appendTranslation ( 0, -ventHeight, 0 );
		
		return identity;
	}
	
	public CannonBall ( Group root, double radius, Color color, double cannonHeight, double ventHeight, double xAngle, double yAngle, double ySpeed, double gravity, MyAnimationTimer timer ) {
                super (
				root,
				CannonBall.getPosition ( cannonHeight, ventHeight, xAngle, yAngle ),
				CannonBall.getSpeed ( ySpeed, xAngle, yAngle ),
				new Point3D ( 0, gravity, 0 ),
				new CannonBallDestination ( radius ),
				timer
		);
                this.root = root;
		Sphere ball = new Sphere (radius );
		ball.setMaterial ( new PhongMaterial ( color ) );
		super.getChildren ( ).addAll ( ball );
                this.radius = radius;
	}

    @Override
    public void onDestinationReached() {
        Trail trail = new Trail(root, this.radius, this);
        trail.getTransforms().addAll(new Translate(position.getTx(), position.getTy() - this.radius*2, position.getTz()));
        super.onDestinationReached();
        root.getChildren().add(trail);
    }

    @Override
    public void onCollision() {
        //Trail trail = new Trail(root, this.radius, this);
        //trail.getTransforms().addAll(new Translate(position.getTx(), position.getTy() - this.radius*2, position.getTz()));
        super.onCollision();
        //root.getChildren().add(trail);
    }
    
    
        
        
}
