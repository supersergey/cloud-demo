package io.kadmos.demo.web;

import io.kadmos.demo.configuration.ServiceProperties;
import io.kadmos.demo.service.BalanceService;
import io.kadmos.demo.web.dto.BalanceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GetAccountBalanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        ServiceProperties serviceProperties = new ServiceProperties(List.of(UUID.randomUUID()));
        GetAccountBalanceController controller = new GetAccountBalanceController(balanceService, serviceProperties);
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();
    }

    @Test
    void getBalance() throws Exception {
        when(balanceService.getBalance(any())).thenReturn(
            new BalanceDto(BigDecimal.valueOf(10.99))
        );
        mockMvc.perform(
                get("/balance")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("amount").value("10.99"));
    }
}