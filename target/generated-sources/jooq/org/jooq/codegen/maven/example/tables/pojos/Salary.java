/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long       laudatoryId;
    private LocalDate  endDate;
    private BigDecimal finalSalary;
    private LocalDate  startDate;
    private BigDecimal subsidize;
    private Long       workingInformationId;

    public Salary() {}

    public Salary(Salary value) {
        this.laudatoryId = value.laudatoryId;
        this.endDate = value.endDate;
        this.finalSalary = value.finalSalary;
        this.startDate = value.startDate;
        this.subsidize = value.subsidize;
        this.workingInformationId = value.workingInformationId;
    }

    public Salary(
        Long       laudatoryId,
        LocalDate  endDate,
        BigDecimal finalSalary,
        LocalDate  startDate,
        BigDecimal subsidize,
        Long       workingInformationId
    ) {
        this.laudatoryId = laudatoryId;
        this.endDate = endDate;
        this.finalSalary = finalSalary;
        this.startDate = startDate;
        this.subsidize = subsidize;
        this.workingInformationId = workingInformationId;
    }

    /**
     * Getter for <code>human_resource_management.salary.laudatory_id</code>.
     */
    public Long getLaudatoryId() {
        return this.laudatoryId;
    }

    /**
     * Setter for <code>human_resource_management.salary.laudatory_id</code>.
     */
    public Salary setLaudatoryId(Long laudatoryId) {
        this.laudatoryId = laudatoryId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.salary.end_date</code>.
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**
     * Setter for <code>human_resource_management.salary.end_date</code>.
     */
    public Salary setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.salary.final_salary</code>.
     */
    public BigDecimal getFinalSalary() {
        return this.finalSalary;
    }

    /**
     * Setter for <code>human_resource_management.salary.final_salary</code>.
     */
    public Salary setFinalSalary(BigDecimal finalSalary) {
        this.finalSalary = finalSalary;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.salary.start_date</code>.
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * Setter for <code>human_resource_management.salary.start_date</code>.
     */
    public Salary setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.salary.subsidize</code>.
     */
    public BigDecimal getSubsidize() {
        return this.subsidize;
    }

    /**
     * Setter for <code>human_resource_management.salary.subsidize</code>.
     */
    public Salary setSubsidize(BigDecimal subsidize) {
        this.subsidize = subsidize;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary.working_information_id</code>.
     */
    public Long getWorkingInformationId() {
        return this.workingInformationId;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary.working_information_id</code>.
     */
    public Salary setWorkingInformationId(Long workingInformationId) {
        this.workingInformationId = workingInformationId;
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
        final Salary other = (Salary) obj;
        if (this.laudatoryId == null) {
            if (other.laudatoryId != null)
                return false;
        }
        else if (!this.laudatoryId.equals(other.laudatoryId))
            return false;
        if (this.endDate == null) {
            if (other.endDate != null)
                return false;
        }
        else if (!this.endDate.equals(other.endDate))
            return false;
        if (this.finalSalary == null) {
            if (other.finalSalary != null)
                return false;
        }
        else if (!this.finalSalary.equals(other.finalSalary))
            return false;
        if (this.startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!this.startDate.equals(other.startDate))
            return false;
        if (this.subsidize == null) {
            if (other.subsidize != null)
                return false;
        }
        else if (!this.subsidize.equals(other.subsidize))
            return false;
        if (this.workingInformationId == null) {
            if (other.workingInformationId != null)
                return false;
        }
        else if (!this.workingInformationId.equals(other.workingInformationId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.laudatoryId == null) ? 0 : this.laudatoryId.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.finalSalary == null) ? 0 : this.finalSalary.hashCode());
        result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
        result = prime * result + ((this.subsidize == null) ? 0 : this.subsidize.hashCode());
        result = prime * result + ((this.workingInformationId == null) ? 0 : this.workingInformationId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Salary (");

        sb.append(laudatoryId);
        sb.append(", ").append(endDate);
        sb.append(", ").append(finalSalary);
        sb.append(", ").append(startDate);
        sb.append(", ").append(subsidize);
        sb.append(", ").append(workingInformationId);

        sb.append(")");
        return sb.toString();
    }
}
