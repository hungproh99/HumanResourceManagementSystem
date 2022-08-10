/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long   areaId;
    private String name;
    private String managerId;

    public Area() {}

    public Area(Area value) {
        this.areaId = value.areaId;
        this.name = value.name;
        this.managerId = value.managerId;
    }

    public Area(
        Long   areaId,
        String name,
        String managerId
    ) {
        this.areaId = areaId;
        this.name = name;
        this.managerId = managerId;
    }

    /**
     * Getter for <code>human_resource_management.area.area_id</code>.
     */
    public Long getAreaId() {
        return this.areaId;
    }

    /**
     * Setter for <code>human_resource_management.area.area_id</code>.
     */
    public Area setAreaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.area.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>human_resource_management.area.name</code>.
     */
    public Area setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.area.manager_id</code>.
     */
    public String getManagerId() {
        return this.managerId;
    }

    /**
     * Setter for <code>human_resource_management.area.manager_id</code>.
     */
    public Area setManagerId(String managerId) {
        this.managerId = managerId;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Area other = (Area) obj;
        if (this.areaId == null) {
            if (other.areaId != null)
                return false;
        }
        else if (!this.areaId.equals(other.areaId))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.managerId == null) {
            if (other.managerId != null)
                return false;
        }
        else if (!this.managerId.equals(other.managerId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.areaId == null) ? 0 : this.areaId.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.managerId == null) ? 0 : this.managerId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Area (");

        sb.append(areaId);
        sb.append(", ").append(name);
        sb.append(", ").append(managerId);

        sb.append(")");
        return sb.toString();
    }
}
