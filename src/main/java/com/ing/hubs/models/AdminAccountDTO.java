package com.ing.hubs.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccountDTO {

    private Integer id;
    private String currency;
    private Double balance;
    private Integer userId;
}