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
public class SalaryMonthly implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long       salaryId;
    private Double     actualPoint;
    private LocalDate  endDate;
    private BigDecimal finalSalary;
    private Double     otPoint;
    private Double     standardPoint;
    private LocalDate  startDate;
    private BigDecimal totalAdvance;
    private BigDecimal totalBonus;
    private BigDecimal totalDeduction;
    private BigDecimal totalInsurancePayment;
    private BigDecimal totalTaxPayment;
    private Long       salaryContractId;
    private Long       salaryStatusId;
    private Boolean    isRemind;
    private String     approver;
    private LocalDate  duration;
    private String     comment;
    private BigDecimal totalAllowance;

    public SalaryMonthly() {}

    public SalaryMonthly(SalaryMonthly value) {
        this.salaryId = value.salaryId;
        this.actualPoint = value.actualPoint;
        this.endDate = value.endDate;
        this.finalSalary = value.finalSalary;
        this.otPoint = value.otPoint;
        this.standardPoint = value.standardPoint;
        this.startDate = value.startDate;
        this.totalAdvance = value.totalAdvance;
        this.totalBonus = value.totalBonus;
        this.totalDeduction = value.totalDeduction;
        this.totalInsurancePayment = value.totalInsurancePayment;
        this.totalTaxPayment = value.totalTaxPayment;
        this.salaryContractId = value.salaryContractId;
        this.salaryStatusId = value.salaryStatusId;
        this.isRemind = value.isRemind;
        this.approver = value.approver;
        this.duration = value.duration;
        this.comment = value.comment;
        this.totalAllowance = value.totalAllowance;
    }

    public SalaryMonthly(
        Long       salaryId,
        Double     actualPoint,
        LocalDate  endDate,
        BigDecimal finalSalary,
        Double     otPoint,
        Double     standardPoint,
        LocalDate  startDate,
        BigDecimal totalAdvance,
        BigDecimal totalBonus,
        BigDecimal totalDeduction,
        BigDecimal totalInsurancePayment,
        BigDecimal totalTaxPayment,
        Long       salaryContractId,
        Long       salaryStatusId,
        Boolean    isRemind,
        String     approver,
        LocalDate  duration,
        String     comment,
        BigDecimal totalAllowance
    ) {
        this.salaryId = salaryId;
        this.actualPoint = actualPoint;
        this.endDate = endDate;
        this.finalSalary = finalSalary;
        this.otPoint = otPoint;
        this.standardPoint = standardPoint;
        this.startDate = startDate;
        this.totalAdvance = totalAdvance;
        this.totalBonus = totalBonus;
        this.totalDeduction = totalDeduction;
        this.totalInsurancePayment = totalInsurancePayment;
        this.totalTaxPayment = totalTaxPayment;
        this.salaryContractId = salaryContractId;
        this.salaryStatusId = salaryStatusId;
        this.isRemind = isRemind;
        this.approver = approver;
        this.duration = duration;
        this.comment = comment;
        this.totalAllowance = totalAllowance;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.salary_id</code>.
     */
    public Long getSalaryId() {
        return this.salaryId;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.salary_id</code>.
     */
    public SalaryMonthly setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.actual_point</code>.
     */
    public Double getActualPoint() {
        return this.actualPoint;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.actual_point</code>.
     */
    public SalaryMonthly setActualPoint(Double actualPoint) {
        this.actualPoint = actualPoint;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.end_date</code>.
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.end_date</code>.
     */
    public SalaryMonthly setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.final_salary</code>.
     */
    public BigDecimal getFinalSalary() {
        return this.finalSalary;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.final_salary</code>.
     */
    public SalaryMonthly setFinalSalary(BigDecimal finalSalary) {
        this.finalSalary = finalSalary;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.ot_point</code>.
     */
    public Double getOtPoint() {
        return this.otPoint;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.ot_point</code>.
     */
    public SalaryMonthly setOtPoint(Double otPoint) {
        this.otPoint = otPoint;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.standard_point</code>.
     */
    public Double getStandardPoint() {
        return this.standardPoint;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.standard_point</code>.
     */
    public SalaryMonthly setStandardPoint(Double standardPoint) {
        this.standardPoint = standardPoint;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.start_date</code>.
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.start_date</code>.
     */
    public SalaryMonthly setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.total_advance</code>.
     */
    public BigDecimal getTotalAdvance() {
        return this.totalAdvance;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.total_advance</code>.
     */
    public SalaryMonthly setTotalAdvance(BigDecimal totalAdvance) {
        this.totalAdvance = totalAdvance;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.total_bonus</code>.
     */
    public BigDecimal getTotalBonus() {
        return this.totalBonus;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.total_bonus</code>.
     */
    public SalaryMonthly setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.total_deduction</code>.
     */
    public BigDecimal getTotalDeduction() {
        return this.totalDeduction;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.total_deduction</code>.
     */
    public SalaryMonthly setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.total_insurance_payment</code>.
     */
    public BigDecimal getTotalInsurancePayment() {
        return this.totalInsurancePayment;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.total_insurance_payment</code>.
     */
    public SalaryMonthly setTotalInsurancePayment(BigDecimal totalInsurancePayment) {
        this.totalInsurancePayment = totalInsurancePayment;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.total_tax_payment</code>.
     */
    public BigDecimal getTotalTaxPayment() {
        return this.totalTaxPayment;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.total_tax_payment</code>.
     */
    public SalaryMonthly setTotalTaxPayment(BigDecimal totalTaxPayment) {
        this.totalTaxPayment = totalTaxPayment;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.salary_contract_id</code>.
     */
    public Long getSalaryContractId() {
        return this.salaryContractId;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.salary_contract_id</code>.
     */
    public SalaryMonthly setSalaryContractId(Long salaryContractId) {
        this.salaryContractId = salaryContractId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.salary_status_id</code>.
     */
    public Long getSalaryStatusId() {
        return this.salaryStatusId;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.salary_status_id</code>.
     */
    public SalaryMonthly setSalaryStatusId(Long salaryStatusId) {
        this.salaryStatusId = salaryStatusId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.is_remind</code>.
     */
    public Boolean getIsRemind() {
        return this.isRemind;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.is_remind</code>.
     */
    public SalaryMonthly setIsRemind(Boolean isRemind) {
        this.isRemind = isRemind;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.approver</code>.
     */
    public String getApprover() {
        return this.approver;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.approver</code>.
     */
    public SalaryMonthly setApprover(String approver) {
        this.approver = approver;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.duration</code>.
     */
    public LocalDate getDuration() {
        return this.duration;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.duration</code>.
     */
    public SalaryMonthly setDuration(LocalDate duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.salary_monthly.comment</code>.
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Setter for <code>human_resource_management.salary_monthly.comment</code>.
     */
    public SalaryMonthly setComment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.salary_monthly.total_allowance</code>.
     */
    public BigDecimal getTotalAllowance() {
        return this.totalAllowance;
    }

    /**
     * Setter for
     * <code>human_resource_management.salary_monthly.total_allowance</code>.
     */
    public SalaryMonthly setTotalAllowance(BigDecimal totalAllowance) {
        this.totalAllowance = totalAllowance;
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
        final SalaryMonthly other = (SalaryMonthly) obj;
        if (this.salaryId == null) {
            if (other.salaryId != null)
                return false;
        }
        else if (!this.salaryId.equals(other.salaryId))
            return false;
        if (this.actualPoint == null) {
            if (other.actualPoint != null)
                return false;
        }
        else if (!this.actualPoint.equals(other.actualPoint))
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
        if (this.otPoint == null) {
            if (other.otPoint != null)
                return false;
        }
        else if (!this.otPoint.equals(other.otPoint))
            return false;
        if (this.standardPoint == null) {
            if (other.standardPoint != null)
                return false;
        }
        else if (!this.standardPoint.equals(other.standardPoint))
            return false;
        if (this.startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!this.startDate.equals(other.startDate))
            return false;
        if (this.totalAdvance == null) {
            if (other.totalAdvance != null)
                return false;
        }
        else if (!this.totalAdvance.equals(other.totalAdvance))
            return false;
        if (this.totalBonus == null) {
            if (other.totalBonus != null)
                return false;
        }
        else if (!this.totalBonus.equals(other.totalBonus))
            return false;
        if (this.totalDeduction == null) {
            if (other.totalDeduction != null)
                return false;
        }
        else if (!this.totalDeduction.equals(other.totalDeduction))
            return false;
        if (this.totalInsurancePayment == null) {
            if (other.totalInsurancePayment != null)
                return false;
        }
        else if (!this.totalInsurancePayment.equals(other.totalInsurancePayment))
            return false;
        if (this.totalTaxPayment == null) {
            if (other.totalTaxPayment != null)
                return false;
        }
        else if (!this.totalTaxPayment.equals(other.totalTaxPayment))
            return false;
        if (this.salaryContractId == null) {
            if (other.salaryContractId != null)
                return false;
        }
        else if (!this.salaryContractId.equals(other.salaryContractId))
            return false;
        if (this.salaryStatusId == null) {
            if (other.salaryStatusId != null)
                return false;
        }
        else if (!this.salaryStatusId.equals(other.salaryStatusId))
            return false;
        if (this.isRemind == null) {
            if (other.isRemind != null)
                return false;
        }
        else if (!this.isRemind.equals(other.isRemind))
            return false;
        if (this.approver == null) {
            if (other.approver != null)
                return false;
        }
        else if (!this.approver.equals(other.approver))
            return false;
        if (this.duration == null) {
            if (other.duration != null)
                return false;
        }
        else if (!this.duration.equals(other.duration))
            return false;
        if (this.comment == null) {
            if (other.comment != null)
                return false;
        }
        else if (!this.comment.equals(other.comment))
            return false;
        if (this.totalAllowance == null) {
            if (other.totalAllowance != null)
                return false;
        }
        else if (!this.totalAllowance.equals(other.totalAllowance))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.salaryId == null) ? 0 : this.salaryId.hashCode());
        result = prime * result + ((this.actualPoint == null) ? 0 : this.actualPoint.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.finalSalary == null) ? 0 : this.finalSalary.hashCode());
        result = prime * result + ((this.otPoint == null) ? 0 : this.otPoint.hashCode());
        result = prime * result + ((this.standardPoint == null) ? 0 : this.standardPoint.hashCode());
        result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
        result = prime * result + ((this.totalAdvance == null) ? 0 : this.totalAdvance.hashCode());
        result = prime * result + ((this.totalBonus == null) ? 0 : this.totalBonus.hashCode());
        result = prime * result + ((this.totalDeduction == null) ? 0 : this.totalDeduction.hashCode());
        result = prime * result + ((this.totalInsurancePayment == null) ? 0 : this.totalInsurancePayment.hashCode());
        result = prime * result + ((this.totalTaxPayment == null) ? 0 : this.totalTaxPayment.hashCode());
        result = prime * result + ((this.salaryContractId == null) ? 0 : this.salaryContractId.hashCode());
        result = prime * result + ((this.salaryStatusId == null) ? 0 : this.salaryStatusId.hashCode());
        result = prime * result + ((this.isRemind == null) ? 0 : this.isRemind.hashCode());
        result = prime * result + ((this.approver == null) ? 0 : this.approver.hashCode());
        result = prime * result + ((this.duration == null) ? 0 : this.duration.hashCode());
        result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
        result = prime * result + ((this.totalAllowance == null) ? 0 : this.totalAllowance.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SalaryMonthly (");

        sb.append(salaryId);
        sb.append(", ").append(actualPoint);
        sb.append(", ").append(endDate);
        sb.append(", ").append(finalSalary);
        sb.append(", ").append(otPoint);
        sb.append(", ").append(standardPoint);
        sb.append(", ").append(startDate);
        sb.append(", ").append(totalAdvance);
        sb.append(", ").append(totalBonus);
        sb.append(", ").append(totalDeduction);
        sb.append(", ").append(totalInsurancePayment);
        sb.append(", ").append(totalTaxPayment);
        sb.append(", ").append(salaryContractId);
        sb.append(", ").append(salaryStatusId);
        sb.append(", ").append(isRemind);
        sb.append(", ").append(approver);
        sb.append(", ").append(duration);
        sb.append(", ").append(comment);
        sb.append(", ").append(totalAllowance);

        sb.append(")");
        return sb.toString();
    }
}
