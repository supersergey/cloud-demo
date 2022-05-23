package io.kadmos.demo.service;

import io.kadmos.demo.persistence.AccountRepository;
import io.kadmos.demo.persistence.model.Transaction;
import io.kadmos.demo.web.dto.BalanceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        clearInvocations(accountRepository);
    }

    private final UUID accountId = UUID.randomUUID();

    @Test
    void shouldReturnBalanceForAnExistingAccount() {
        // given
        when(accountRepository.getBalance(any())).thenReturn(Optional.of(BigDecimal.valueOf(10)));

        // when
        var actual = balanceService.getBalance(accountId);

        // then
        assertThat(actual).isEqualTo(new BalanceDto(new BigDecimal(10)));
        verify(accountRepository).getBalance(accountId);
    }

    @Test
    void shouldReturnZeroForAnNonExistingAccount() {
        // given
        when(accountRepository.getBalance(any())).thenReturn(Optional.empty());

        // when
        var actual = balanceService.getBalance(accountId);

        // then
        assertThat(actual).isEqualTo(new BalanceDto(BigDecimal.ZERO));
        verify(accountRepository).getBalance(accountId);
    }

    @Test
    void shouldSaveTheAmountAndReturnAnUpdatedBalance() {
        // given
        when(accountRepository.save(any())).thenReturn(BigDecimal.valueOf(10.99));

        // when
        var actual = balanceService.addBalance(accountId, BigDecimal.valueOf(5));

        // then
        assertThat(actual.amount())
            .usingComparator(BigDecimal::compareTo)
            .isEqualTo(BigDecimal.valueOf(10.99));

        verify(accountRepository).save(new Transaction(accountId, BigDecimal.valueOf(5)));
    }
}