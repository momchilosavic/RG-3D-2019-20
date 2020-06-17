/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

/**
 *
 * @author PC
 */
public class WeaponCounter extends Group {
    public WeaponCounter(int count){
        for(int i = 0; i < count; i++){
            Rectangle granade = new Rectangle(10, 20, Color.YELLOW);
            granade.getTransforms().add(new Translate(-i*30, 0, 0));
            super.getChildren().add(granade);
        }
    }
    
    public void decrease(){
        super.getChildren().remove(super.getChildren().size() - 1);
    }
    
    public void set(int count){
        if(super.getChildren().size() > count){
            for(int i = super.getChildren().size() - 1; i >= count; i--)
                super.getChildren().remove(i);
        }
        else if(super.getChildren().size() < count){
            for(int i = super.getChildren().size(); i < count; i++){
            Rectangle granade = new Rectangle(10, 20, Color.YELLOW);
            granade.getTransforms().add(new Translate(-i*30, 0, 0));
            super.getChildren().add(granade);                
            }
        }
        System.out.println(super.getChildren().size());
    }
}
