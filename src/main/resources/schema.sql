CREATE TABLE IF NOT EXISTS PRODUCT
(
    PRODUCT_ID INTEGER IDENTITY PRIMARY KEY,
    NAME VARCHAR(100),
    PRICE VARCHAR(100),
);

CREATE TABLE IF NOT EXISTS DISCOUNT
(
    PRODUCT INTEGER,
    QUANTITY VARCHAR(100),
    PRICE VARCHAR(100),
);
