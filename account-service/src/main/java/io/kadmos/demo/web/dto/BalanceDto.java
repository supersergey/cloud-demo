package io.kadmos.demo.web.dto;

import java.math.BigDecimal;

public record BalanceDto(
    BigDecimal amount
) {
}
