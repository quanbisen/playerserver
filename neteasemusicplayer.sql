create database neteasemusicplayer default character set utf8 collate utf8_general_ci;
use neteasemusicplayer;
create table t_users
(
id varchar(50) primary key,
password varchar(50) not null,
name varchar(50),
token varchar(255),
login_time datetime,
description varchar(500),
sex varchar(5) default '保密',
birthday date,
image_url varchar(255),
province varchar(50),
city varchar(50)
)engine InnoDB comment '用户信息表';
create table t_groups
(
id bigint unsigned primary key auto_increment,
name varchar (50),
description varchar(500),
create_time datetime, 
user_id varchar(50),
image_url varchar(255),
favor tinyint,
foreign key(user_id) references t_users(id)
)engine InnoDB comment '歌单信息表';
create table t_singers
(
id bigint unsigned primary key auto_increment,
name varchar(255),
birthday date,
height float,
weight float,
constellation varchar(50),
description varchar(500),
image_url varchar(255)
)engine InnoDB comment '歌手信息表';
create table t_albums
(
id bigint unsigned primary key auto_increment,
name varchar(255),
description varchar(500),
singer_id bigint unsigned,
image_url varchar(255),
foreign key(singer_id) references t_singers(id)
)engine InnoDB comment '专辑信息表';
create table t_songs
(
id bigint unsigned primary key auto_increment,
name varchar (255),
total_time varchar(50),
size varchar(50),
publish_time datetime,
album_id bigint unsigned,
collect_time datetime,
resource_url varchar (255),
lyric_url varchar (255),
foreign key(album_id) references t_albums(id)
)engine InnoDB comment '歌曲信息表';
create table t_groupsongs
(
group_id bigint unsigned,
song_id bigint unsigned,
add_time datetime,
foreign key(group_id) references t_groups(id),
foreign key(song_id) references t_songs(id),
unique(group_id,song_id)	#设置不可重复
)engine InnoDB comment '歌单歌曲中间表';
create table t_singersongs
(
singer_id bigint unsigned,
song_id bigint unsigned,
foreign key(singer_id) references t_singers(id),
foreign key(song_id) references t_songs(id)
)engine InnoDB comment '歌手歌曲中间表';
