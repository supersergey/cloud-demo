package io.kadmos.demo.persistence;

import io.kadmos.demo.persistence.config.JooqTestConfiguration;
import io.kadmos.demo.persistence.config.JooqTestContainerExtension;
import io.kadmos.demo.persistence.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import(JooqTestConfiguration.class)
@ExtendWith(JooqTestContainerExtension.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    void shouldExecuteTransactionsInARow() {
        // given
        var accountId = UUID.randomUUID();
        var addBalanceTransaction = new Transaction(
            accountId, BigDecimal.valueOf(10.5)
        );

        var removeBalanceTransaction = new Transaction(
            accountId, BigDecimal.valueOf(-10.5)
        );
        var transactions = new ArrayList<Transaction>();
        for (int i = 0; i < 50; i++) {
            transactions.add(addBalanceTransaction);
            transactions.add(removeBalanceTransaction);
        }
        transactions.add(addBalanceTransaction);
        Collections.shuffle(transactions);
        transactions.forEach(t -> repository.save(t));

        // when
        var result = repository.getBalance(accountId);

        // then
        assertThat(result).usingComparator(BigDecimal::compareTo).isEqualTo(BigDecimal.valueOf(10.5));
    }

    @Test
    void shouldExecuteTransactionsInParallel() {
        // given
        var accountId = UUID.randomUUID();
        var transaction = new Transaction(
            accountId, BigDecimal.ONE
        );
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    repository.save(transaction);
                }
            }));
        }

        // when
        threads.forEach(Thread::start);
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ignored) {
        }

        // then
        assertThat(repository.getBalance(accountId))
            .usingComparator(BigDecimal::compareTo)
            .isEqualTo(BigDecimal.valueOf(1000));
    }
}