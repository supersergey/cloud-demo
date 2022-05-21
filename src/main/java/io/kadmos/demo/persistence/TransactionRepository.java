package io.kadmos.demo.persistence;

import io.kadmos.demo.persistence.model.Transaction;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

import static io.kadmos.demo.generated.tables.Transaction.TRANSACTION;

@Repository
public class TransactionRepository {
    private final DSLContext jooq;

    @Autowired
    public TransactionRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    public void save(Transaction transaction) {
        jooq.transaction(configuration -> {
                DSL.using(configuration).query(LOCK_QUERY).execute();
                var query = buildAddQuery(transaction.accountId(), transaction.transactionAmount());
                DSL.using(configuration).query(query).execute();
            }
        );
    }

    public BigDecimal getBalance(UUID accountId) {
        var lastRecord = lastTransaction(accountId);
        return lastRecord.getFirst().add(lastRecord.getSecond());
    }

    private Pair<BigDecimal, BigDecimal> lastTransaction(UUID accountId) {
        var lastRecord = jooq
            .select(TRANSACTION.OPENING_BALANCE, TRANSACTION.AMOUNT)
            .from(TRANSACTION)
            .where(TRANSACTION.ACCOUNT_ID.eq(accountId))
            .orderBy(TRANSACTION.CREATED_AT.desc())
            .limit(1)
            .fetchOne();
        if (lastRecord == null) {
            return Pair.of(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        return Pair.of(
            lastRecord.getValue(TRANSACTION.OPENING_BALANCE),
            lastRecord.getValue(TRANSACTION.AMOUNT)
        );
    }

    private String buildAddQuery(UUID accountId, BigDecimal amount) {
        return String.format(ADD_TRANSACTION_SQL_TEMPLATE, accountId, accountId, amount);
    }

    private final static String LOCK_QUERY = /*language=SQL*/ """
            lock table main.transaction IN SHARE ROW EXCLUSIVE MODE;
        """;

    private final static String ADD_TRANSACTION_SQL_TEMPLATE = /*language=SQL*/ """
        insert into main.transaction (account_id, opening_balance, amount)
        values ( '%s',
                coalesce(
                    (select opening_balance + amount from main.transaction
                     where account_id = '%s'
                     order by id desc
                     limit 1
                     for update of transaction
                     ), 0),
                '%s'
                );
        """;
}
