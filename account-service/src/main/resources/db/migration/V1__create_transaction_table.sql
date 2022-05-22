CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Transaction
(
    id              BIGSERIAL      PRIMARY KEY,
    account_id      UUID           NOT NULL,
    created_at      timestamptz    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    opening_balance DECIMAL(15, 2) NOT NULL,
    amount          DECIMAL(15, 2) NOT NULL
);

create index transaction_account_id_created_at_index
    on transaction (account_id, created_at desc);
