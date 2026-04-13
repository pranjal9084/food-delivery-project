package com.SnackStack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.events.Event;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private IngredientCategory category;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private boolean inStock = true;

}
