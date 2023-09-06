drop database if exists housing_tracker_test;
create database housing_tracker_test;
use housing_tracker_test;

drop table if exists app_user_role;
drop table if exists app_role;
drop table if exists app_user;
drop table if exists location;
drop table if exists listings;
drop table if exists comments;

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    enabled bit not null default(1)
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table location (
	location_id int primary key auto_increment,
    city text,
    state text, 
    zipcode int
);

create table listings (
	listing_id int primary key auto_increment,
    location_id int,
    cost int, 
    num_beds int,
    num_baths int,
    app_user_id int,
    pet_friendly boolean,
    laundry text,
    parking text,
    gym boolean,
    constraint fk_listings_location_id
        foreign key (location_id)
        references location(location_id),
    constraint fk_app_user_user_id
        foreign key (app_user_id)
        references app_user(app_user_id)
);

create table comments (
	comment_id int primary key auto_increment,
    comment_text text,
    app_user_id int, 
    listing_id int,
    constraint fk_comment_app_user_id
		foreign key (app_user_id)
        references app_user(app_user_id),
	constraint fk_comment_listing_id
		foreign key (listing_id)
        references listings(listing_id)
);  

delimiter //
create procedure set_known_good_state()
begin

set FOREIGN_KEY_CHECKS = 0; -- Disable foreign key checks

   truncate comments;
   truncate listings;
   truncate location;
   truncate app_user_role;
   truncate app_user;
   truncate app_role;

   set FOREIGN_KEY_CHECKS = 1; -- Re-enable foreign key checks

insert into app_role (`name`) values
    ('USER'),
    ('ADMIN');

-- passwords are set to "P@ssw0rd!"
insert into app_user (username, password_hash, enabled)
    values
    ('john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
    ('alex123', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
	('emily22', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1),
	('daniel94', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1);

insert into app_user_role
    values
    (1, 2),
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1);  
    
insert into location (city, state, zipcode)
	values 
    ("Charlotte","NC",28278),
    ("Charlotte","NC",28217);
    
insert into listings (location_id, cost, num_beds, num_baths, app_user_id, pet_friendly, laundry, parking, gym)
	values
    (1,2000,2,1,1,false,"In-Unit","Street",true),
    (2,1500,1,1,4,true,"In-Unit","Paid residential",true);
    
insert into comments (comment_text, app_user_id, listing_id)
	values 
    ("Not a safe area", 4, 2),
    ("This one is so pretty!",2,1);
    
        end //
delimiter ;

