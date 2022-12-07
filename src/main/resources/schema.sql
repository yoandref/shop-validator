create schema if not exists shop;
create table shop.product(
    id INT primary key auto_increment not null,
    product_identifier varchar(100) not null,
    amount int not null
);
