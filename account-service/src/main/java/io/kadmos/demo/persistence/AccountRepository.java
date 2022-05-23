package io.kadmos.demo.persistence;

import io.kadmos.demo.persistence.model.Transaction;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static io.kadmos.demo.generated.tables.Transaction.TRANSACTION;

@Repository
public class AccountRepository {
    private final DSLContext jooq;

    public AccountRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    public BigDecimal save(Transaction transaction) {
        BigDecimalWrapper newAmount = new BigDecimalWrapper(BigDecimal.ZERO);
        jooq.transaction(configuration -> {
                DSL.using(configuration).query(QueryFactory.LOCK_QUERY).execute();
                var query = QueryFactory.getAddTransactionQuery(transaction.accountId(), transaction.transactionAmount());
                var executionResult = DSL.using(configuration).fetch(query);
                newAmount.add(
                    new BigDecimal(executionResult.getValue(0, 0).toString())
                ).add(
                    new BigDecimal(executionResult.getValue(0, 1).toString())
                );
            }
        );
        return newAmount.value;
    }

    public Optional<BigDecimal> getBalance(UUID accountId) {
        var lastRecord = fetchLastTransaction(accountId);
        if (lastRecord == null) {
            return Optional.empty();
        } else {
            return Optional.of(lastRecord.getFirst().add(lastRecord.getSecond()));
        }
    }

    private Pair<BigDecimal, BigDecimal> fetchLastTransaction(UUID accountId) {
        var lastRecord = jooq
            .select(TRANSACTION.OPENING_BALANCE, TRANSACTION.AMOUNT)
            .from(TRANSACTION)
            .where(TRANSACTION.ACCOUNT_ID.eq(accountId))
            .orderBy(TRANSACTION.CREATED_AT.desc())
            .limit(1)
            .fetchOne();
        if (lastRecord == null) {
            return null;
        } else {
            return Pair.of(
                lastRecord.getValue(TRANSACTION.OPENING_BALANCE),
                lastRecord.getValue(TRANSACTION.AMOUNT)
            );
        }
    }

    private class BigDecimalWrapper {
        private BigDecimal value;

        BigDecimalWrapper(BigDecimal initial) {
            this.value = initial;
        }

        BigDecimalWrapper add(BigDecimal other) {
            value = value.add(other);
            return this;
        }
    }
}
