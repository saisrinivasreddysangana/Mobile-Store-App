package org.mobilestoreapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "SMARTRONIX")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Mobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile brand ")
    private String brand;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile model ")
    private String model;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile osType")
    private String osType;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile storage")
    private String storage;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile price")
    private long price;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile colour")
    private String colour;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile image")
    private String image;


}
