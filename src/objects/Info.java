/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author PC
 */
public class Info extends Group{
    private static Info instance = null;
    private Info(){
        txt1.setText("Destroyed: 0");
        txt2.setText("Landed: 0");
        txt3.setText("HP: 100%");
        txt1.setY(20);
        txt2.setY(40);
        txt3.setY(80);
        
        txt1.setFont(Font.font("arial", FontWeight.LIGHT, 20));
        txt2.setFont(Font.font("arial", FontWeight.LIGHT, 20));
        txt3.setFont(Font.font("arial", FontWeight.LIGHT, 20));
        
        txt1.setFill(Color.YELLOW);
        txt2.setFill(Color.YELLOW);
        txt3.setFill(Color.YELLOW);
        
        super.getChildren().addAll(txt1, txt2, txt3);
    }
    
    private static int maxHP = 100;
    private static int currentHP = 100;
    private static int destroyedCount = 0;
    private static int landedCount = 0;
    
    private static Text txt1 = new Text();
    private static Text txt2 = new Text();
    private static Text txt3 = new Text();
    
    public static Info GetInstance(){
        if(instance == null) instance = new Info();
        return instance;
    }
    
    public static void increaseDestroyed(){
        destroyedCount++;
        txt1.setText("Destroyed: " + destroyedCount);
    }
    
    public static void increaseLanded(int count){
        landedCount+=count;
        txt2.setText("Landed: " + landedCount);
    }
    
    public static void updateHP(int currentHP){
        txt3.setText("HP: " + (double)currentHP/maxHP*100 + "%");
    }
}
