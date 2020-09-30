
package lk.ijse.bakeHouse.custom;

import lk.ijse.bakeHouse.custom.GoToAnotherWindow;
import java.io.IOException;
import javafx.event.ActionEvent;
/**
 *
 * @author NIMESH
 */
//using singelton pattern
public class NavigationOnDashboard {
   
    private static NavigationOnDashboard navigationBar;
   
    public static NavigationOnDashboard getInstance(){
        if (navigationBar==null) {
            navigationBar=new NavigationOnDashboard();
        }
        return navigationBar;
    }
    
    public void goBackHome(ActionEvent event) throws IOException
    {
      GoToAnotherWindow.getAnotherWindow().makeWindow(event,"/lk/ijse/bakeHouse/view/DashboardForm.fxml");  
    }
    
}
