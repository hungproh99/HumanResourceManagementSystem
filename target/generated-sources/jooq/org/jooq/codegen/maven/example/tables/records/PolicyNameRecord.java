/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.codegen.maven.example.tables.PolicyName;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PolicyNameRecord extends UpdatableRecordImpl<PolicyNameRecord> implements Record3<Long, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>human_resource_management.policy_name.policy_name_id</code>.
     */
    public PolicyNameRecord setPolicyNameId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.policy_name.policy_name_id</code>.
     */
    public Long getPolicyNameId() {
        return (Long) get(0);
    }

    /**
     * Setter for
     * <code>human_resource_management.policy_name.policy_name</code>.
     */
    public PolicyNameRecord setPolicyName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.policy_name.policy_name</code>.
     */
    public String getPolicyName() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>human_resource_management.policy_name.policy_type_id</code>.
     */
    public PolicyNameRecord setPolicyTypeId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.policy_name.policy_type_id</code>.
     */
    public Long getPolicyTypeId() {
        return (Long) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, String, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Long, String, Long> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PolicyName.POLICY_NAME.POLICY_NAME_ID;
    }

    @Override
    public Field<String> field2() {
        return PolicyName.POLICY_NAME.POLICY_NAME_;
    }

    @Override
    public Field<Long> field3() {
        return PolicyName.POLICY_NAME.POLICY_TYPE_ID;
    }

    @Override
    public Long component1() {
        return getPolicyNameId();
    }

    @Override
    public String component2() {
        return getPolicyName();
    }

    @Override
    public Long component3() {
        return getPolicyTypeId();
    }

    @Override
    public Long value1() {
        return getPolicyNameId();
    }

    @Override
    public String value2() {
        return getPolicyName();
    }

    @Override
    public Long value3() {
        return getPolicyTypeId();
    }

    @Override
    public PolicyNameRecord value1(Long value) {
        setPolicyNameId(value);
        return this;
    }

    @Override
    public PolicyNameRecord value2(String value) {
        setPolicyName(value);
        return this;
    }

    @Override
    public PolicyNameRecord value3(Long value) {
        setPolicyTypeId(value);
        return this;
    }

    @Override
    public PolicyNameRecord values(Long value1, String value2, Long value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PolicyNameRecord
     */
    public PolicyNameRecord() {
        super(PolicyName.POLICY_NAME);
    }

    /**
     * Create a detached, initialised PolicyNameRecord
     */
    public PolicyNameRecord(Long policyNameId, String policyName, Long policyTypeId) {
        super(PolicyName.POLICY_NAME);

        setPolicyNameId(policyNameId);
        setPolicyName(policyName);
        setPolicyTypeId(policyTypeId);
    }

    /**
     * Create a detached, initialised PolicyNameRecord
     */
    public PolicyNameRecord(org.jooq.codegen.maven.example.tables.pojos.PolicyName value) {
        super(PolicyName.POLICY_NAME);

        if (value != null) {
            setPolicyNameId(value.getPolicyNameId());
            setPolicyName(value.getPolicyName());
            setPolicyTypeId(value.getPolicyTypeId());
        }
    }
}
