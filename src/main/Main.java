package main;


import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import objects.Cannon;
import objects.movable.Boat;
import objects.movable.MovableObject;
import timer.GameEventListener;
import timer.MyAnimationTimer;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import objects.Distance;
import objects.Info;
import objects.WeaponCounter;
import objects.movable.weapon.CannonBall;
import objects.movable.weapon.Weapon;
import subScenes.MiniMap;

public class Main extends Application implements GameEventListener {
    
        private static SubScene ScenePtr = null;
        private static Camera DefCamera = null;
        private static Camera BallCamera = null;
        private static Group root;
        private static List<MovableObject> movableObjects;
        private static SubScene TopView = null;
        
    public static class Constants {
        public static final String TITLE = "Island defense";
        
        public static final double CAMERA_NEAR_CLIP = 0.1;
	    public static final double CAMERA_FAR_CLIP  = 100000;
	    public static final double CAMERA_X_ANGLE   = -45;
	    public static final double CAMERA_Z         = -1000;
        
        public static final double SCENE_WIDTH  = 800;
        public static final double SCENE_HEIGHT = 800;
        
        public static final double OCEAN_WIDTH  = 10000;
        public static final double OCEAN_DEPTH  = 10000;
        public static final double OCEAN_HEIGHT = 2;
        
        public static final double ISLAND_RADIUS  = 100;
        public static final double ISLAND_HEIGHT = 6;
        
        public static final double DELTA = 0.1;
        
        public static final int    NUMBER_OF_BOATS = 4;
        public static final double BOAT_WIDTH      = 15;
        public static final double BOAT_HEIGHT     = 15;
        public static final double BOAT_DEPTH      = 47;
        public static final double BOAT_DISTANCE   = 500;
        public static final double BOAT_SPEED      = 0.1;
        
        public static final double CANNON_WIDTH       = 15;
        public static final double CANNON_HEIGHT      = 40;
        public static final double CANNON_DEPTH       = 30;
        public static final double CANNON_VENT_LENGTH = 50;
        public static final double Y_SPEED            = -2;
        
        public static final double GRAVITY = 0.01;
    }
    
	private MyAnimationTimer timer;
	
	public static void main ( String[] args ) {
		launch ( args );
	}
	
	@Override public void onGameWon ( ) { }
	
	@Override public void onGameLost ( ) { }
	
