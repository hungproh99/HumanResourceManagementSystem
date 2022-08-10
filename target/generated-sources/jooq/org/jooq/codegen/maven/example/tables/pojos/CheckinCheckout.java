/*
 * This file is generated by jOOQ.
 */
package org.jooq.codegen.maven.example.tables.pojos;


import java.io.Serializable;
import java.time.LocalTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CheckinCheckout implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long      checkinCheckoutId;
    private LocalTime checkin;
    private LocalTime checkout;
    private Long      timekeepingId;

    public CheckinCheckout() {}

    public CheckinCheckout(CheckinCheckout value) {
        this.checkinCheckoutId = value.checkinCheckoutId;
        this.checkin = value.checkin;
        this.checkout = value.checkout;
        this.timekeepingId = value.timekeepingId;
    }

    public CheckinCheckout(
        Long      checkinCheckoutId,
        LocalTime checkin,
        LocalTime checkout,
        Long      timekeepingId
    ) {
        this.checkinCheckoutId = checkinCheckoutId;
        this.checkin = checkin;
        this.checkout = checkout;
        this.timekeepingId = timekeepingId;
    }

    /**
     * Getter for
     * <code>human_resource_management.checkin_checkout.checkin_checkout_id</code>.
     */
    public Long getCheckinCheckoutId() {
        return this.checkinCheckoutId;
    }

    /**
     * Setter for
     * <code>human_resource_management.checkin_checkout.checkin_checkout_id</code>.
     */
    public CheckinCheckout setCheckinCheckoutId(Long checkinCheckoutId) {
        this.checkinCheckoutId = checkinCheckoutId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.checkin_checkout.checkin</code>.
     */
    public LocalTime getCheckin() {
        return this.checkin;
    }

    /**
     * Setter for
     * <code>human_resource_management.checkin_checkout.checkin</code>.
     */
    public CheckinCheckout setCheckin(LocalTime checkin) {
        this.checkin = checkin;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.checkin_checkout.checkout</code>.
     */
    public LocalTime getCheckout() {
        return this.checkout;
    }

    /**
     * Setter for
     * <code>human_resource_management.checkin_checkout.checkout</code>.
     */
    public CheckinCheckout setCheckout(LocalTime checkout) {
        this.checkout = checkout;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.checkin_checkout.timekeeping_id</code>.
     */
    public Long getTimekeepingId() {
        return this.timekeepingId;
    }

    /**
     * Setter for
     * <code>human_resource_management.checkin_checkout.timekeeping_id</code>.
     */
    public CheckinCheckout setTimekeepingId(Long timekeepingId) {
        this.timekeepingId = timekeepingId;
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
        final CheckinCheckout other = (CheckinCheckout) obj;
        if (this.checkinCheckoutId == null) {
            if (other.checkinCheckoutId != null)
                return false;
        }
        else if (!this.checkinCheckoutId.equals(other.checkinCheckoutId))
            return false;
        if (this.checkin == null) {
            if (other.checkin != null)
                return false;
        }
        else if (!this.checkin.equals(other.checkin))
            return false;
        if (this.checkout == null) {
            if (other.checkout != null)
                return false;
        }
        else if (!this.checkout.equals(other.checkout))
            return false;
        if (this.timekeepingId == null) {
            if (other.timekeepingId != null)
                return false;
        }
        else if (!this.timekeepingId.equals(other.timekeepingId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.checkinCheckoutId == null) ? 0 : this.checkinCheckoutId.hashCode());
        result = prime * result + ((this.checkin == null) ? 0 : this.checkin.hashCode());
        result = prime * result + ((this.checkout == null) ? 0 : this.checkout.hashCode());
        result = prime * result + ((this.timekeepingId == null) ? 0 : this.timekeepingId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CheckinCheckout (");

        sb.append(checkinCheckoutId);
        sb.append(", ").append(checkin);
        sb.append(", ").append(checkout);
        sb.append(", ").append(timekeepingId);

        sb.append(")");
        return sb.toString();
    }
}
