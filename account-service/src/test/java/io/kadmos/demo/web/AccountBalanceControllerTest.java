package io.kadmos.demo.web;

import io.kadmos.demo.configuration.ServiceProperties;
import io.kadmos.demo.service.BalanceService;
import io.kadmos.demo.web.dto.BalanceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountBalanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BalanceService balanceService;

    private final UUID activeAccount = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ServiceProperties serviceProperties = new ServiceProperties(List.of(activeAccount));
        AccountBalanceController controller = new AccountBalanceController(balanceService, serviceProperties);
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();
    }

    @Test
    void shouldGetBalance() throws Exception {
        // given
        when(balanceService.getBalance(any())).thenReturn(
            new BalanceDto(BigDecimal.valueOf(10.99))
        );

        // when
        mockMvc.perform(
                get("/balance")
            )
        // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("amount").value("10.99"));

        verify(balanceService).getBalance(activeAccount);
    }

    @Test
    void shouldAddBalance() throws Exception {
        // given
        when(balanceService.addBalance(any(), any())).thenReturn(new BalanceDto(BigDecimal.valueOf(10.99)));

        //when
        mockMvc.perform(
                post("/balance")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(/*language=JSON*/
                        """
                        { "amount": "10.99"}
                        """)
            )
        // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("amount").value("10.99"));

        verify(balanceService).addBalance(activeAccount, BigDecimal.valueOf(10.99));
    }
}