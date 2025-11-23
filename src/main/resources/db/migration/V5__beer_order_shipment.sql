-- Drop table if exists
DROP TABLE IF EXISTS beer_order_shipment;

-- Create beer_order_shipment table
CREATE TABLE beer_order_shipment
(
    id                      UUID PRIMARY KEY,
    beer_order_id           UUID UNIQUE NOT NULL,
    tracking_number         VARCHAR(50),
    created_date            TIMESTAMP(6),
    last_modified_date      TIMESTAMP(6) DEFAULT NULL,
    version                 BIGINT DEFAULT NULL,
    CONSTRAINT fk_beer_order FOREIGN KEY (beer_order_id) REFERENCES beer_order (id)
);

-- Add beer_order_shipment_id column to beer_order table
ALTER TABLE beer_order
    ADD COLUMN beer_order_shipment_id UUID;

-- Add foreign key constraint
ALTER TABLE beer_order
    ADD CONSTRAINT fk_beer_order_shipment
        FOREIGN KEY (beer_order_shipment_id) REFERENCES beer_order_shipment (id);

