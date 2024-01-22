INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('ticket'),
       ('pass');

INSERT INTO CARTS(ID)
VALUES (1),
       (2);

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('single ticket', 480, 90, 'test1', 1),
       ('monthly student pass', 4000, 9000, 'test2', 2),
       ('monthly pass', 9500, 9000, 'test3', 2);
VALUES (1);

INSERT INTO CARTS(ID)
VALUES (2);

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('vonaljegy', 480, 90, 'teszt1', 1),
       ('havi diák bérlet', 4000, 9000, 'teszt2', 2),
       ('havi bérlet', 9500, 9000, 'teszt3', 2);

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID, IS_DELETED)
VALUES ('vonaljegy', 480, 90, 'teszt1', 1, false),
       ('havi diák bérlet', 4000, 9000, 'teszt2', 2, false),
       ('havi bérlet', 9500, 9000, 'teszt3', 2, false);

INSERT INTO CART_PRODUCT(CART_ID, PRODUCT_ID)
VALUES ((SELECT ID FROM CARTS WHERE ID = 1),(SELECT ID FROM PRODUCTS WHERE ID = 1));

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE, CART_ID)
VALUES ('Admin', 'admin@admin.com', '$2a$10$fAKsedEmk29iZlspbWX2fODzONubZQRVfWg5Wc4.gWLHupGHMC6LS', 'ROLE_USER,ROLE_ADMIN',1),
       ('User', 'user@user.com', '$2a$12$z.fUhIuPpKbM2ikjf5YcbO31.yBSo0/GT4/1FB3PNyzIgEQh4q/fy', 'ROLE_USER',2);

INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets awesome', 'Ipsum Lorum', '2023-12-11');
INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test Title', 'Test Content', '2023-12-11');