package io.kadmos.demo.service;

import io.kadmos.demo.persistence.AccountRepository;
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

    @Test
    void shouldReturnBalanceForAnExistingAccount() {
        // given
        var accountId = UUID.randomUUID();
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
        var accountId = UUID.randomUUID();
        when(accountRepository.getBalance(any())).thenReturn(Optional.empty());

        // when
        var actual = balanceService.getBalance(accountId);

        // then
        assertThat(actual).isEqualTo(new BalanceDto(BigDecimal.ZERO));
        verify(accountRepository).getBalance(accountId);
    }
}