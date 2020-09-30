
package lk.ijse.bakeHouse.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lk.ijse.bakeHouse.main.NewFXMain;


public class LoadingFormeController implements Initializable {
   
    
    @FXML
    private AnchorPane paneAnchormain;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        System.gc();
        //Load splash screen with fade in effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), paneAnchormain);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        //Finish splash with fade out effect
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), paneAnchormain);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeIn.play();
        //After fade in, start fade out
        fadeIn.setOnFinished((e) -> {
            fadeOut.play();
        });
        //After fade out, load actual content
        fadeOut.setOnFinished((ActionEvent event) -> {
            try {
                   
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/bakeHouse/view/LoginAndSignupForm.fxml"));
         
            FadeTransition fade = new FadeTransition(Duration.seconds(2), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play(); 
            
            Scene scene = new Scene(root);
            Stage mainStage = NewFXMain.getStage();
            mainStage.setScene(scene);
    
            
            }catch (IOException ex) {
                System.out.println(ex);
            }
        });
  
    } 
   
}
