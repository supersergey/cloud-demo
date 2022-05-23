package io.kadmos.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record BalanceDto(
    BigDecimal amount
) {
}
