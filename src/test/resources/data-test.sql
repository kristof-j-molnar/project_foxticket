
INSERT INTO CARTS(ID)
VALUES (default),
       (default);


INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('ticket'),
       ('pass');

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('test ticket', 480, 90, 'test1',1),
       ('test pass 1', 4000, 9000, 'test2',2),
       ('test pass 2', 9500, 9000, 'test3',2);

INSERT INTO CARTITEMS (CART_ID, ID, PRODUCT_ID)
VALUES (2, default, 1),
       (2, default, 2),
       (2, default, 3);

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE, CART_ID)
VALUES ('TestUser', 'user@user.hu', '$2a$12$z.fUhIuPpKbM2ikjf5YcbO31.yBSo0/GT4/1FB3PNyzIgEQh4q/fy', 'ROLE_USER', (SELECT ID FROM CARTS LIMIT 1));

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE, CART_ID)
VALUES ('TestAdmin', 'admin@admin.hu', '$2a$10$fAKsedEmk29iZlspbWX2fODzONubZQRVfWg5Wc4.gWLHupGHMC6LS',
        'ROLE_USER,ROLE_ADMIN', (SELECT ID FROM CARTS LIMIT 1 OFFSET 1));


INSERT INTO ARTICLES (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test article 1', 'Ipsum Lorem', '2023-12-11'),
       ('Test article 2', 'Test Content', '2023-12-11'),
       ('Test article 3', 'Lorem Ipsum', '2024-01-27');