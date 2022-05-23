package io.kadmos.demo.persistence;

import java.math.BigDecimal;
import java.util.UUID;

public class QueryFactory {
    final static String LOCK_QUERY = /*language=SQL*/ """
            lock table main.transaction IN SHARE ROW EXCLUSIVE MODE;
        """;

    static String getAddTransactionQuery(UUID accountId, BigDecimal amount) {
        return String.format(ADD_TRANSACTION_SQL_TEMPLATE, accountId, accountId, amount);
    }

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
            )
            returning opening_balance, amount;
        """;
}
