-- ### TestTable ###
create table test_table
(
    id   bigint not null
        primary key,
    name varchar(255)
);

alter table test_table
    owner to root;


-- ### UserGroup ###
create table user_group
(
    id   uuid not null
        primary key,
    name varchar(255)
);

alter table user_group
    owner to root;

INSERT INTO user_group (id, name) VALUES ('61e1c15f-bd2f-4a4a-b0f3-41eae7b681c9', 'group1');
INSERT INTO user_group (id, name) VALUES ('f85f413d-2e85-4f84-b709-4a8adbf3c778', 'group2');

-- ### AppUser ###
create table app_user
(
    id       uuid not null
        primary key,
    group_id uuid
        constraint fk_app_user_user_group_id
            references user_group,
    name     varchar(255)
);

alter table app_user
    owner to root;

INSERT INTO app_user (id, name, group_id) VALUES ('4a421c1c-8df2-4ef2-845d-07d9a216c114', 'user1', '61e1c15f-bd2f-4a4a-b0f3-41eae7b681c9');
INSERT INTO app_user (id, name, group_id) VALUES ('9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa', 'user2','61e1c15f-bd2f-4a4a-b0f3-41eae7b681c9');
INSERT INTO app_user (id, name, group_id) VALUES ('11111111-1111-1111-1111-111111111111', 'user3', 'f85f413d-2e85-4f84-b709-4a8adbf3c778');
INSERT INTO app_user (id, name, group_id) VALUES ('22222222-2222-2222-2222-222222222222', 'user4', 'f85f413d-2e85-4f84-b709-4a8adbf3c778');
INSERT INTO app_user (id, name, group_id) VALUES ('33333333-3333-3333-3333-333333333333', 'user5', 'f85f413d-2e85-4f84-b709-4a8adbf3c778');

-- ### Order ###
create table orders
(
    id        uuid    not null
        primary key,
    quantity  integer not null,
    user_id   uuid
        constraint fk_orders_app_user_id
            references app_user,
    item_name varchar(255)
);

alter table orders
    owner to root;
-- Orders for user1
INSERT INTO orders (id, item_name, quantity, user_id) VALUES
                                                          ('10000000-0000-0000-0000-000000000001', 'Laptop', 1, '4a421c1c-8df2-4ef2-845d-07d9a216c114'),
                                                          ('10000000-0000-0000-0000-000000000002', 'Keyboard', 2, '4a421c1c-8df2-4ef2-845d-07d9a216c114'),
                                                          ('10000000-0000-0000-0000-000000000003', 'Mouse', 1, '4a421c1c-8df2-4ef2-845d-07d9a216c114'),
                                                          ('10000000-0000-0000-0000-000000000004', 'Monitor', 1, '4a421c1c-8df2-4ef2-845d-07d9a216c114'),
                                                          ('10000000-0000-0000-0000-000000000005', 'Notebook', 5, '4a421c1c-8df2-4ef2-845d-07d9a216c114');

-- Orders for user2
INSERT INTO orders (id, item_name, quantity, user_id) VALUES
                                                          ('20000000-0000-0000-0000-000000000001', 'Desk', 1, '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa'),
                                                          ('20000000-0000-0000-0000-000000000002', 'Chair', 1, '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa'),
                                                          ('20000000-0000-0000-0000-000000000003', 'Lamp', 2, '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa'),
                                                          ('20000000-0000-0000-0000-000000000004', 'Notebook', 3, '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa'),
                                                          ('20000000-0000-0000-0000-000000000005', 'Pen Set', 5, '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa');
-- Orders for user3
INSERT INTO orders (id, item_name, quantity, user_id) VALUES
                                                          ('30000000-0000-0000-0000-000000000001', 'Tablet', 1, '11111111-1111-1111-1111-111111111111'),
                                                          ('30000000-0000-0000-0000-000000000002', 'Pen', 2, '11111111-1111-1111-1111-111111111111'),
                                                          ('30000000-0000-0000-0000-000000000003', 'Notebook', 3, '11111111-1111-1111-1111-111111111111');

