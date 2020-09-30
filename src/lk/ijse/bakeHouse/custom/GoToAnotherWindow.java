
package lk.ijse.bakeHouse.custom;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author NIMESH
 */
public class GoToAnotherWindow {
     
    private static GoToAnotherWindow window;
    
    private GoToAnotherWindow(){}
    
    public void makeWindow(ActionEvent event,String fxml) throws IOException{
         Parent root = FXMLLoader.load(getClass().getResource(fxml));
            
            FadeTransition tempTransition = new FadeTransition(Duration.millis(3500),root);
            tempTransition.setFromValue(0.0);
            tempTransition.setToValue(1.0);
            tempTransition.play();
            
            Scene scene = new Scene(root);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(scene);
            mainStage.centerOnScreen();
            mainStage.setWidth(1200);
            mainStage.setHeight(720);
            mainStage.show();
    }//CLOSE
    
    public static GoToAnotherWindow getAnotherWindow(){
        if (window==null) {
            window = new GoToAnotherWindow();
        }
       return window;
    }
}
