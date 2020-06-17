/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects.movable;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import objects.Cannon;
import timer.MyAnimationTimer;

/**
 *
 * @author PC
 */
public class BoatBall extends MovableObject{
    
    private static final double radius = 3;
    private final MyAnimationTimer timer;
    
    public static class BoatBallDestination implements Destination{
        private Point3D destination;
        private double delta;
        private double radius;
        
        public BoatBallDestination(double radius){
            this.radius = radius;
        }

        @Override
        public boolean reached(double x, double y, double z) {
            return y - radius >= 0;
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
	
	private static Affine getPosition ( double boatHeight, double xAngle, double yAngle ) {
		Affine identity = new Affine ( );
		
		identity.appendRotation ( yAngle, Point3D.ZERO, Rotate.Y_AXIS );
		identity.appendTranslation ( 0, -boatHeight/2, 0 );
		identity.appendRotation ( xAngle, Point3D.ZERO, Rotate.X_AXIS );
		identity.appendTranslation ( 0, -boatHeight, 0 );
		
		return identity;
	}
    
    
    
    public BoatBall(Group root, Color color, double boatHeight, Point3D speed, double ySpeed, double gravity, MyAnimationTimer timer) {
        super(
                root,
                BoatBall.getPosition(boatHeight, 0, 0),
                speed, 
                new Point3D(0, gravity, 0),// + (Math.random()*0.1 - 0.2)), 
                new BoatBallDestination(radius));
        this.timer = timer;
        
        Sphere ball = new Sphere (radius );
        ball.setMaterial ( new PhongMaterial ( color ) );
	super.getChildren ( ).addAll ( ball );
    }

    @Override
    public void onCollision() {
        super.onCollision(); //To change body of generated methods, choose Tools | Templates.
        timer.decreasCannonHealth();
    }

    public boolean handleCollision(Group other) {
        if ( this.getBoundsInParent().intersects ( ((Cannon)other).getTransformedBounds ( ) ) ) {
            this.onCollision ( );
            return true;
	} else {
            return false;
	} 
    }
}
