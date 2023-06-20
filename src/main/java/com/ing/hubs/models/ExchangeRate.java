package com.ing.hubs.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "exchange_rates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(nullable = false)
    private String inputCurrency;
    @Column(nullable = false)
    private String outputCurrency;
    @Column(nullable = false)
    private double rate;
}

