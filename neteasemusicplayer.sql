create database neteasemusicplayer;
use neteasemusicplayer;
create table t_songs
(
id bigint primary key auto_increment,
name varchar (255),
singer varchar(255),
album varchar (255),
total_time varchar(50),
size varchar(50),
resource_url varchar (255),
lyric_url varchar (255),
album_url varchar(255)
);
create table t_singers
(
id bigint primary key auto_increment,
name varchar(255),
birthday date,
height float,
weight float,
constellation varchar(50),
description varchar(500),
image_url varchar(255)
);
create table t_albums
(
id bigint primary key auto_increment,
name varchar(255),
singer varchar(255),
createtime datetime default current_timestamp,
description varchar(500),
image_url varchar(255)
);
create table t_users
(
id varchar(50) primary key,
password varchar(50) not null,
name varchar(50),
token varchar(50),
login_time datetime,
description varchar(500),
sex varchar(5) default '保密',
birthday date,
image_url varchar(255)
);
create table t_groups
(
id bigint primary key auto_increment,
name varchar (50),
description varchar(500),
createtime datetime default current_timestamp, 
user_id varchar(50),
image_url varchar(255),
favor tinyint,
foreign key(user_id) references t_users(id)
);
create table t_groupsongs
(
group_id bigint,
song_id bigint,
addtime datetime default current_timestamp,
foreign key(group_id) references t_groups(id),
foreign key(song_id) references t_songs(id)
);
create table t_albumsongs
(
album_id bigint,
song_id bigint,
addtime datetime default current_timestamp,
foreign key(album_id) references t_albums(id),
foreign key(song_id) references t_songs(id)
);
