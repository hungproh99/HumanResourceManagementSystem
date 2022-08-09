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
public class WorkingPlace implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long      workingPlaceId;
    private LocalDate startDate;
    private Boolean   workingPlaceStatus;
    private Long      areaId;
    private Long      gradeId;
    private Long      jobId;
    private Long      officeId;
    private Long      workingContractId;

    public WorkingPlace() {}

    public WorkingPlace(WorkingPlace value) {
        this.workingPlaceId = value.workingPlaceId;
        this.startDate = value.startDate;
        this.workingPlaceStatus = value.workingPlaceStatus;
        this.areaId = value.areaId;
        this.gradeId = value.gradeId;
        this.jobId = value.jobId;
        this.officeId = value.officeId;
        this.workingContractId = value.workingContractId;
    }

    public WorkingPlace(
        Long      workingPlaceId,
        LocalDate startDate,
        Boolean   workingPlaceStatus,
        Long      areaId,
        Long      gradeId,
        Long      jobId,
        Long      officeId,
        Long      workingContractId
    ) {
        this.workingPlaceId = workingPlaceId;
        this.startDate = startDate;
        this.workingPlaceStatus = workingPlaceStatus;
        this.areaId = areaId;
        this.gradeId = gradeId;
        this.jobId = jobId;
        this.officeId = officeId;
        this.workingContractId = workingContractId;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.working_place_id</code>.
     */
    public Long getWorkingPlaceId() {
        return this.workingPlaceId;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.working_place_id</code>.
     */
    public WorkingPlace setWorkingPlaceId(Long workingPlaceId) {
        this.workingPlaceId = workingPlaceId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.start_date</code>.
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.start_date</code>.
     */
    public WorkingPlace setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.working_place_status</code>.
     */
    public Boolean getWorkingPlaceStatus() {
        return this.workingPlaceStatus;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.working_place_status</code>.
     */
    public WorkingPlace setWorkingPlaceStatus(Boolean workingPlaceStatus) {
        this.workingPlaceStatus = workingPlaceStatus;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.working_place.area_id</code>.
     */
    public Long getAreaId() {
        return this.areaId;
    }

    /**
     * Setter for <code>human_resource_management.working_place.area_id</code>.
     */
    public WorkingPlace setAreaId(Long areaId) {
        this.areaId = areaId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.working_place.grade_id</code>.
     */
    public Long getGradeId() {
        return this.gradeId;
    }

    /**
     * Setter for <code>human_resource_management.working_place.grade_id</code>.
     */
    public WorkingPlace setGradeId(Long gradeId) {
        this.gradeId = gradeId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.working_place.job_id</code>.
     */
    public Long getJobId() {
        return this.jobId;
    }

    /**
     * Setter for <code>human_resource_management.working_place.job_id</code>.
     */
    public WorkingPlace setJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.office_id</code>.
     */
    public Long getOfficeId() {
        return this.officeId;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.office_id</code>.
     */
    public WorkingPlace setOfficeId(Long officeId) {
        this.officeId = officeId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.working_place.working_contract_id</code>.
     */
    public Long getWorkingContractId() {
        return this.workingContractId;
    }

    /**
     * Setter for
     * <code>human_resource_management.working_place.working_contract_id</code>.
     */
    public WorkingPlace setWorkingContractId(Long workingContractId) {
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
        final WorkingPlace other = (WorkingPlace) obj;
        if (this.workingPlaceId == null) {
            if (other.workingPlaceId != null)
                return false;
        }
        else if (!this.workingPlaceId.equals(other.workingPlaceId))
            return false;
        if (this.startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!this.startDate.equals(other.startDate))
            return false;
        if (this.workingPlaceStatus == null) {
            if (other.workingPlaceStatus != null)
                return false;
        }
        else if (!this.workingPlaceStatus.equals(other.workingPlaceStatus))
            return false;
        if (this.areaId == null) {
            if (other.areaId != null)
                return false;
        }
        else if (!this.areaId.equals(other.areaId))
            return false;
        if (this.gradeId == null) {
            if (other.gradeId != null)
                return false;
        }
        else if (!this.gradeId.equals(other.gradeId))
            return false;
        if (this.jobId == null) {
            if (other.jobId != null)
                return false;
        }
        else if (!this.jobId.equals(other.jobId))
            return false;
        if (this.officeId == null) {
            if (other.officeId != null)
                return false;
        }
        else if (!this.officeId.equals(other.officeId))
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
        result = prime * result + ((this.workingPlaceId == null) ? 0 : this.workingPlaceId.hashCode());
        result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
        result = prime * result + ((this.workingPlaceStatus == null) ? 0 : this.workingPlaceStatus.hashCode());
        result = prime * result + ((this.areaId == null) ? 0 : this.areaId.hashCode());
        result = prime * result + ((this.gradeId == null) ? 0 : this.gradeId.hashCode());
        result = prime * result + ((this.jobId == null) ? 0 : this.jobId.hashCode());
        result = prime * result + ((this.officeId == null) ? 0 : this.officeId.hashCode());
        result = prime * result + ((this.workingContractId == null) ? 0 : this.workingContractId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WorkingPlace (");

        sb.append(workingPlaceId);
        sb.append(", ").append(startDate);
        sb.append(", ").append(workingPlaceStatus);
        sb.append(", ").append(areaId);
        sb.append(", ").append(gradeId);
        sb.append(", ").append(jobId);
        sb.append(", ").append(officeId);
        sb.append(", ").append(workingContractId);

        sb.append(")");
        return sb.toString();
    }
}
