package org.mobilestoreapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SMARTRONIX")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Mobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String osType;

    @Column(nullable = false)
    private String storage;

    @Column(nullable = false)
    private long price;

    @Column (nullable = true)
    private String colour;


}
