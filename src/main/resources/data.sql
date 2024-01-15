INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('jegy'),
       ('bérlet');

INSERT INTO CARTS(ID)
VALUES (1);

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID,CART_ID)
VALUES ('vonaljegy', 480, 90, 'teszt1', 1,1),
       ('havi diák bérlet', 4000, 9000, 'teszt2', 2,1),
       ('havi bérlet', 9500, 9000, 'teszt3', 2,1);

INSERT INTO `USERS` (`NAME`, `EMAIL`, `PASSWORD`, `ROLE`, CART_ID)
VALUES ('Admin', 'admin@admin.com', 'adminadmin', 'ROLE_USER,ROLE_ADMIN',1);

INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets awesome', 'Ipsum Lorum', '2023-12-11');
INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test Title', 'Test Content', '2023-12-11');