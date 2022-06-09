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
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String    employeeId;
    private String    address;
    private String    avatar;
    private LocalDate birthDate;
    private String    companyEmail;
    private String    currentSituation;
    private String    facebook;
    private String    fullName;
    private String    gender;
    private String    managerId;
    private String    maritalStatus;
    private String    nickName;
    private String    password;
    private String    personalEmail;
    private String    phoneNumber;
    private String    taxCode;
    private Boolean   workingStatus;
    private Long      bankId;
    private Long      employeeTypeId;
    private String    cardId;
    private Long      roleType;
    private Long      workingTypeId;

    public Employee() {}

    public Employee(Employee value) {
        this.employeeId = value.employeeId;
        this.address = value.address;
        this.avatar = value.avatar;
        this.birthDate = value.birthDate;
        this.companyEmail = value.companyEmail;
        this.currentSituation = value.currentSituation;
        this.facebook = value.facebook;
        this.fullName = value.fullName;
        this.gender = value.gender;
        this.managerId = value.managerId;
        this.maritalStatus = value.maritalStatus;
        this.nickName = value.nickName;
        this.password = value.password;
        this.personalEmail = value.personalEmail;
        this.phoneNumber = value.phoneNumber;
        this.taxCode = value.taxCode;
        this.workingStatus = value.workingStatus;
        this.bankId = value.bankId;
        this.employeeTypeId = value.employeeTypeId;
        this.cardId = value.cardId;
        this.roleType = value.roleType;
        this.workingTypeId = value.workingTypeId;
    }

    public Employee(
        String    employeeId,
        String    address,
        String    avatar,
        LocalDate birthDate,
        String    companyEmail,
        String    currentSituation,
        String    facebook,
        String    fullName,
        String    gender,
        String    managerId,
        String    maritalStatus,
        String    nickName,
        String    password,
        String    personalEmail,
        String    phoneNumber,
        String    taxCode,
        Boolean   workingStatus,
        Long      bankId,
        Long      employeeTypeId,
        String    cardId,
        Long      roleType,
        Long      workingTypeId
    ) {
        this.employeeId = employeeId;
        this.address = address;
        this.avatar = avatar;
        this.birthDate = birthDate;
        this.companyEmail = companyEmail;
        this.currentSituation = currentSituation;
        this.facebook = facebook;
        this.fullName = fullName;
        this.gender = gender;
        this.managerId = managerId;
        this.maritalStatus = maritalStatus;
        this.nickName = nickName;
        this.password = password;
        this.personalEmail = personalEmail;
        this.phoneNumber = phoneNumber;
        this.taxCode = taxCode;
        this.workingStatus = workingStatus;
        this.bankId = bankId;
        this.employeeTypeId = employeeTypeId;
        this.cardId = cardId;
        this.roleType = roleType;
        this.workingTypeId = workingTypeId;
    }

    /**
     * Getter for <code>human_resource_management.employee.employee_id</code>.
     */
    public String getEmployeeId() {
        return this.employeeId;
    }

    /**
     * Setter for <code>human_resource_management.employee.employee_id</code>.
     */
    public Employee setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.address</code>.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Setter for <code>human_resource_management.employee.address</code>.
     */
    public Employee setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.avatar</code>.
     */
    public String getAvatar() {
        return this.avatar;
    }

    /**
     * Setter for <code>human_resource_management.employee.avatar</code>.
     */
    public Employee setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.birth_date</code>.
     */
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    /**
     * Setter for <code>human_resource_management.employee.birth_date</code>.
     */
    public Employee setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.company_email</code>.
     */
    public String getCompanyEmail() {
        return this.companyEmail;
    }

    /**
     * Setter for <code>human_resource_management.employee.company_email</code>.
     */
    public Employee setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.employee.current_situation</code>.
     */
    public String getCurrentSituation() {
        return this.currentSituation;
    }

    /**
     * Setter for
     * <code>human_resource_management.employee.current_situation</code>.
     */
    public Employee setCurrentSituation(String currentSituation) {
        this.currentSituation = currentSituation;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.facebook</code>.
     */
    public String getFacebook() {
        return this.facebook;
    }

    /**
     * Setter for <code>human_resource_management.employee.facebook</code>.
     */
    public Employee setFacebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.full_name</code>.
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * Setter for <code>human_resource_management.employee.full_name</code>.
     */
    public Employee setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.gender</code>.
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Setter for <code>human_resource_management.employee.gender</code>.
     */
    public Employee setGender(String gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.manager_id</code>.
     */
    public String getManagerId() {
        return this.managerId;
    }

    /**
     * Setter for <code>human_resource_management.employee.manager_id</code>.
     */
    public Employee setManagerId(String managerId) {
        this.managerId = managerId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.employee.marital_status</code>.
     */
    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    /**
     * Setter for
     * <code>human_resource_management.employee.marital_status</code>.
     */
    public Employee setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.nick_name</code>.
     */
    public String getNickName() {
        return this.nickName;
    }

    /**
     * Setter for <code>human_resource_management.employee.nick_name</code>.
     */
    public Employee setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.password</code>.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Setter for <code>human_resource_management.employee.password</code>.
     */
    public Employee setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.employee.personal_email</code>.
     */
    public String getPersonalEmail() {
        return this.personalEmail;
    }

    /**
     * Setter for
     * <code>human_resource_management.employee.personal_email</code>.
     */
    public Employee setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.phone_number</code>.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Setter for <code>human_resource_management.employee.phone_number</code>.
     */
    public Employee setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.tax_code</code>.
     */
    public String getTaxCode() {
        return this.taxCode;
    }

    /**
     * Setter for <code>human_resource_management.employee.tax_code</code>.
     */
    public Employee setTaxCode(String taxCode) {
        this.taxCode = taxCode;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.employee.working_status</code>.
     */
    public Boolean getWorkingStatus() {
        return this.workingStatus;
    }

    /**
     * Setter for
     * <code>human_resource_management.employee.working_status</code>.
     */
    public Employee setWorkingStatus(Boolean workingStatus) {
        this.workingStatus = workingStatus;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.bank_id</code>.
     */
    public Long getBankId() {
        return this.bankId;
    }

    /**
     * Setter for <code>human_resource_management.employee.bank_id</code>.
     */
    public Employee setBankId(Long bankId) {
        this.bankId = bankId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.employee.employee_type_id</code>.
     */
    public Long getEmployeeTypeId() {
        return this.employeeTypeId;
    }

    /**
     * Setter for
     * <code>human_resource_management.employee.employee_type_id</code>.
     */
    public Employee setEmployeeTypeId(Long employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.card_id</code>.
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * Setter for <code>human_resource_management.employee.card_id</code>.
     */
    public Employee setCardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    /**
     * Getter for <code>human_resource_management.employee.role_type</code>.
     */
    public Long getRoleType() {
        return this.roleType;
    }

    /**
     * Setter for <code>human_resource_management.employee.role_type</code>.
     */
    public Employee setRoleType(Long roleType) {
        this.roleType = roleType;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.employee.working_type_id</code>.
     */
    public Long getWorkingTypeId() {
        return this.workingTypeId;
    }

    /**
     * Setter for
     * <code>human_resource_management.employee.working_type_id</code>.
     */
    public Employee setWorkingTypeId(Long workingTypeId) {
        this.workingTypeId = workingTypeId;
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
        final Employee other = (Employee) obj;
        if (this.employeeId == null) {
            if (other.employeeId != null)
                return false;
        }
        else if (!this.employeeId.equals(other.employeeId))
            return false;
        if (this.address == null) {
            if (other.address != null)
                return false;
        }
        else if (!this.address.equals(other.address))
            return false;
        if (this.avatar == null) {
            if (other.avatar != null)
                return false;
        }
        else if (!this.avatar.equals(other.avatar))
            return false;
        if (this.birthDate == null) {
            if (other.birthDate != null)
                return false;
        }
        else if (!this.birthDate.equals(other.birthDate))
            return false;
        if (this.companyEmail == null) {
            if (other.companyEmail != null)
                return false;
        }
        else if (!this.companyEmail.equals(other.companyEmail))
            return false;
        if (this.currentSituation == null) {
            if (other.currentSituation != null)
                return false;
        }
        else if (!this.currentSituation.equals(other.currentSituation))
            return false;
        if (this.facebook == null) {
            if (other.facebook != null)
                return false;
        }
        else if (!this.facebook.equals(other.facebook))
            return false;
        if (this.fullName == null) {
            if (other.fullName != null)
                return false;
        }
        else if (!this.fullName.equals(other.fullName))
            return false;
        if (this.gender == null) {
            if (other.gender != null)
                return false;
        }
        else if (!this.gender.equals(other.gender))
            return false;
        if (this.managerId == null) {
            if (other.managerId != null)
                return false;
        }
        else if (!this.managerId.equals(other.managerId))
            return false;
        if (this.maritalStatus == null) {
            if (other.maritalStatus != null)
                return false;
        }
        else if (!this.maritalStatus.equals(other.maritalStatus))
            return false;
        if (this.nickName == null) {
            if (other.nickName != null)
                return false;
        }
        else if (!this.nickName.equals(other.nickName))
            return false;
        if (this.password == null) {
            if (other.password != null)
                return false;
        }
        else if (!this.password.equals(other.password))
            return false;
        if (this.personalEmail == null) {
            if (other.personalEmail != null)
                return false;
        }
        else if (!this.personalEmail.equals(other.personalEmail))
            return false;
        if (this.phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        }
        else if (!this.phoneNumber.equals(other.phoneNumber))
            return false;
        if (this.taxCode == null) {
            if (other.taxCode != null)
                return false;
        }
        else if (!this.taxCode.equals(other.taxCode))
            return false;
        if (this.workingStatus == null) {
            if (other.workingStatus != null)
                return false;
        }
        else if (!this.workingStatus.equals(other.workingStatus))
            return false;
        if (this.bankId == null) {
            if (other.bankId != null)
                return false;
        }
        else if (!this.bankId.equals(other.bankId))
            return false;
        if (this.employeeTypeId == null) {
            if (other.employeeTypeId != null)
                return false;
        }
        else if (!this.employeeTypeId.equals(other.employeeTypeId))
            return false;
        if (this.cardId == null) {
            if (other.cardId != null)
                return false;
        }
        else if (!this.cardId.equals(other.cardId))
            return false;
        if (this.roleType == null) {
            if (other.roleType != null)
                return false;
        }
        else if (!this.roleType.equals(other.roleType))
            return false;
        if (this.workingTypeId == null) {
            if (other.workingTypeId != null)
                return false;
        }
        else if (!this.workingTypeId.equals(other.workingTypeId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.employeeId == null) ? 0 : this.employeeId.hashCode());
        result = prime * result + ((this.address == null) ? 0 : this.address.hashCode());
        result = prime * result + ((this.avatar == null) ? 0 : this.avatar.hashCode());
        result = prime * result + ((this.birthDate == null) ? 0 : this.birthDate.hashCode());
        result = prime * result + ((this.companyEmail == null) ? 0 : this.companyEmail.hashCode());
        result = prime * result + ((this.currentSituation == null) ? 0 : this.currentSituation.hashCode());
        result = prime * result + ((this.facebook == null) ? 0 : this.facebook.hashCode());
        result = prime * result + ((this.fullName == null) ? 0 : this.fullName.hashCode());
        result = prime * result + ((this.gender == null) ? 0 : this.gender.hashCode());
        result = prime * result + ((this.managerId == null) ? 0 : this.managerId.hashCode());
        result = prime * result + ((this.maritalStatus == null) ? 0 : this.maritalStatus.hashCode());
        result = prime * result + ((this.nickName == null) ? 0 : this.nickName.hashCode());
        result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
        result = prime * result + ((this.personalEmail == null) ? 0 : this.personalEmail.hashCode());
        result = prime * result + ((this.phoneNumber == null) ? 0 : this.phoneNumber.hashCode());
        result = prime * result + ((this.taxCode == null) ? 0 : this.taxCode.hashCode());
        result = prime * result + ((this.workingStatus == null) ? 0 : this.workingStatus.hashCode());
        result = prime * result + ((this.bankId == null) ? 0 : this.bankId.hashCode());
        result = prime * result + ((this.employeeTypeId == null) ? 0 : this.employeeTypeId.hashCode());
        result = prime * result + ((this.cardId == null) ? 0 : this.cardId.hashCode());
        result = prime * result + ((this.roleType == null) ? 0 : this.roleType.hashCode());
        result = prime * result + ((this.workingTypeId == null) ? 0 : this.workingTypeId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Employee (");

        sb.append(employeeId);
        sb.append(", ").append(address);
        sb.append(", ").append(avatar);
        sb.append(", ").append(birthDate);
        sb.append(", ").append(companyEmail);
        sb.append(", ").append(currentSituation);
        sb.append(", ").append(facebook);
        sb.append(", ").append(fullName);
        sb.append(", ").append(gender);
        sb.append(", ").append(managerId);
        sb.append(", ").append(maritalStatus);
        sb.append(", ").append(nickName);
        sb.append(", ").append(password);
        sb.append(", ").append(personalEmail);
        sb.append(", ").append(phoneNumber);
        sb.append(", ").append(taxCode);
        sb.append(", ").append(workingStatus);
        sb.append(", ").append(bankId);
        sb.append(", ").append(employeeTypeId);
        sb.append(", ").append(cardId);
        sb.append(", ").append(roleType);
        sb.append(", ").append(workingTypeId);

        sb.append(")");
        return sb.toString();
    }
}