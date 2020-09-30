
package lk.ijse.bakeHouse.model;

/**
 *
 * @author NIMESH
 */
public class Route {
    
        private String routeID;
        private String name;
        private String description;

    public Route() {
    }

    public Route(String routeID, String name, String description) {
        this.routeID = routeID;
        this.name = name;
        this.description = description;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Route{" + "routeID=" + routeID + ", name=" + name + ", description=" + description + '}';
    }
        
    
}
