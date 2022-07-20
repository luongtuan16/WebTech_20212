use czone;
create table account
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    name varchar(255) not null,
    password text not null,
    phonenumber varchar(255) not null,
    avatar longtext null,
    uuid varchar(255) not null,
    description text null,
	city varchar(255) null,
	address text null,
	country varchar(255) null,
	cover_image text null,
	rolekey bigint null,
	active bool not null
);
create table post
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    content text not null,
    accountid bigint not null
);
create table comment
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    content text not null,
    postid bigint not null,
    accountid bigint not null
);

create table blocks
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    idblocks bigint not null,
    idblocked bigint not null
); 
create table friend
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    idA bigint not null,
    idB bigint not null,
    is_friend bool not null
);
create table likes
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    postid bigint not null,
    accountid bigint not null
);
create table file
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    content longtext null,
    postid bigint not null
    
);

create table search
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    keyword longtext null,
    accountid bigint not null
    
);

create table report
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    details varchar(255) not null,
    accountid bigint not null,
    postid bigint not null,
    typereportid bigint not null
);
create table typereport
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    name varchar(255) not null
);
alter table file add constraint fk_file_post foreign key (postid) references post(id);
alter table search add constraint fk_search_account foreign key (accountid) references account(id);
alter table blocks add constraint fk_blocks_account_1 foreign key (idblocks) references account(id);
alter table blocks add constraint fk_blocks_account_2 foreign key (idblocked) references account(id);
alter table post add constraint fk_post_account foreign key (accountid) references account(id);
alter table comment add constraint fk_comment_post foreign key (postid) references post(id);
alter table likes add constraint fk_likes_account foreign key (accountid) references account(id);
alter table likes add constraint fk_likes_post foreign key (postid) references post(id);
alter table comment add constraint fk_comment_account foreign key (accountid) references account(id);

alter table friend add constraint fk_friend_account_1 foreign key (idA) references account(id);
alter table friend add constraint fk_friend_account_2 foreign key (idB) references account(id);

alter table report add constraint fk_report_account foreign key (accountid) references account(id);
alter table report add constraint fk_report_post foreign key (postid) references post(id);
alter table report add constraint fk_report_typereport foreign key (typereportid) references typereport(id);
INSERT INTO typereport(deleted,name) VALUES(false,"Nội dung nhạy cảm");
INSERT INTO typereport(deleted,name) VALUES(false,"Làm phiền");
INSERT INTO typereport(deleted,name) VALUES(false,"Lừa đảo");
INSERT INTO typereport(deleted,name) VALUES(false,"Lí do khác");

create table block_mess
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) not null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    idblocks bigint not null,
    idblocked bigint not null
); 
alter table block_mess add constraint fk_block_mess_account_1 foreign key (idblocks) references account(id);
alter table block_mess add constraint fk_block_mess_account_2 foreign key (idblocked) references account(id);

create table verify_code
(
	id bigint not null primary key auto_increment,
    deleted bool not null,
    createddate timestamp not null,
    createdby varchar(255) null,
    modifieddate timestamp null,
    modifiedby varchar(255) null,
    phonenumber varchar(255) not null,
	vcode varchar(10) not null
); 


create table role
(
	rolekey bigint not null primary key,
    deleted bool not null,
    role varchar(255) not null
); 
alter table account add constraint fk_account_role foreign key (rolekey) references role(rolekey);

INSERT INTO role(rolekey,deleted,role) VALUES(4,false,"user");
INSERT INTO role(rolekey,deleted,role) VALUES(5,false,"admin");
INSERT INTO role(rolekey,deleted,role) VALUES(6,false,"superadmin");

create table conversation
(
	id bigint not null primary key auto_increment,
    accountA bigint not null,
	accountB bigint not null
);

create table message
(
	id bigint not null primary key auto_increment,
    createddate timestamp not null,
    content longtext null,
    conversationid bigint not null,
    atob bool not null, -- =true nếu accountA gửi cho accountB và ngược lại
	unread bool not null
);
alter table conversation add constraint fk_conversation_account_1 foreign key (accountA) references account(id);
alter table conversation add constraint fk_conversation_account_2 foreign key (accountB) references account(id);
alter table message add constraint fk_message_conversation foreign key (conversationid) references conversation(id);

ALTER DATABASE czone CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

