/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkingInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long      workingInformationId;
    private LocalDate endDate;
    private LocalDate startDate;
    private Long      workingContractId;

    public WorkingInformation() {}

    public WorkingInformation(WorkingInformation value) {
        this.workingInformationId = value.workingInformationId;
        this.endDate = value.endDate;
        this.startDate = value.startDate;
        this.workingContractId = value.workingContractId;
    }

    public WorkingInformation(
        Long      workingInformationId,
        LocalDate endDate,
        LocalDate startDate,
        Long      workingContractId
    ) {
        this.workingInformationId = workingInformationId;
        this.endDate = endDate;
        this.startDate = startDate;
        this.workingContractId = workingContractId;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_information.working_information_id</code>.
     */
    public Long getWorkingInformationId() {
        return this.workingInformationId;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_information.working_information_id</code>.
     */
    public WorkingInformation setWorkingInformationId(Long workingInformationId) {
        this.workingInformationId = workingInformationId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_information.end_date</code>.
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_information.end_date</code>.
     */
    public WorkingInformation setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_information.start_date</code>.
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_information.start_date</code>.
     */
    public WorkingInformation setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_information.working_contract_id</code>.
     */
    public Long getWorkingContractId() {
        return this.workingContractId;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_information.working_contract_id</code>.
     */
    public WorkingInformation setWorkingContractId(Long workingContractId) {
        this.workingContractId = workingContractId;
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
        final WorkingInformation other = (WorkingInformation) obj;
        if (this.workingInformationId == null) {
            if (other.workingInformationId != null)
                return false;
        }
        else if (!this.workingInformationId.equals(other.workingInformationId))
            return false;
        if (this.endDate == null) {
            if (other.endDate != null)
                return false;
        }
        else if (!this.endDate.equals(other.endDate))
            return false;
        if (this.startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!this.startDate.equals(other.startDate))
            return false;
        if (this.workingContractId == null) {
            if (other.workingContractId != null)
                return false;
        }
        else if (!this.workingContractId.equals(other.workingContractId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.workingInformationId == null) ? 0 : this.workingInformationId.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
        result = prime * result + ((this.workingContractId == null) ? 0 : this.workingContractId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WorkingInformation (");

        sb.append(workingInformationId);
        sb.append(", ").append(endDate);
        sb.append(", ").append(startDate);
        sb.append(", ").append(workingContractId);

        sb.append(")");
        return sb.toString();
    }
}
