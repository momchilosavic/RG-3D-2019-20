/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;

/**
 *
 * @author PC
 */
public class Distance extends Group {
    private static final double R[] = {50, 150, 300, 500, 1000};
    
    public Distance(double height, PhongMaterial material){
        Cylinder circles[] = new Cylinder[5];
        for(int i = 4; i >= 0; i--){
            circles[i] = new Cylinder(R[i], height);
            circles[i].setMaterial(new PhongMaterial(Color.AQUAMARINE));
            circles[i].setDepthTest(DepthTest.DISABLE);
            
           Cylinder temp = new Cylinder(R[i] - 2, height - 2);
           temp.setMaterial(material);
            
            super.getChildren().addAll(circles[i], temp);
        }
        
    }
}
