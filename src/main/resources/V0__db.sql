create type foo as
(
    bar real
);

create table quz
(
    foos foo[]
);

insert into quz
values (array [row (1.23)::foo])
