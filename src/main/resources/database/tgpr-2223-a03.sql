drop database if exists `tgpr-2223-a03`;
create database `tgpr-2223-a03`;
use `tgpr-2223-a03`;

create table account
(
    id    int auto_increment
        primary key,
    iban  varchar(34)                              not null,
    title varchar(255)                             not null,
    floor double                                   null,
    type  enum ('external', 'savings', 'checking') not null,
    saldo double                                   null,
    constraint account_iban_uindex
        unique (iban)
);

create table agency
(
    id      int auto_increment
        primary key,
    name    varchar(255) not null,
    manager int          not null,
    constraint agency_name_uindex
        unique (name)
);

create table user
(
    id         int auto_increment
        primary key,
    email      varchar(255)                                         not null,
    password   varchar(255)                                         not null,
    last_name  varchar(255)                                         not null,
    first_name varchar(255)                                         null,
    birth_date date                                                 null,
    type       enum ('client', 'manager', 'admin') default 'client' not null,
    agency     int                                                  null,
    constraint user_email_uindex
        unique (email),
    constraint user_agency_id_fk
        foreign key (agency) references agency (id)
);

create table access
(
    user    int                      not null,
    account int                      not null,
    type    enum ('holder', 'proxy') not null,
    primary key (user, account),
    constraint access_account_id_fk
        foreign key (account) references account (id),
    constraint access_user_id_fk
        foreign key (user) references user (id)
);

alter table agency
    add constraint agency_user_id_fk
        foreign key (manager) references user (id);

create table category
(
    id   int auto_increment
        primary key,
    name varchar(255) not null,
    account int          null,
    constraint category_name_user_uindex
        unique (name, account),
    constraint category_account_id_fk
        foreign key (account) references account (id)
);

create table favourite
(
    user    int not null,
    account int not null,
    primary key (user, account),
    constraint favourite_account_id_fk
        foreign key (account) references account (id),
    constraint favourite_user_id_fk
        foreign key (user) references user (id)
);

create table transfer
(
    id             int auto_increment
        primary key,
    amount         double                                             not null,
    description    varchar(255)                                       not null,
    source_account int                                                not null,
    target_account int                                                not null,
    source_saldo   double                                             null,
    target_saldo   double                                             null,
    created_at     datetime                                           not null,
    created_by     int                                                null,
    effective_at   date                                               null,
    state          enum ('executed', 'future', 'ignored', 'rejected') not null,
    constraint transfer_account_id_fk
        foreign key (source_account) references account (id),
    constraint transfer_account_id_fk_2
        foreign key (target_account) references account (id),
    constraint transfer_user_id_fk
        foreign key (created_by) references user (id)
);

create table transfer_category
(
    transfer int not null,
    account     int not null,
    category int not null,
    primary key (transfer, account),
    constraint transfer_category_category_id_fk
        foreign key (category) references category (id),
    constraint transfer_category_transfer_id_fk
        foreign key (transfer) references transfer (id),
    constraint transfer_category_account_id_fk
        foreign key (account) references account (id)
);

create table global
(
    system_date datetime not null
);


SET FOREIGN_KEY_CHECKS=0;

INSERT INTO `global` (system_date)
VALUES ('2022-01-17 23:59:59');

INSERT INTO `access` (`user`, `account`, `type`)
VALUES (4, 1, 'holder'),
       (4, 2, 'holder'),
       (4, 4, 'proxy'),
       (5, 1, 'proxy'),
       (5, 3, 'holder'),
       (5, 4, 'holder');

INSERT INTO `account` (`id`, `iban`, `title`, `floor`, `type`, `saldo`)
VALUES (1, 'BE02 9999 1017 8207', 'AAA', -50, 'checking', -50),
       (2, 'BE14 9996 1669 4306', 'BBB', -10, 'checking', 35),
       (3, 'BE55 9999 6717 9982', 'DDD', -100, 'checking', 0),
       (4, 'BE71 9991 5987 4787', 'CCC', 0, 'savings', 10),
       (5, 'BE23 0081 6870 0358', 'EEE', NULL, 'external', NULL);

INSERT INTO `agency` (`id`, `name`, `manager`)
VALUES (1, 'Agency1', 2),
       (2, 'Agency2', 2),
       (3, 'Agency3', 3);

