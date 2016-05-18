drop database ecommerce_2016;

create database ecommerce_2016;

create language plpgsql;

create role ecommerce_2016_user with password 'ecommerce_2016_user_password' login;

\c ecommerce_2016;

create table account(
	username varchar(20) not null,
	password varchar(20) not null,
	realname varchar(40),
	realsurname varchar(40),
	is_admin boolean not null,
	constraint account_pk primary key(username)
);

create table currency(
	curr_name varchar(10) not null,
	to_EUR decimal,
	to_dollar decimal,
	constraint currency_pk primary key(curr_name)
);

create table item(
	id integer not null,
	title varchar(100) not null,
	description text,
	price decimal not null,
	currency varchar(10) not null,
	available timestamp without time zone,
	image_path varchar(100), -- path to the image file
	image_descr varchar(100), -- alternative description (if file not found)
	show boolean not null,
	constraint item_pk primary key(id),
	constraint currency_ref foreign key(currency) references currency(curr_name)
);

create table category(
	id integer not null,
	categ_name varchar(100) not null,
	image_path varchar(100), -- path to the image file
	image_descr varchar(100), -- alternative description (if file not found)
	constraint category_pk primary key(id)
);

create table categ_item(
	categ_id integer not null,
	item_id integer not null,
	constraint categ_item_pk primary key(categ_id, item_id),
	constraint categ_item_ref1 foreign key(categ_id) references category(id),
	constraint categ_item_ref2 foreign key(item_id) references item(id)
);

grant create on database ecommerce_2016 to ecommerce_2016_user;

grant select on account to ecommerce_2016_user;
grant insert on account to ecommerce_2016_user;
grant update on account to ecommerce_2016_user;
grant delete on account to ecommerce_2016_user;

grant select on currency to ecommerce_2016_user;
grant insert on currency to ecommerce_2016_user;
grant update on currency to ecommerce_2016_user;
grant delete on currency to ecommerce_2016_user;

grant select on item to ecommerce_2016_user;
grant insert on item to ecommerce_2016_user;
grant update on item to ecommerce_2016_user;
grant delete on item to ecommerce_2016_user;

grant select on category to ecommerce_2016_user;
grant insert on category to ecommerce_2016_user;
grant update on category to ecommerce_2016_user;
grant delete on category to ecommerce_2016_user;

grant select on categ_item to ecommerce_2016_user;
grant insert on categ_item to ecommerce_2016_user;
grant update on categ_item to ecommerce_2016_user;
grant delete on categ_item to ecommerce_2016_user;

