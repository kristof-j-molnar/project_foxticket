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

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE, CART_ID)
VALUES ('Admin', 'admin@admin.com', '$2a$10$fAKsedEmk29iZlspbWX2fODzONubZQRVfWg5Wc4.gWLHupGHMC6LS',
        'ROLE_USER,ROLE_ADMIN', 1),
       ('User', 'user@user.com', '$2a$12$z.fUhIuPpKbM2ikjf5YcbO31.yBSo0/GT4/1FB3PNyzIgEQh4q/fy', 'ROLE_USER', 2);

INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets awesome', 'Ipsum Lorum', '2023-12-11');
INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test Title', 'Test Content', '2023-12-11');

INSERT INTO CARTITEMS(CART_ID, ID, PRODUCT_ID)
VALUES (1, 1, 1),
        (NULL, 2, 2),
        (NULL, 3, 3);