INSERT INTO `user` (`id`, `email`, `password`, `last_name`, `first_name`, `birth_date`, `type`, `agency`)
VALUES (1, 'admin@test.com', 'c6aa01bd261e501b1fea93c41fe46dc7', 'Admin', NULL, NULL, 'admin', NULL),
       (2, 'ben@test.com', 'cc4902e2506fc6de54e53489314c615a', 'Penelle', 'Benoît', NULL, 'manager', NULL),
       (3, 'marc@test.com', 'b41216a574ab900d4911cce4f4941a00', 'Michel', 'Marc', NULL, 'manager', NULL),
       (4, 'bob@test.com', '6bc8d5a0ad1818c0924255f5e56e68c6', 'L\'Éponge', 'Bob', '1970-07-01', 'client', 1),
       (5, 'caro@test.com', 'e82d99e3aaa83e1746bda2a58b99ba17', 'de Monaco', 'Caroline', '1987-06-02', 'client', 1),
       (6, 'louise@test.com', '7daa9b56c1f4b8a7df06d3dbf6ca24a2', 'Attaque', 'Louise', '1988-12-31', 'client', 2),
       (7, 'jules@test.com', '1a895463711c99649bbf106e05ab1387', 'Verne', 'Jules', '1945-01-01', 'client', 2);

INSERT INTO `transfer` (`id`, `amount`, `description`, `source_account`, `target_account`, `source_saldo`,
                        `target_saldo`, `created_at`, `created_by`, `effective_at`, `state`)
VALUES (1, 10, 'Tx #001', 1, 2, -10, 10, '2022-01-01 18:24:04', 1, NULL, 'executed'),
(22, 10, 'Tx #022', 1, 2, -10, 10, '2022-01-01 18:36:04', 1, '2022-11-22', 'future'),
(23, 10, 'Tx #023', 1, 4, -10, 10, '2022-01-01 19:24:04', 1, '2022-11-22', 'future'),
       (2, 5, 'Tx #002', 1, 4, -15, 20, '2022-01-01 18:25:38', 1, '2022-01-05', 'executed'),
       (3, 35, 'Tx #003', 1, 2, -50, 30, '2022-01-01 22:09:38', 1, '2022-01-09', 'executed'),
       (4, 15, 'Tx #004', 2, 4, -5, 15, '2022-01-02 01:54:21', 1, '2022-01-03', 'executed'),
       (5, 50, 'Tx #005', 1, 2, NULL, NULL, '2022-01-02 13:39:47', 1, '2022-01-04', 'rejected'),
       (6, 20, 'Tx #006', 2, 1, NULL, NULL, '2022-01-03 14:04:16', 1, '2022-01-07', 'rejected'),
       (7, 5, 'Tx #007', 5, 4, NULL, 25, '2022-01-04 01:08:07', 1, '2022-01-08', 'executed'),
       (8, 100, 'Tx #008', 4, 2, NULL, NULL, '2022-01-06 13:32:25', 1, NULL, 'rejected'),
       (9, 10, 'Tx #009', 2, 5, 20, NULL, '2022-01-07 22:40:19', 1, '2022-01-11', 'executed'),
       (10, 15, 'Tx #010', 4, 1, 10, -35, '2022-01-10 10:32:15', 1, NULL, 'executed'),
       (11, 15, 'Tx #011', 1, 4, NULL, NULL, '2022-01-11 22:17:03', 1, '2022-01-16', 'future'),
       (12, 35, 'Tx #012', 2, 4, NULL, NULL, '2022-01-12 20:27:22', 1, '2022-01-14', 'rejected'),
       (13, 100, 'Tx #013', 1, 4, NULL, NULL, '2022-01-13 11:08:02', 1, NULL, 'rejected'),
       (14, 15, 'Tx #014', 1, 2, -50, 35, '2022-01-15 02:54:05', 1, NULL, 'executed');

INSERT INTO `category` (`id`, `name`, `account`)
VALUES (1, 'Salary', NULL),
       (2, 'Holiday', NULL),
       (3, 'Rent', NULL),
       (4, 'Sport', NULL),
       (5, 'Shopping', NULL),
       (6, 'Car', NULL),
       (7, 'Energy', NULL),
       (8, 'Food', NULL),
       (9, 'Clothing', NULL),
       (10, 'Culture', NULL),
       (11, 'Video Game', 1),
       (12, 'Gift', 1),
       (13, 'IT Material', 1),
       (14, 'Gift', 2),
       (15, 'Party', 2),
       (16, 'None', NULL);

INSERT INTO `transfer_category` (`transfer`, `account`, `category`)
VALUES (1, 1, 1),
       (1, 2, 1),
       (2, 1, 2),
       (11, 1, 4),
       (3, 1, 5),
       (4, 4, 5),
       (5, 1, 13),
       (4, 2, 14);

INSERT INTO `favourite` (`user`, `account`)
VALUES (4, 5);

SET FOREIGN_KEY_CHECKS=1;
