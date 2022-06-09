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
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long      educationId;
    private String    certificate;
    private LocalDate endDate;
    private String    nameSchool;
    private LocalDate startDate;
    private String    status;
    private String    employeeId;

    public Education() {}

    public Education(Education value) {
        this.educationId = value.educationId;
        this.certificate = value.certificate;
        this.endDate = value.endDate;
        this.nameSchool = value.nameSchool;
        this.startDate = value.startDate;
        this.status = value.status;
        this.employeeId = value.employeeId;
    }

    public Education(
        Long      educationId,
        String    certificate,
        LocalDate endDate,
        String    nameSchool,
        LocalDate startDate,
        String    status,
        String    employeeId
    ) {
        this.educationId = educationId;
        this.certificate = certificate;
        this.endDate = endDate;
        this.nameSchool = nameSchool;
        this.startDate = startDate;
        this.status = status;
        this.employeeId = employeeId;
    }

    /**
     * Getter for <code>human_resource_management.education.education_id</code>.
     */
    public Long getEducationId() {
        return this.educationId;
    }

    /**
     * Setter for <code>human_resource_management.education.education_id</code>.
     */
    public Education setEducationId(Long educationId) {
        this.educationId = educationId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.education.certificate</code>.
     */
    public String getCertificate() {
        return this.certificate;
    }

    /**
     * Setter for <code>human_resource_management.education.certificate</code>.
     */
    public Education setCertificate(String certificate) {
        this.certificate = certificate;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.education.end_date</code>.
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**
     * Setter for <code>human_resource_management.education.end_date</code>.
     */
    public Education setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.education.name_school</code>.
     */
    public String getNameSchool() {
        return this.nameSchool;
    }

    /**
     * Setter for <code>human_resource_management.education.name_school</code>.
     */
    public Education setNameSchool(String nameSchool) {
        this.nameSchool = nameSchool;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.education.start_date</code>.
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * Setter for <code>human_resource_management.education.start_date</code>.
     */
    public Education setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.education.status</code>.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>human_resource_management.education.status</code>.
     */
    public Education setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.education.employee_id</code>.
     */
    public String getEmployeeId() {
        return this.employeeId;
    }

    /**
     * Setter for <code>human_resource_management.education.employee_id</code>.
     */
    public Education setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
        final Education other = (Education) obj;
        if (this.educationId == null) {
            if (other.educationId != null)
                return false;
        }
        else if (!this.educationId.equals(other.educationId))
            return false;
        if (this.certificate == null) {
            if (other.certificate != null)
                return false;
        }
        else if (!this.certificate.equals(other.certificate))
            return false;
        if (this.endDate == null) {
            if (other.endDate != null)
                return false;
        }
        else if (!this.endDate.equals(other.endDate))
            return false;
        if (this.nameSchool == null) {
            if (other.nameSchool != null)
                return false;
        }
        else if (!this.nameSchool.equals(other.nameSchool))
            return false;
        if (this.startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!this.startDate.equals(other.startDate))
            return false;
        if (this.status == null) {
            if (other.status != null)
                return false;
        }
        else if (!this.status.equals(other.status))
            return false;
        if (this.employeeId == null) {
            if (other.employeeId != null)
                return false;
        }
        else if (!this.employeeId.equals(other.employeeId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.educationId == null) ? 0 : this.educationId.hashCode());
        result = prime * result + ((this.certificate == null) ? 0 : this.certificate.hashCode());
        result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
        result = prime * result + ((this.nameSchool == null) ? 0 : this.nameSchool.hashCode());
        result = prime * result + ((this.startDate == null) ? 0 : this.startDate.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        result = prime * result + ((this.employeeId == null) ? 0 : this.employeeId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Education (");

        sb.append(educationId);
        sb.append(", ").append(certificate);
        sb.append(", ").append(endDate);
        sb.append(", ").append(nameSchool);
        sb.append(", ").append(startDate);
        sb.append(", ").append(status);
        sb.append(", ").append(employeeId);

        sb.append(")");
        return sb.toString();
    }
}