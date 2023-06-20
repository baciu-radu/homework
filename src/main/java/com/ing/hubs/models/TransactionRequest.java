package com.ing.hubs.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private Double amount;
    private TransactionType type;
    private Integer initializerAccountId;
    private Integer targetAccountId;


}
