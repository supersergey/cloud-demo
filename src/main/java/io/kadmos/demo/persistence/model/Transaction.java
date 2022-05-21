package io.kadmos.demo.persistence.model;

import java.math.BigDecimal;
import java.util.UUID;

public final record Transaction(
    UUID accountId,
    BigDecimal transactionAmount
) {

}
