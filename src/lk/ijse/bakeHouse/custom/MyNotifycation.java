
package lk.ijse.bakeHouse.custom;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author NIMESH
 */
public class MyNotifycation {
    
    private static String massage01="SUCCESS!";
    private static String massage02="ERROR";
    private static String massage03="WARRNING!";
    private static String massage04="INFORMATION!";
    
    public static TrayNotification sucsessfullyNotify()
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage01);
        tray.setMessage("YOUR REQUEST WAS COMPLETED SUCSESSFULLY.");
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
    public static TrayNotification sucsessfullyNotify(String massageBody)
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage01);
        tray.setMessage(massageBody);
        tray.setNotificationType(NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
     public static TrayNotification unSucsessfullyNotify()
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage02);
        tray.setMessage("YOUR REQUEST WAS COMPLETED UNSUCSESSFULLY.");
        tray.setNotificationType(NotificationType.ERROR);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
       public static TrayNotification unSucsessfullyNotify(String massageBody)
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage02);
        tray.setMessage(massageBody);
        tray.setNotificationType(NotificationType.ERROR);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
    public static TrayNotification warningNotify()
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage03);
        tray.setMessage("SOMETHING IS WRONG.");
        tray.setNotificationType(NotificationType.WARNING);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
     public static TrayNotification warningNotify(String massageBody)
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage03);
        tray.setMessage(massageBody);
        tray.setNotificationType(NotificationType.WARNING);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
     public static TrayNotification infoNotify()
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage04);
        tray.setMessage("SOMETHING IS WRONG.");
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    }
     public static TrayNotification infoNotify(String massageBody)
    {
        TrayNotification tray =new TrayNotification();
        AnimationType type=AnimationType.POPUP;
        
        tray.setAnimationType(type);
        tray.setTitle(massage04);
        tray.setMessage(massageBody);
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.showAndDismiss(Duration.millis(2500));
        return tray;
    
    }
        
        
        private static void setIconToTheAlert(Alert a) {

        }
     
        public static boolean askQuestion(String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        setIconToTheAlert(a);
        Optional<ButtonType> btSet = a.showAndWait();
        if (btSet.get() == ButtonType.YES)
            return true;
        else if (btSet.get() == ButtonType.NO)
            return false;
        else
            return false;
    }

    public static boolean askWarning(String message) {
        Alert a = new Alert(Alert.AlertType.WARNING, message, ButtonType.YES, ButtonType.NO);
        setIconToTheAlert(a);
        Optional<ButtonType> btSet = a.showAndWait();
        if (btSet.get() == ButtonType.YES)
            return true;
        else if (btSet.get() == ButtonType.NO)
            return false;
        else
            return false;
    } 
}//
