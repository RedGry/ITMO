create type sex as enum ('мужской', 'женский');

/* Создание таблиц */

create table "стая" (
    "ид"        serial primary key,
    "название"  text not null
);

create table "питекантроп"(
    "ид"        bigserial primary key,
    "прозвище"  text not null,
    "пол"       sex not null,
    "стая_ид"   int references "стая"("ид")
);

create table "локация"(
    "ид"        serial primary key,
    "название"  text not null
);

create table "действие"(
    "ид"            bigserial primary key,
    "название"      varchar(255) not null,
    "локация_ид"    int references "локация"("ид"),
    "начало"        timestamp not null,
    "конец"         timestamp constraint end_less_start check ("конец" >= "начало")
);

create table "действие_type"(
    "ид"                        bigserial primary key,
    "title"                     varchar(255) not null,
    "действие_ид"               int references "действие"("ид"),
    "адресат_стая_ид"           bigint,
    "адресат_питекантроп_ид"    int
);

create table "расположение_питекантропа"(
    "питекантроп_ид"    bigint references "питекантроп"("ид"),
    "локация_ид"        int references "локация"("ид"),
    primary key ("питекантроп_ид", "локация_ид")
);

create table "коммуникация_питекантропа"(
    "действие_ид"       bigint unique references "действие"("ид"),
    "петекантроп_ид"    bigint references "питекантроп"("ид"),
    primary key ("действие_ид", "петекантроп_ид")
);

create table "коммуникация_стаи"(
    "действие_ид"   bigint unique references "действие"("ид"),
    "стая_ид"       bigint references "стая"("ид"),
    primary key ("действие_ид", "стая_ид")
);