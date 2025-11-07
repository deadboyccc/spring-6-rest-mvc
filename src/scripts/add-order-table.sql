DROP TABLE IF EXISTS beer_order_line;
DROP TABLE IF EXISTS beer_order;

CREATE TABLE beer_order
(
    id                 VARCHAR(36) PRIMARY KEY,
    created_date       TIMESTAMP(6),
    customer_ref       VARCHAR(255),
    last_modified_date TIMESTAMP(6),
    version            BIGINT,
    customer_id        VARCHAR(36),
    CONSTRAINT fk_beer_order_customer
        FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE beer_order_line
(
    id                 VARCHAR(36) PRIMARY KEY,
    beer_id            VARCHAR(36),
    created_date       TIMESTAMP(6),
    last_modified_date TIMESTAMP(6),
    order_quantity     INTEGER,
    quantity_allocated INTEGER,
    version            BIGINT,
    beer_order_id      VARCHAR(36),
    CONSTRAINT fk_beer_order_line_order
        FOREIGN KEY (beer_order_id) REFERENCES beer_order (id),
    CONSTRAINT fk_beer_order_line_beer
        FOREIGN KEY (beer_id) REFERENCES beer (id)
);
