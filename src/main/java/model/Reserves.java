package model;

import Client.Client;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reserve")
@Relation(value = "reserve", collectionRelation = "reserves")
public class Reserves implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserve_sequence")
    @SequenceGenerator(name = "reserve_sequence", initialValue = 1)
    // Reserve Information
    @Column(name = "id")
    private Long id;
    @Column(name = "employer_id")
    private Long employerId;
    @Column(name = "preferred_branch_office")
    private String preferredBranchOffice;
    @Column(name = "is_admin")
    private boolean isAdmin;

    // Barber Information
    private Barber barber;

    // Client Information
    @Column(name = "user_id")
    private Client client;



}
