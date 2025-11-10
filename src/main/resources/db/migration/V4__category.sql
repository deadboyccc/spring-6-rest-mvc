-- Drop tables if they exist
DROP TABLE IF EXISTS beer_category;
DROP TABLE IF EXISTS category;

-- Create category table
CREATE TABLE category
(
    id                 UUID PRIMARY KEY,
    description        VARCHAR(50),
    created_date       TIMESTAMP(6),
    last_modified_date TIMESTAMP(6) DEFAULT NULL,
    version            BIGINT DEFAULT NULL
);

-- Create beer_category join table
CREATE TABLE beer_category
(
    beer_id     UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (beer_id, category_id),
    CONSTRAINT fk_beer FOREIGN KEY (beer_id) REFERENCES beer (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id)
);
