/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.model;

/**
 *
 * @author NIMESH
 */
public class Customer {

   private String participation;
   private String cusID;
   private String fName;
   private String lName;
   private String NIC;
   private String gender;
   private int telephone;
   private String route;

    public Customer() {}
    
    public Customer(String participation, String cusID, String fName, String lName,String gender, String NIC,  int telephone, String route) {
        this.participation = participation;
        this.cusID = cusID;
        this.fName = fName;
        this.lName = lName;
        this.NIC = NIC;
        this.gender = gender;
        this.telephone = telephone;
        this.route = route;
    }

    public String getParticipation() {
        return participation;
    }

    public void setParticipation(String participation) {
        this.participation = participation;
    }

    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public String getFName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String Route) {
        this.route = Route;
    }

    @Override
    public String toString() {
        return "Customer{" + "participation=" 
                + participation + ", cusID=" 
                + cusID + ", fName=" + fName 
                + ", lName=" + lName + ", NIC=" 
                + NIC + ", gender=" + gender + ", telephone=" 
                + telephone + ", Route=" + route + '}';
    }
    
  
   
}
