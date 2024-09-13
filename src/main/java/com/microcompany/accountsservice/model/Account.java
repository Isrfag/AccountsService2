package com.microcompany.accountsservice.model;

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

    @NotBlank(message = "No puede quedar en blanco")
    @NotNull
    @Size(min = 5, max = 20, message = "Debe ser entre 5 y 20 caracteres")
    private String type;

    @DateTimeFormat
    @NotNull
    LocalDate openingDate;

    @Min(0)
    private int balance;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "owner_id")
    Customer owner;

     public void isValid() throws Exception {
        if (type == null || openingDate == null)
            throw new Exception("Cuenta no válida");
    }

}
