package io.kadmos.demo.web;

import io.kadmos.demo.configuration.ServiceProperties;
import io.kadmos.demo.service.BalanceService;
import io.kadmos.demo.web.dto.BalanceDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GetAccountBalanceController {
    private final BalanceService balanceService;
    private final UUID activeAccount;

    public GetAccountBalanceController(BalanceService balanceService, ServiceProperties properties) {
        this.balanceService = balanceService;
        this.activeAccount = properties.activeAccounts().get(0);
    }

    @GetMapping("/balance")
    public BalanceDto getBalance() {
        return balanceService.getBalance(activeAccount);
    }
}
