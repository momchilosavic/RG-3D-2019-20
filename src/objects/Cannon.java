package objects;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import objects.movable.weapon.CannonBall;
import timer.MyAnimationTimer;
import main.*;

public class Cannon extends Group implements EventHandler<MouseEvent> {
	private double           sceneWidth;
	private double           sceneHeight;
	public  Rotate           rotateX;
	public  Rotate           rotateY;
	private MyAnimationTimer timer;
	private double           width;
	private Color            cannonColor;
	private double           height;
	private double           ySpeed;
	private double           gravity;
	private Group            root;
	private double           ventHeight;
        
        private WeaponCounter weaponCounter;
	private int maxHealth;
        private int health;
        public int getMaxHealth(){return this.maxHealth;}
        public void setMaxHealth(int maxHealth){this.maxHealth = maxHealth;}
        public int getHealth(){return this.health;}
        public void setHealth(int health){this.health = health;}
        
	public Cannon ( Group root, WeaponCounter weaponCounter, double width, double height, double depth, double islandHeight, double ventHeight, Color cannonColor, double sceneWidth, double sceneHeight, double ySpeed, double gravity, MyAnimationTimer timer ) {
		this.root = root;
		this.weaponCounter = weaponCounter;
                
		Group cannon = new Group ( );
		super.getChildren ( ).addAll ( cannon );
		
                maxHealth = 100;
                health = 100;
                
                MeshView podium = new MeshView();
                TriangleMesh podiumMesh = new TriangleMesh();
                
                float widthF = (float)width;
                float heightF = (float)height;
                float depthF = (float)depth;
                podiumMesh.getPoints().addAll(
                        -widthF/2,  heightF/2, -depthF/3, 
                        -widthF/2,  -heightF/2,  -depthF/3,
                        widthF/2,   -heightF/2,  -depthF/3,
                        widthF/2,   heightF/2, -depthF/3, // zadnja strana
                        -widthF/2,  -heightF/2,  -depthF/3,
                        -widthF/2,  -heightF/2,  0,
                        widthF/2,   -heightF/2,  0,
                        widthF/2,   -heightF/2,  -depthF/3, // vrh
                        -widthF/2,  -heightF/2, 0,
                        -widthF/2,  heightF/4, depthF*2/3,
                        widthF/2,   heightF/4, depthF*2/3,
                        widthF/2,   -heightF/2, 0,    // prednja gornja strana
                        -widthF/2,  heightF/4, depthF*2/3,
                        -widthF/2,  heightF/2, depthF/3,
                        widthF/2,   heightF/2, depthF/3,
                        widthF/2,   heightF/4, depthF*2/3,   // prednja donja strana
                        -widthF/2,  heightF/2, -depthF/3,
                        -widthF/2,  heightF/2, depthF/3,
                        widthF/2,   heightF/2, depthF/3,
                        widthF/2,   heightF/2, -depthF/3,  // donja strana
                        -widthF/2,  heightF/2, -depthF/3,
                        -widthF/2,  -heightF/2,  -depthF/3,
                        -widthF/2,  -heightF/2,  0f,
                        -widthF/2,  heightF/4, depthF*2/3,
                        -widthF/2,  heightF/2, depthF/3, // leva strana
                        widthF/2,  heightF/2, -depthF/3,
                        widthF/2,  -heightF/2,  -depthF/3,
                        widthF/2,  -heightF/2,  0f,
                        widthF/2,  heightF/4, depthF*2/3,
                        widthF/2,  heightF/2, depthF/3 // desna strana
                );
                podiumMesh.getTexCoords().addAll(0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f);
                for(int i = 0; i < 5; i++){
                    podiumMesh.getFaces().addAll(
                            i*4, 0, i*4 + 1, 1, i*4 + 3, 3,
                            i*4, 0, i*4 + 3, 3, i*4 + 1, 1,
                            i*4 + 1, 1, i*4 + 2, 2, i*4 + 3, 3,
                            i*4 + 1, 1, i*4 + 3, 3, i*4 + 2, 2
                    );
                }
                for(int i = 0; i < 2; i++){
                    podiumMesh.getFaces().addAll(
                            5*(4+i), 0, 5*(4+i) + 1, 1, 5*(4+i) + 2, 2,
                            5*(4+i), 0, 5*(4+i) + 2, 2, 5*(4+i) + 1, 1,
                            5*(4+i) + 2, 2, 5*(4+i) + 3, 0, 5*(4+i) + 4, 1,
                            5*(4+i) + 2, 2, 5*(4+i) + 4, 1, 5*(4+i) + 3, 0,
                            5*(4+i) + 4, 1, 5*(4+i), 0, 5*(4+i) + 2, 2,
                            5*(4+i) + 4, 1, 5*(4+i) + 2, 2, 5*(4+i), 0
                    );
                }
                podium.setMesh(podiumMesh);
		podium.setMaterial ( new PhongMaterial ( cannonColor ) );
		cannon.getChildren ( ).addAll ( podium );
		
                Group vent = new Group();
		Cylinder ventMain = new Cylinder ( width / 4, ventHeight );
		ventMain.setMaterial ( new PhongMaterial ( cannonColor ) );
                Cylinder ventFront = new Cylinder( width * 2 / 6, ventHeight / 8);
                ventFront.setMaterial(new PhongMaterial(cannonColor));
                ventFront.getTransforms().add(new Translate(0, -ventHeight/2, 0));
                vent.getChildren().addAll(ventMain, ventFront);
		
		this.rotateX = new Rotate ( );
		this.rotateY = new Rotate ( );
		this.rotateX.setAxis ( Rotate.X_AXIS );
		this.rotateY.setAxis ( Rotate.Y_AXIS );
		
		cannon.getTransforms ( ).addAll (
				new Translate ( 0, -( height + islandHeight ) / 2, 0 ),
				this.rotateY
		);
		vent.getTransforms ( ).addAll (
				//new Translate ( 0, 0, -depthF/6 ),
				this.rotateX,
				new Translate ( 0, -ventHeight *7/8 / 2, 0 )
		);
		cannon.getChildren ( ).addAll ( vent );
		
		this.sceneHeight = sceneHeight;
		this.sceneWidth = sceneWidth;
		this.timer = timer;
		this.width = width;
		this.cannonColor = cannonColor;
		this.height = height;
		this.ySpeed = ySpeed;
		this.gravity = gravity;
		this.ventHeight = ventHeight;
	}
	
	@Override public void handle ( MouseEvent event ) {
		if ( MouseEvent.MOUSE_MOVED.equals ( event.getEventType ( ) ) ) {
			double xRatio = event.getSceneX ( ) * 2 / this.sceneWidth;
			double yRatio = event.getSceneY ( ) / this.sceneHeight;
			
			this.rotateX.setAngle ( -120 * yRatio );
			this.rotateY.setAngle ( 360 * xRatio );
                        
                        
		} else if ( MouseEvent.MOUSE_PRESSED.equals ( event.getEventType ( ) ) && this.timer.canAddWeapon ( ) ) {
			CannonBall cannonBall = new CannonBall (
					root,
					this.width / 2,
					this.cannonColor,
					this.height,
					this.ventHeight,
					this.rotateX.getAngle ( ),
					this.rotateY.getAngle ( ),
					this.ySpeed,
					this.gravity,
					timer
			);
			root.getChildren ( ).addAll ( cannonBall );
                        this.timer.decreasWeaponCount();
                        this.weaponCounter.set(timer.getWeaponCount());
		}
	}
        
        public Bounds getTransformedBounds ( ) {
		return super.getLayoutBounds ( );
	}
}
