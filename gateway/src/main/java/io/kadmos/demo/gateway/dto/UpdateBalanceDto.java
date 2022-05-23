package io.kadmos.demo.gateway.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateBalanceDto(UUID accountId, BigDecimal amount) {}