	@Override
	public void start ( Stage primaryStage ) throws Exception {
		movableObjects = new ArrayList<> ( );
		root           = new Group ( );
                SubScene               scene          = new SubScene ( root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED );
		List<Camera> cameras = new ArrayList<>();
                ScenePtr = scene;
                
                Group mainRoot = new Group();
                Scene mainScene = new Scene(mainRoot, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED);
                
		Box ocean = new Box ( Constants.OCEAN_WIDTH, Constants.OCEAN_HEIGHT, Constants.OCEAN_DEPTH );
		ocean.setMaterial ( new PhongMaterial ( Color.BLUE ) );
		root.getChildren ( ).addAll ( ocean );
                
                Distance distances = new Distance(Constants.OCEAN_HEIGHT, new PhongMaterial(Color.BLUE));
                distances.setDepthTest(DepthTest.DISABLE);
                root.getChildren().addAll(distances);
		
		Cylinder island = new Cylinder ( Constants.ISLAND_RADIUS, Constants.ISLAND_HEIGHT );
		island.setMaterial ( new PhongMaterial ( Color.BROWN ) );
		root.getChildren ( ).addAll ( island );
		
		Camera camera = new PerspectiveCamera ( true );
		camera.setNearClip ( Constants.CAMERA_NEAR_CLIP );
		camera.setFarClip ( Constants.CAMERA_FAR_CLIP );
		camera.getTransforms ( ).addAll (
				new Rotate ( Constants.CAMERA_X_ANGLE, Rotate.X_AXIS ),
				new Translate ( 0, 0, Constants.CAMERA_Z )
		);
		scene.setCamera ( camera );
                cameras.add(camera);
		
		InitBoats(1);
                
		this.timer = new MyAnimationTimer ( movableObjects, this, cameras );
                
                WeaponCounter weaponCounter = new WeaponCounter(timer.getWeaponCount());
                weaponCounter.setDepthTest(DepthTest.ENABLE);
                weaponCounter.getTransforms().add(new Translate(scene.getWidth(), 0));
                
                SubScene subSceneCounter = new SubScene(weaponCounter, scene.getWidth(), scene.getHeight() / 20, false, SceneAntialiasing.BALANCED);
                subSceneCounter.getTransforms().add(new Translate(0, scene.getHeight() - subSceneCounter.getHeight(),-500));
                        
		Cannon cannon = new Cannon (
				root,
                        weaponCounter,
				Constants.CANNON_WIDTH,
				Constants.CANNON_HEIGHT,
				Constants.CANNON_DEPTH,
				Constants.ISLAND_HEIGHT,
				Constants.CANNON_VENT_LENGTH,
				Color.GREEN,
				Constants.SCENE_WIDTH,
				Constants.SCENE_HEIGHT,
				Constants.Y_SPEED,
				Constants.GRAVITY,
				this.timer
		);
		root.getChildren ( ).addAll ( cannon);
                
		scene.addEventHandler ( MouseEvent.ANY, cannon );
                mainScene.addEventHandler(KeyEvent.KEY_PRESSED, e ->{
                    switch(e.getCode()){
                        case T:{
                            if(scene.getCamera().equals(cameras.get(1))){
                                if(mainRoot.getChildren().contains(TopView))
                                    mainRoot.getChildren().remove(TopView);
                                else 
                                    mainRoot.getChildren().add(TopView);
                            }
                            break;
                        }
                        case NUMPAD0: case DIGIT0:{
                            scene.setCamera(cameras.get(0));
                            if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            break;
                        }
                        case NUMPAD5: case DIGIT5:{
                            scene.setCamera(cameras.get(1));
                            if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            break;
                        }
                        case NUMPAD1: case DIGIT1:{
                            if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            scene.setCamera(cameras.get(2));
                            scene.getCamera().setId("0");
                            break;
                        }
                        case NUMPAD2: case DIGIT2:{
                            if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            scene.setCamera(cameras.get(2));
                            scene.getCamera().setId("1");
                            break;
                        }
                        case NUMPAD3: case DIGIT3:{
                            if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            scene.setCamera(cameras.get(2));
                            scene.getCamera().setId("2");
                            break;
                        }
                        case NUMPAD4: case DIGIT4:{
                            if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            scene.setCamera(cameras.get(2));
                            scene.getCamera().setId("3");
                            break;
                        }
                        case LEFT: case RIGHT: {
                            double delta = e.getCode() == KeyCode.LEFT ? -.5 : .5;
                            ((Translate)cameras.get(2).getUserData()).setX(((Translate)cameras.get(2).getUserData()).getX() + delta);
                            if(((Translate)cameras.get(2).getUserData()).getX() > 7.5) ((Translate)cameras.get(2).getUserData()).setX(7.5);
                            if(((Translate)cameras.get(2).getUserData()).getX() < -7.5) ((Translate)cameras.get(2).getUserData()).setX(-7.5);
                            break;
                        }
                        case UP: case DOWN: {
                            double delta = e.getCode() == KeyCode.UP ? -.5 : .5;
                            ((Translate)cameras.get(2).getUserData()).setY(((Translate)cameras.get(2).getUserData()).getY() + delta);
                            if(((Translate)cameras.get(2).getUserData()).getY() > 7.5) ((Translate)cameras.get(2).getUserData()).setY(7.5);
                            if(((Translate)cameras.get(2).getUserData()).getY() < -7.5) ((Translate)cameras.get(2).getUserData()).setY(-7.5);
                            break;
                        }
                        case PAGE_UP: case PAGE_DOWN: {
                            double delta = e.getCode() == KeyCode.PAGE_DOWN ? -.5 : .5;
                            ((Translate)cameras.get(2).getUserData()).setZ(((Translate)cameras.get(2).getUserData()).getZ() + delta);
                            if(((Translate)cameras.get(2).getUserData()).getZ() > 29.5) ((Translate)cameras.get(2).getUserData()).setZ(29.5);
                            if(((Translate)cameras.get(2).getUserData()).getZ() < -17.5) ((Translate)cameras.get(2).getUserData()).setZ(-17.5);
                            break;
                        }
                        case SPACE:{
                            if(scene.getCamera().equals(cameras.get(3))){
                                scene.setCamera(cameras.get(1));
                                break;
                            }
                            //if(mainRoot.getChildren().contains(TopView)) mainRoot.getChildren().remove(TopView);
                            root.getChildren().forEach(obj ->{
                                if(obj instanceof CannonBall){
                                    scene.setCamera(cameras.get(3));
                                }
                            });
                            break;
                        }
                    }
                });
		
		scene.setFill ( Color.LIGHTBLUE );
		scene.setCursor ( Cursor.NONE );
                
                SubScene infoScene = new SubScene(Info.GetInstance(), 150, 100, false, SceneAntialiasing.BALANCED);
                infoScene.setFill(Color.TRANSPARENT);
                        
                MiniMap miniMap = new MiniMap(150, 150, 10, 10, Color.LIGHTBLUE, Constants.BOAT_DISTANCE, movableObjects, island);
                miniMap.setCannon(cannon);
                SubScene topViewScene = new SubScene(miniMap, 150, 150);
                topViewScene.getTransforms().addAll(new Translate(0, scene.getHeight() - 150, 0));
                
                TopView = topViewScene;
                timer.setMiniMap(miniMap);
                
                scene.heightProperty().bind(mainScene.heightProperty());
                scene.widthProperty().bind(mainScene.widthProperty());
                mainScene.heightProperty().addListener((observable, oldValue, newValue) -> {
                    subSceneCounter.getTransforms().setAll(new Translate(0, newValue.doubleValue() - subSceneCounter.getHeight()));
                    topViewScene.getTransforms().setAll(new Translate(0, newValue.doubleValue() - topViewScene.getHeight()));
                });
                subSceneCounter.widthProperty().addListener((observable, oldValue, newValue) -> {
                    weaponCounter.getTransforms().setAll(new Translate(subSceneCounter.getWidth() - 20, 0));
                });
                subSceneCounter.widthProperty().bind(mainScene.widthProperty());
                
                        scene.setPickOnBounds(true);
                       
                        mainRoot.getChildren().addAll(scene, subSceneCounter, infoScene);
                        
                        primaryStage.setScene ( mainScene );
                        primaryStage.setTitle ( Constants.TITLE );
                        primaryStage.setFullScreen(true);
                        primaryStage.show ( );
                        
                        initCameras(cameras, cannon, movableObjects);
                        timer.setCannon(cannon);
                        
                        timer.start ( );
                    
                }
        
