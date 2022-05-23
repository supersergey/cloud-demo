package io.kadmos.demo.web;

import io.kadmos.demo.configuration.ServiceProperties;
import io.kadmos.demo.service.BalanceService;
import io.kadmos.demo.web.dto.BalanceDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountBalanceController {
    private final BalanceService balanceService;
    private final UUID activeAccount;

    public AccountBalanceController(BalanceService balanceService, ServiceProperties properties) {
        this.balanceService = balanceService;
        this.activeAccount = properties.activeAccounts().get(0);
    }

    @GetMapping("/balance")
    public BalanceDto getBalance() {
        return balanceService.getBalance(activeAccount);
    }

    @PostMapping("/balance")
    public BalanceDto addBalance(@RequestBody BalanceDto balanceDto) {
        return balanceService.addBalance(activeAccount, balanceDto.amount());
    }
}
