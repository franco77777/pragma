package com.pragma.square.infrastructure.output.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;
    private String description;
   @OneToMany(mappedBy = "idCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JsonIgnore
    private Set<PlateEntity> plate= new HashSet<>();
}