        private void initCameras(List<Camera> cameras, Cannon cannon, List<MovableObject> movableObjects){
                Camera cameraCannon = new PerspectiveCamera(true);
		cameraCannon.setNearClip ( Constants.CAMERA_NEAR_CLIP );
		cameraCannon.setFarClip ( Constants.CAMERA_FAR_CLIP );
                cameraCannon.getTransforms().setAll(cannon.getChildren().get(0).getTransforms());
                cameraCannon.getTransforms().addAll(new Translate(0,-40, -75), new Rotate(-5, Rotate.X_AXIS));
                
                cameraCannon.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                    cameraCannon.getTransforms().setAll(cannon.getChildren().get(0).getTransforms());
                    cameraCannon.getTransforms().addAll(new Translate(0,-40, -75), new Rotate(-5, Rotate.X_AXIS));
                });
                
                Camera cameraBoat = new PerspectiveCamera(true);
		cameraBoat.setNearClip ( Constants.CAMERA_NEAR_CLIP );
		cameraBoat.setFarClip ( Constants.CAMERA_FAR_CLIP );
                cameraBoat.setId("0");
                
                Translate translate = new Translate();
                cameraBoat.setUserData(translate);
                
                Camera cameraBall = new PerspectiveCamera(true);
		cameraBall.setNearClip ( Constants.CAMERA_NEAR_CLIP );
		cameraBall.setFarClip ( Constants.CAMERA_FAR_CLIP );
                
                cameras.add(cameraCannon);
                cameras.add(cameraBoat);
                cameras.add(cameraBall);
                
                DefCamera = cameraCannon;
                BallCamera = cameraBall;
        }
        
        public static void ToggleBallCamera(){
            if(ScenePtr.getCamera().equals(BallCamera))
                ScenePtr.setCamera(DefCamera);
        }
        
        public static void InitBoats(int wave){
            for ( int i = 0; i < Constants.NUMBER_OF_BOATS; ++i ) {
			double angle = 360 * Math.random();// / Constants.NUMBER_OF_BOATS * i;
			Boat boat = new Boat (
					root,
					Constants.BOAT_WIDTH,
					Constants.BOAT_HEIGHT,
					Constants.BOAT_DEPTH,
					Color.RED,
					Constants.BOAT_DISTANCE,
					angle,
					Constants.BOAT_SPEED * wave,
					Constants.ISLAND_RADIUS,
					Constants.DELTA
			);
			
			root.getChildren ( ).addAll ( boat );
			movableObjects.add ( boat );
		}
        }
}
