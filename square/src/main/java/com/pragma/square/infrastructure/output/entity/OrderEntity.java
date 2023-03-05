package com.pragma.square.infrastructure.output.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idClient;
    private Date orderDate;
    private String status;
    private Long idChef;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="plates_id")
    private List<PlateEntity> plates;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_restaurant")
    private RestaurantEntity idRestaurant;

//    @OneToMany(mappedBy = "idOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<OrderPlateEntity> orderPlate;

}