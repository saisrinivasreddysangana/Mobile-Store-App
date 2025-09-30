package org.mobilestoreapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "SMARTRONIX",
        indexes = {
                @Index(name = "idx_mobile_brand", columnList = "brand"),
                @Index(name = "idx_mobile_model", columnList = "model")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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
    @DecimalMin(value = "599.00", message = "Price must be at least 599")
    private BigDecimal price;

    @Column(nullable = false)
    @NotBlank(message = "Please provide mobile colour")
    private String colour;


}
