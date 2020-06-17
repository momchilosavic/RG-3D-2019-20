// 
// Decompiled by Procyon v0.5.36
// 

package subScenes;

import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Transform;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Cylinder;
import javafx.scene.paint.Color;
import objects.Cannon;
import javafx.scene.transform.Rotate;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;
import javafx.scene.shape.Rectangle;
import objects.movable.Boat;
import javafx.scene.Group;
import objects.movable.MovableObject;

public class MiniMap extends Group
{
    private double ratio;
    private Boat[] boats;
    private Rectangle[] miniMapBoats;
    private Translate[] miniMapBoatPositions;
    private Polygon miniMapCannon;
    private Rotate miniMapCannonRotate;
    private Cannon cannon;
    private Group elements;
    private int ij = 0;
    
    public MiniMap(final double width, final double height, final double widthMargin, final double heightMargin, final Color backgroundColor, final double boatDistance, final List<MovableObject> boats, final Cylinder island) {
        final Rectangle background = new Rectangle(width, height, (Paint)backgroundColor);
        super.getChildren().addAll((Node[])(Object[])new Node[] { (Node)background });
        final double minimalDimension = Math.min(width - widthMargin, height - heightMargin);
        this.ratio = minimalDimension / (2.0 * boatDistance);
        this.elements = new Group();
        this.boats = new Boat[boats.size()];
        boats.forEach(obj ->{
            this.boats[ij] = ((Boat)boats.get(ij));
            ij++;
        });
        this.miniMapBoats = new Rectangle[this.boats.length];
        this.miniMapBoatPositions = new Translate[this.miniMapBoats.length];
        for (int i = 0; i < this.miniMapBoats.length; ++i) {
            final double boatWidth = this.ratio * main.Main.Constants.BOAT_WIDTH;
            final double boatHeight = this.ratio * main.Main.Constants.BOAT_DEPTH;
            this.miniMapBoats[i] = new Rectangle(boatWidth, boatHeight, Color.GRAY);
            this.miniMapBoatPositions[i] = new Translate();
            final double angle = ((Boat)boats.get(i)).angle;
            this.miniMapBoats[i].getTransforms().addAll((Transform[])(Object[])new Transform[] { (Transform)this.miniMapBoatPositions[i], (Transform)new Rotate(angle), (Transform)new Translate(-boatWidth / 2.0, -boatHeight / 2.0) });
            this.elements.getChildren().addAll((Node[])(Object[])new Node[] { (Node)this.miniMapBoats[i] });
        }
        final double islandRadius = island.getRadius() * this.ratio;
        final Circle miniMapIsland = new Circle(islandRadius, (Paint)((PhongMaterial)island.getMaterial()).getDiffuseColor());
        this.elements.getChildren().addAll((Node[])(Object[])new Node[] { (Node)miniMapIsland });
        this.miniMapCannon = new Polygon(new double[] { 0.0 * islandRadius, -0.9 * islandRadius, 0.25 * islandRadius, 0.5 * islandRadius, -0.25 * islandRadius, 0.5 * islandRadius });
        this.miniMapCannonRotate = new Rotate();
        this.miniMapCannon.getTransforms().addAll((Transform[])(Object[])new Transform[] { (Transform)this.miniMapCannonRotate });
        this.elements.getChildren().addAll((Node[])(Object[])new Node[] { (Node)this.miniMapCannon });
        this.elements.getTransforms().addAll((Transform[])(Object[])new Transform[] { (Transform)new Translate(width / 2.0, height / 2.0) });
        super.getChildren().addAll((Node[])(Object[])new Node[] { (Node)this.elements });
    }
    
    public void setCannon(final Cannon cannon) {
        this.cannon = cannon;
        this.miniMapCannon.setFill(Color.GREEN);
    }
    
    public void update() {
        for (int i = 0; i < this.miniMapBoats.length; ++i) {
            if (!this.boats[i].underSea()) {
                final double x = this.boats[i].position.getTx() * this.ratio;
                final double y = -this.boats[i].position.getTz() * this.ratio;
                this.miniMapBoatPositions[i].setX(x);
                this.miniMapBoatPositions[i].setY(y);
            }
            else {
                this.elements.getChildren().remove((Object)this.miniMapBoats[i]);
            }
        }
        if (this.cannon != null) {
            this.miniMapCannonRotate.setAngle(this.cannon.rotateY.getAngle());
        }
    }
}
