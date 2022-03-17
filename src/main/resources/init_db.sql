create schema grocery_manager;

create table grocery_manager.product (
    id serial primary key,
    name text not null,
    quantity int not null default 0 check (quantity >= 0)
)