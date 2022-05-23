package io.kadmos.demo.persistence;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QueryFactoryTest {

    @Test
    void shouldBuildAddTransactionQuery() {
        // given
        var accountId = UUID.fromString("62a0b4a2-7887-44fc-ade8-6a682d278802");
        var amount = new BigDecimal(10);

        // when
        var actual = QueryFactory.getAddTransactionQuery(accountId, amount);

        // then
        assertThat(actual).isEqualToNormalizingNewlines(
            """
            insert into main.transaction (account_id, opening_balance, amount)
            values ( '62a0b4a2-7887-44fc-ade8-6a682d278802',
                coalesce(
                    (select opening_balance + amount from main.transaction
                     where account_id = '62a0b4a2-7887-44fc-ade8-6a682d278802'
                     order by id desc
                     limit 1
                     for update of transaction
                     ), 0),
                '10'
                );
            """
        );
    }

    @Test
    void shouldReturnLockQuery() {
        assertThat(QueryFactory.LOCK_QUERY).isEqualToIgnoringWhitespace(
            "lock table main.transaction IN SHARE ROW EXCLUSIVE MODE;"
        );
    }
}