package timer;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import objects.movable.MovableObject;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import objects.Cannon;
import objects.Info;
import objects.movable.Boat;
import objects.movable.BoatBall;
import objects.movable.weapon.CannonBall;
import subScenes.MiniMap;

public class MyAnimationTimer extends AnimationTimer {
	private List<MovableObject> movableObjects;
	private MovableObject       weapon;
	private GameEventListener listener;
	private boolean gameOver;
        private int weaponCount = 20;
        private List<Camera> cameras;
        private List<MovableObject> boatWeapon; //////
        private Cannon cannon;
        private static int WaveCount = 1;
    private MiniMap miniMap;
	
	public MyAnimationTimer ( List<MovableObject> movableObjects, GameEventListener listener, List<Camera> cameras ) {
		this.movableObjects = movableObjects;
		this.listener = listener;
                this.cameras = cameras;
                this.boatWeapon = new ArrayList<MovableObject>();
	}
	
        double x = 0; double y = 0; double z = 0;
        int landedCnt = 0;
	@Override public synchronized void handle ( long now ) {
                landedCnt = movableObjects.size();
		boolean islandHit = this.movableObjects.removeIf ( object -> object.update ( now  ));
                this.movableObjects.removeIf(object -> object instanceof Boat && ((Boat)object).underSea());
                
                int id = Integer.parseInt(this.cameras.get(2).getId());
                if(this.movableObjects.size() > 0 && (id < this.movableObjects.size() && this.movableObjects.get(id) instanceof Boat)){
                    this.cameras.get(2).getTransforms().setAll(this.movableObjects.get(Integer.parseInt(this.cameras.get(2).getId())).getTransforms());	
                    this.cameras.get(2).getTransforms().addAll(new Translate(0, -15, 0), new Rotate(180, Rotate.X_AXIS), new Rotate(180, Rotate.Z_AXIS));
                }   this.cameras.get(2).getTransforms().addAll(((Translate)this.cameras.get(2).getUserData()));
                
                if(this.weapon != null){
                    x = this.weapon.speed.getX();
                    y = this.weapon.speed.getY();
                    z = this.weapon.speed.getZ();
                    Affine pos = weapon.position;
                    this.cameras.get(3).getTransforms().setAll(new Translate(pos.getTx(), pos.getTy(), pos.getTz()));
                    
                    double rY = z > 0.001 || z < -0.001 ? Math.atan(Math.abs(x)/Math.abs(z)) * 180.0 / Math.PI : 90.0;
                    if(x > 0.001){
                        if(z < -0.001) rY = 180.0-rY;
                    }
                    else{
                        if(x < -0.001){
                            if(z >= 0.001) rY = -rY;
                            else rY += 180.0;
                        }
                        else{ // x == 0
                            if(z > 0.001) rY = 180.0;
                            else rY = -180;
                        }
                    }
                    
                    double rX = z > x ? 
                            (z > 0.001 || z < 0.001 ? Math.atan(Math.abs(y)/Math.abs(z)) * 180.0 / Math.PI : 90.0)
                            : (x > 0.001 || x < 0.001 ? Math.atan(Math.abs(y)/Math.abs(x)) * 180.0 / Math.PI : 90.0);
                    if(y > 0.001){
                        rX = -rX;
                    }
                    
                    this.cameras.get(3).getTransforms().addAll(
                            new Rotate(rY, Rotate.Y_AXIS),
                            new Rotate(rX, Rotate.X_AXIS),
                            //new Rotate(rZ, Rotate.Z_AXIS),
                            new Translate(0, 0, -50));
                }
                else{
                    main.Main.ToggleBallCamera();
                }
                if ( this.gameOver == false ) {
			if ( islandHit || cannon.getHealth() <= 0) {
                                Info.increaseLanded(landedCnt);
				this.listener.onGameLost ( );
				this.gameOver = true;
			} else if ( this.weapon != null ) {
				//boolean boatHit = this.movableObjects.removeIf ( object -> object.handleCollision ( this.weapon ) );
                            boolean boatHit = false;
                            for(MovableObject boat : movableObjects){
                                if(boat.handleCollision(this.weapon)){
                                    boatHit = true;
                                    Info.increaseDestroyed();
                                }
                            }
                            if ( boatHit ) {
					this.weapon.onCollision ( );
					this.weapon = null;
                            }
			}
                        if ( this.movableObjects.isEmpty() && WaveCount > 3 ) {
                                this.listener.onGameWon ( );
				this.gameOver = true;
                            }
                            else if(this.movableObjects.isEmpty() && WaveCount <= 3){
                                main.Main.InitBoats(WaveCount);
                                WaveCount++;
                                weaponCount = weaponCount < 10 ? weaponCount + 10 : 20;
                                System.out.println(WaveCount);
                            }
		}
		
		if ( this.weapon != null ) {
			this.weapon.update ( now );
		}
                
                boatWeapon.forEach(obj ->{
                    obj.update(now);
                });
                    
                    
                if(cannon != null)
                    boatWeapon.removeIf((t) -> {
                        return ((BoatBall)t).handleCollision(cannon);
                    });
                
                if(Math.random() * 0.5 < 0.001){
                    int index = (int)Math.round(Math.random() * movableObjects.size());
                    if (index == movableObjects.size()) index--;
                    if(index >= 0) boatWeapon.add(((Boat)movableObjects.get(index)).fire(this));
                }
                
                if(miniMap != null) miniMap.update();
	}
	
	public synchronized boolean canAddWeapon ( ) {
		return this.gameOver == false && this.weapon == null && this.weaponCount > 0;
	}
	
	public synchronized void setWeapon ( MovableObject weapon ) {
		this.weapon = weapon;
	}
        
        public synchronized int getWeaponCount(){
            return this.weaponCount;
        }
        
        public synchronized void decreasWeaponCount(){
            this.weaponCount--;
        }
        
        public synchronized void setCannon(Cannon cannon){
            this.cannon = cannon;
        }
        
        public synchronized void decreasCannonHealth(){
            this.cannon.setHealth(this.cannon.getHealth() - 10);
            System.out.println("HP: " + this.cannon.getHealth());
            Info.updateHP(this.cannon.getHealth());
        }
        
        public synchronized void setMovableObjects(List<MovableObject> objects){
            movableObjects = objects;
        }
        
        public synchronized void setMiniMap(MiniMap minimap){
            this.miniMap = minimap;
        }
}
