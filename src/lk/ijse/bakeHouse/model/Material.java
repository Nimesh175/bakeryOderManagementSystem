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
public class Material {
    
    private String materialID;
    private String materialName;
    private String brandID;
    private String materialType;
    private String materialDescription;
    
    
     public Material(String materialID, String materialName, String brandID, String materialType, String materialDescription) {
        this.materialID = materialID;
        this.materialName = materialName;
        this.brandID = brandID;
        this.materialType = materialType;
        this.materialDescription = materialDescription;
    }

    public Material() {
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

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    
    @Override
    public String toString() {
        return "Material{" + "materialID=" + materialID + ", materialName=" +
                materialName + ", brandID=" + brandID + ", materialType=" + materialType
                + ", materialDescription=" + materialDescription + '}';
    }

   
}
