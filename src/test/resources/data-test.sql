DROP TABLE IF EXISTS PRODUCTS;

DROP TABLE IF EXISTS USERS;

DROP TABLE IF EXISTS CARTS;
CREATE TABLE CARTS (ID INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (ID));

INSERT INTO CARTS(ID)
VALUES (1);

DROP TABLE IF EXISTS PRODUCT_TYPES;
CREATE TABLE PRODUCT_TYPES
(
    ID   int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME varchar(100) DEFAULT NULL
);

INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('jegy'),
       ('bérlet');

CREATE TABLE PRODUCTS
(
    ID          int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME        varchar(100) DEFAULT NULL,
    PRICE       int          DEFAULT NULL,
    DURATION    int          DEFAULT NULL,
    DESCRIPTION varchar(100) DEFAULT NULL,
    TYPE_ID     int          DEFAULT NULL,
    CART_ID     int          DEFAULT NULL,
    CONSTRAINT FK_TYPE
        FOREIGN KEY (TYPE_ID)
            REFERENCES PRODUCT_TYPES (ID),
    CONSTRAINT FK_CART
        FOREIGN KEY (CART_ID)
            REFERENCES CARTS (ID)
);

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID , CART_ID)
VALUES ('teszt jegy 1', 480, 90, 'teszt1',1,1),
       ('teszt bérlet 1', 4000, 9000, 'teszt2',2,1),
       ('teszt bérlet 2', 9500, 9000, 'teszt3',2,1);

CREATE TABLE USERS
(
    ID       int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME     varchar(100) DEFAULT NULL,
    PASSWORD varchar(100) DEFAULT NULL,
    EMAIL    varchar(100) DEFAULT NULL UNIQUE,
    ROLE     varchar(100) DEFAULT NULL,
    CART_ID  int DEFAULT NULL,
        CONSTRAINT IF NOT EXISTS FK_CART
    FOREIGN KEY (CART_ID)
        REFERENCES CARTS (ID)

);

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE)
VALUES ('TestUser', 'user@user.user', '$2a$12$L8fyzChb7.59SlhPPJ.0DOuzM1J3x3FtXUK75ibY9udZ3QNCDReSW', 'ROLE_USER');

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE)
VALUES ('TestAdmin', 'admin@admin.hu', '$2a$10$fAKsedEmk29iZlspbWX2fODzONubZQRVfWg5Wc4.gWLHupGHMC6LS',
        'ROLE_USER,ROLE_ADMIN');

DROP TABLE IF EXISTS ARTICLES;
CREATE TABLE ARTICLES
(
    ID          INT NOT NULL AUTO_INCREMENT,
    TITLE       VARCHAR(100) DEFAULT NULL,
    CONTENT     VARCHAR(100) DEFAULT NULL,
    PUBLISHDATE DATE         DEFAULT NULL,
    PRIMARY KEY (ID)
);

INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets awesome', 'Ipsum Lorum', '2023-12-11');
INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test Title', 'Test Content', '2023-12-11');