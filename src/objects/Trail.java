/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import objects.movable.weapon.CannonBall;

/**
 *
 * @author Korisnik
 */
public class Trail extends Group{
        private Group group;
        private Cylinder root;
        private PhongMaterial material;
        private AnimationTimer timer;
        private Group parent;
        double height = 0;
        double maxHeight;
        
        public Trail(Group group, double radius, Group parent){
            this.group = group;
            this.maxHeight = 3*radius;
            this.parent = parent;
            root = new Cylinder(radius, 1);
            material = new PhongMaterial(Color.BLUE);
            root.setMaterial(material);
            root.getTransforms().add(new Translate(0, radius, 0));
            this.getChildren().add(root);
            timer = new AnimationTimer() {
                long lastUpdate = 0;
                long limit = 0;
                int direction = 1;
                double delta = maxHeight / (2 * 1000000000l);
                @Override
                public void handle(long now) {
                    if(lastUpdate == 0) lastUpdate = now;
                    if(now - lastUpdate < 1000000000l / 28) return;
                    update(direction, delta * (now - lastUpdate));
                    limit += (now - lastUpdate);
                    if(limit >= 2000000000l && direction == 1){
                        direction = -1;
                        limit = 0;
                    }
                    if(limit >= 2000000000l && direction == -1){
                        group.getChildren().remove(this);
                    }
                    lastUpdate = now;
                }
            };
            timer.start();
        }
    
        void update(int direction, double delta){
            root.setHeight(root.getHeight() + direction * delta);
            //root.getTransforms().addAll(new Translate(0, -direction * root.getHeight()/2, 0));
            material.diffuseColorProperty().set(material.diffuseColorProperty().get().interpolate(direction > 0 ? Color.WHITE : Color.BLUE, direction > 0 ? root.getHeight()/maxHeight/8 : (maxHeight - root.getHeight()) / maxHeight/8));
        }
    }
