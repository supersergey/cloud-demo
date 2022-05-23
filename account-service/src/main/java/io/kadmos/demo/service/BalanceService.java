package io.kadmos.demo.service;

import io.kadmos.demo.persistence.AccountRepository;
import io.kadmos.demo.persistence.model.Transaction;
import io.kadmos.demo.web.dto.BalanceDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class BalanceService {
    private final AccountRepository accountRepository;

    public BalanceService(AccountRepository accountrepository) {
        this.accountRepository = accountrepository;
    }

    public BalanceDto getBalance(UUID accountId) {
        return accountRepository.getBalance(accountId)
            .flatMap(amount -> Optional.of(new BalanceDto(amount)))
            .orElse(new BalanceDto(BigDecimal.ZERO));
    }

    public BalanceDto addBalance(UUID accountId, BigDecimal amount) {
        var updatedBalance= accountRepository.save(
                new Transaction(accountId, amount)
        );
        return new BalanceDto(updatedBalance);
    }
}
