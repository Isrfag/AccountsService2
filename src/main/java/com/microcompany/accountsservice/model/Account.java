package com.microcompany.accountsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    LocalDate openingDate;

    private int balance;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} ,fetch=FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false, updatable = false)
    @NotNull
    @JsonIgnore
    Customer owner;

    @Column(name="owner_id", insertable = false, updatable = false)
    private Long ownerId;


}
