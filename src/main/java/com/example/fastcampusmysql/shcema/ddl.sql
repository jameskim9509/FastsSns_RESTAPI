create table Member
(
    id int auto_increment,
    email varchar(20) not null,
    nickName varchar(20) not null,
    birth date not null,
    createdAt datetime not null,
    constraint member_id_uindex
        primary key (id)
);

create table MemberNickNameHistory
(
    id int auto_increment,
    memberId int not null,
    nickName varchar(20) not null,
    createdAt datetime not null,
    constraint memberNicknameHistory_id_uindex
        primary key (id)
);

create table Follow
(
    id int auto_increment,
    fromMemberId int not null,
    toMemberId int not null,
    createdAt datetime not null,
    constraint Follow_id_uindex
        primary key (id)
);

create unique index Follow_fromMemberId_toMemberId_uindex
    on Follow (fromMemberId, toMemberId);

create table POST
(
    id int auto_increment,
    memberId int not null,
    contents varchar(100) not null,
    createdDate date not null,
    createdAt datetime not null,
    constraint POST_id_uindex
        primary key (id)
);

CREATE INDEX index_member_id
    ON Post (memberId);

SHOW INDEX FROM Post;
ANALYZE TABLE Post;

CREATE INDEX index_created_date
    ON Post (createdDate);

create table TimeLine
(
    id int auto_increment,
    memberId int not null,
    postId int not null,
    createdAt datetime not null,
    constraint TimeLine_id_uindex
      primary key(id)
)

drop table Member;
drop table MemberNickNameHistory;
drop table TimeLine;