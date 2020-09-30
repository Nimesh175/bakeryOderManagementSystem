/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.custom;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;

/**
 *
 * @author NIMESH
 */
public class IDGeneratorConnection {
     public static String getNewID(String tableName, String colName, String IDFirstLetter) throws SQLException, ClassNotFoundException, IOException {
        String lastId = IDGeneratedQuery.getLastID(tableName, colName);
        if (lastId != null) {
            int id = Integer.parseInt(lastId.split(IDFirstLetter)[1]);
            id++;
            NumberFormat numberFormat = NumberFormat.getIntegerInstance();
            numberFormat.setMinimumIntegerDigits(3);
            numberFormat.setGroupingUsed(false);
            String newID = numberFormat.format(id);
            return IDFirstLetter + newID;
        } else {
            return IDFirstLetter + "001";
        }
    }
    
}