-- Orders for user4
INSERT INTO orders (id, item_name, quantity, user_id) VALUES
                                                          ('40000000-0000-0000-0000-000000000001', 'Camera', 1, '22222222-2222-2222-2222-222222222222'),
                                                          ('40000000-0000-0000-0000-000000000002', 'Tripod', 1, '22222222-2222-2222-2222-222222222222'),
                                                          ('40000000-0000-0000-0000-000000000003', 'SD Card', 4, '22222222-2222-2222-2222-222222222222');

-- Orders for user5
INSERT INTO orders (id, item_name, quantity, user_id) VALUES
                                                          ('50000000-0000-0000-0000-000000000001', 'Book', 2, '33333333-3333-3333-3333-333333333333'),
                                                          ('50000000-0000-0000-0000-000000000002', 'E-reader', 1, '33333333-3333-3333-3333-333333333333'),
                                                          ('50000000-0000-0000-0000-000000000003', 'Cover Case', 1, '33333333-3333-3333-3333-333333333333');

-- ### Payment ###
create table payment
(    id         uuid not null
         primary key,
     user_id    uuid
         constraint fk_payment_app_user_id
             references app_user,
    created_at timestamp(6),
    method     varchar(255)
);

alter table payment
    owner to root;

-- Payments for user1
INSERT INTO payment (id, method, created_at, user_id) VALUES
                                                          ('30000000-0000-0000-0000-000000000001', 'CARD', '2025-04-05T10:00:00', '4a421c1c-8df2-4ef2-845d-07d9a216c114'),
                                                          ('30000000-0000-0000-0000-000000000002', 'PAYPAL', '2025-04-05T12:00:00', '4a421c1c-8df2-4ef2-845d-07d9a216c114'),
                                                          ('30000000-0000-0000-0000-000000000003', 'CARD', '2025-04-05T14:00:00', '4a421c1c-8df2-4ef2-845d-07d9a216c114');

-- Payments for user2
INSERT INTO payment (id, method, created_at, user_id) VALUES
                                                          ('40000000-0000-0000-0000-000000000001', 'CARD', '2025-04-05T10:30:00', '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa'),
                                                          ('40000000-0000-0000-0000-000000000002', 'PAYPAL', '2025-04-05T13:00:00', '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa'),
                                                          ('40000000-0000-0000-0000-000000000003', 'CARD', '2025-04-05T15:00:00', '9b4f499b-5b9e-4d56-9f90-8c8d7b013aaa');

-- Payments for user3
INSERT INTO payment (id, method, created_at, user_id) VALUES
                                                          ('50000000-0000-0000-0000-000000000001', 'CARD', '2025-04-05T11:00:00', '11111111-1111-1111-1111-111111111111'),
                                                          ('50000000-0000-0000-0000-000000000002', 'BANK_TRANSFER', '2025-04-05T11:30:00', '11111111-1111-1111-1111-111111111111');

-- Payments for user4
INSERT INTO payment (id, method, created_at, user_id) VALUES
                                                          ('60000000-0000-0000-0000-000000000001', 'CARD', '2025-04-05T12:00:00', '22222222-2222-2222-2222-222222222222'),
                                                          ('60000000-0000-0000-0000-000000000002', 'CASH', '2025-04-05T12:30:00', '22222222-2222-2222-2222-222222222222');

-- Payments for user5
INSERT INTO payment (id, method, created_at, user_id) VALUES
                                                          ('70000000-0000-0000-0000-000000000001', 'PAYPAL', '2025-04-05T13:00:00', '33333333-3333-3333-3333-333333333333'),
                                                          ('70000000-0000-0000-0000-000000000002', 'CARD', '2025-04-05T13:30:00', '33333333-3333-3333-3333-333333333333');