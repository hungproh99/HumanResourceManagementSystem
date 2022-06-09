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
public class IdentityCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String    cardId;
    private LocalDate dateOfExpiry;
    private String    nationality;
    private String    placeOfOrigin;
    private String    placeOfResidence;
    private LocalDate provideDate;
    private String    providePlace;

    public IdentityCard() {}

    public IdentityCard(IdentityCard value) {
        this.cardId = value.cardId;
        this.dateOfExpiry = value.dateOfExpiry;
        this.nationality = value.nationality;
        this.placeOfOrigin = value.placeOfOrigin;
        this.placeOfResidence = value.placeOfResidence;
        this.provideDate = value.provideDate;
        this.providePlace = value.providePlace;
    }

    public IdentityCard(
        String    cardId,
        LocalDate dateOfExpiry,
        String    nationality,
        String    placeOfOrigin,
        String    placeOfResidence,
        LocalDate provideDate,
        String    providePlace
    ) {
        this.cardId = cardId;
        this.dateOfExpiry = dateOfExpiry;
        this.nationality = nationality;
        this.placeOfOrigin = placeOfOrigin;
        this.placeOfResidence = placeOfResidence;
        this.provideDate = provideDate;
        this.providePlace = providePlace;
    }

    /**
     * Getter for <code>human_resource_management.identity_card.card_id</code>.
     */
    public String getCardId() {
        return this.cardId;
    }

    /**
     * Setter for <code>human_resource_management.identity_card.card_id</code>.
     */
    public IdentityCard setCardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.identity_card.date_of_expiry</code>.
     */
    public LocalDate getDateOfExpiry() {
        return this.dateOfExpiry;
    }

    /**
     * Setter for
     * <code>human_resource_management.identity_card.date_of_expiry</code>.
     */
    public IdentityCard setDateOfExpiry(LocalDate dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.identity_card.nationality</code>.
     */
    public String getNationality() {
        return this.nationality;
    }

    /**
     * Setter for
     * <code>human_resource_management.identity_card.nationality</code>.
     */
    public IdentityCard setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.identity_card.place_of_origin</code>.
     */
    public String getPlaceOfOrigin() {
        return this.placeOfOrigin;
    }

    /**
     * Setter for
     * <code>human_resource_management.identity_card.place_of_origin</code>.
     */
    public IdentityCard setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.identity_card.place_of_residence</code>.
     */
    public String getPlaceOfResidence() {
        return this.placeOfResidence;
    }

    /**
     * Setter for
     * <code>human_resource_management.identity_card.place_of_residence</code>.
     */
    public IdentityCard setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.identity_card.provide_date</code>.
     */
    public LocalDate getProvideDate() {
        return this.provideDate;
    }

    /**
     * Setter for
     * <code>human_resource_management.identity_card.provide_date</code>.
     */
    public IdentityCard setProvideDate(LocalDate provideDate) {
        this.provideDate = provideDate;
        return this;
    }

    /**
     * Getter for
     * <code>human_resource_management.identity_card.provide_place</code>.
     */
    public String getProvidePlace() {
        return this.providePlace;
    }

    /**
     * Setter for
     * <code>human_resource_management.identity_card.provide_place</code>.
     */
    public IdentityCard setProvidePlace(String providePlace) {
        this.providePlace = providePlace;
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
        final IdentityCard other = (IdentityCard) obj;
        if (this.cardId == null) {
            if (other.cardId != null)
                return false;
        }
        else if (!this.cardId.equals(other.cardId))
            return false;
        if (this.dateOfExpiry == null) {
            if (other.dateOfExpiry != null)
                return false;
        }
        else if (!this.dateOfExpiry.equals(other.dateOfExpiry))
            return false;
        if (this.nationality == null) {
            if (other.nationality != null)
                return false;
        }
        else if (!this.nationality.equals(other.nationality))
            return false;
        if (this.placeOfOrigin == null) {
            if (other.placeOfOrigin != null)
                return false;
        }
        else if (!this.placeOfOrigin.equals(other.placeOfOrigin))
            return false;
        if (this.placeOfResidence == null) {
            if (other.placeOfResidence != null)
                return false;
        }
        else if (!this.placeOfResidence.equals(other.placeOfResidence))
            return false;
        if (this.provideDate == null) {
            if (other.provideDate != null)
                return false;
        }
        else if (!this.provideDate.equals(other.provideDate))
            return false;
        if (this.providePlace == null) {
            if (other.providePlace != null)
                return false;
        }
        else if (!this.providePlace.equals(other.providePlace))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.cardId == null) ? 0 : this.cardId.hashCode());
        result = prime * result + ((this.dateOfExpiry == null) ? 0 : this.dateOfExpiry.hashCode());
        result = prime * result + ((this.nationality == null) ? 0 : this.nationality.hashCode());
        result = prime * result + ((this.placeOfOrigin == null) ? 0 : this.placeOfOrigin.hashCode());
        result = prime * result + ((this.placeOfResidence == null) ? 0 : this.placeOfResidence.hashCode());
        result = prime * result + ((this.provideDate == null) ? 0 : this.provideDate.hashCode());
        result = prime * result + ((this.providePlace == null) ? 0 : this.providePlace.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IdentityCard (");

        sb.append(cardId);
        sb.append(", ").append(dateOfExpiry);
        sb.append(", ").append(nationality);
        sb.append(", ").append(placeOfOrigin);
        sb.append(", ").append(placeOfResidence);
        sb.append(", ").append(provideDate);
        sb.append(", ").append(providePlace);

        sb.append(")");
        return sb.toString();
    }
}