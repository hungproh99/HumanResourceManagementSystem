package com.csproject.hrm.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "identity_card")
public class IdentityCard {
    @Id
    @Column(name = "card_id")
    private String id;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "place_of_origin")
    private String placeOfOrigin;

    @Column(name = "place_of_residence")
    private String placeOfResidence;

    @Column(name = "date_of_expiry")
    private LocalDate dateOfExpiry;

    @Column(name = "provide_date")
    private LocalDate provideDate;

    @Column(name = "provide_place")
    private String providePlace;

    @OneToOne(mappedBy = "identityCard", fetch = FetchType.LAZY)
    private Employee employee;
}
