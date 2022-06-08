/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LaudatoryType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long   typeId;
    private String name;

    public LaudatoryType() {}

    public LaudatoryType(LaudatoryType value) {
        this.typeId = value.typeId;
        this.name = value.name;
    }

    public LaudatoryType(
        Long   typeId,
        String name
    ) {
        this.typeId = typeId;
        this.name = name;
    }

    /**
     * Getter for <code>human_resource_management.laudatory_type.type_id</code>.
     */
    public Long getTypeId() {
        return this.typeId;
    }

    /**
     * Setter for <code>human_resource_management.laudatory_type.type_id</code>.
     */
    public LaudatoryType setTypeId(Long typeId) {
        this.typeId = typeId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.laudatory_type.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>human_resource_management.laudatory_type.name</code>.
     */
    public LaudatoryType setName(String name) {
        this.name = name;
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
        final LaudatoryType other = (LaudatoryType) obj;
        if (this.typeId == null) {
            if (other.typeId != null)
                return false;
        }
        else if (!this.typeId.equals(other.typeId))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.typeId == null) ? 0 : this.typeId.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LaudatoryType (");

        sb.append(typeId);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }
}
