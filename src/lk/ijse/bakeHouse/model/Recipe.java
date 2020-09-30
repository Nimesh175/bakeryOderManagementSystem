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
public class Recipe {
    
    private String primaryID;
    private String proID;
    private String materialID;
    private String materialName;
    private double size;
    private String unit;
    private String description;
    
    public Recipe() {
    }

    public Recipe(String primaryID, String proID, String materialID, String materialName, double size, String unit, String description) {
        this.primaryID = primaryID;
        this.proID = proID;
        this.materialID = materialID;
        this.materialName = materialName;
        this.size = size;
        this.unit = unit;
        this.description = description;
    }

    public String getPrimaryID() {
        return primaryID;
    }

    public void setPrimaryID(String primaryID) {
        this.primaryID = primaryID;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Recipe{" + "primaryID=" + primaryID + ", proID=" + proID + ", materialID=" 
                + materialID + ", materialName=" + materialName + ", size=" + size 
                + ", unit=" + unit + ", description=" + description + '}';
    }

   
    
     
    
}
