/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.main;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
/**
 *
 * @author NIMESH
 */
public class NewFXMain extends Application {
    
     private static Stage stage;
   
     @Override
    public void start(Stage primaryStage) throws IOException  {
        System.gc();
        stage=primaryStage;
      
      Parent root = FXMLLoader.load(this.getClass().getResource( "/lk/ijse/bakeHouse/view/LoadingForme.fxml"));
      Scene scene=new Scene(root,Color.TRANSPARENT);
      root.setStyle("-fx-background-color : transparent ;");
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.initStyle(StageStyle.DECORATED);//maximum,minimize,close
           
      primaryStage.getIcons().add( new Image(getClass().getResourceAsStream(
              "/lk/ijse/bakeHouse/assests/Untitled.jpg")));
      //title Icon Change
      
      primaryStage.centerOnScreen();
      primaryStage.show();
     
 
      FadeTransition tempTransition = new FadeTransition(Duration.millis(2500),root);
      tempTransition.setFromValue(0.0);
      tempTransition.setToValue(1.0);
      tempTransition.play();
      
    }
   
    public static void main(String[] args) {
        launch(args);       
    }  

    public static Stage getStage() {
        return stage;
    }
   
}
