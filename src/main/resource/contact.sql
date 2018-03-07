drop table MEMBERS

create table MEMBERS(
	user_id 		varchar2(20) primary key,
	name			varchar2(20),
	password 		varchar2(20),
	cell_phone		varchar2(50),
	email			varchar2(50),
	address 		varchar2(200),
	grade 			number,
	reg_date 		date 	default sysdate,
	update_date 	date 	default sysdate
);

-- 일반회원 추가
insert into MEMBERS (user_id, name, password, cell_phone, email, address, grade)
values('hong', '홍길동', '1234', '010-1111-1111', 'hong@gmail.com', null, 1);

insert into MEMBERS (user_id, name, password, cell_phone, email, address, grade)
values('go', '고길동', '1234', '010-1111-1111', 'go@gmail.com', null, 2);

-- 관리자 추가
insert into MEMBERS (user_id, name, password, cell_phone, email, address, grade)
values('admin', '관리자', '1234', null, null, null, 0); 

select * from MEMBERS;

drop table CONTACTS;

create table CONTACTS(
	contact_id 		number primary key,
	owner			varchar2(20)	references members(user_id),
	name 			varchar2(20) not null,
	email			varchar2(50),
	cell_phone		varchar2(20),
	address 		varchar2(200),
	reg_date 		date 	default sysdate,
	update_date 	date 	default sysdate
);

drop sequence CONTACTS_SEQ;
create sequence CONTACTS_SEQ;

insert into CONTACTS (contact_id, owner, name, email, cell_phone, address) 
values(contacts_seq.nextval, 'hong', '허준', 'hj@naver.com', '010-2222-2222', null); 

insert into CONTACTS (contact_id, owner, name, email, cell_phone, address) 
values(contacts_seq.nextval, 'hong', '허균', 'hk@naver.com', '010-3333-3333', null); 

insert into CONTACTS (contact_id, owner, name, email, cell_phone, address)
values(contacts_seq.nextval, 'go', '둘리', 'dooli@naver.com', '010-4444-4444', null); 

insert into CONTACTS (contact_id, owner, name, email, cell_phone, address) 
values(contacts_seq.nextval, 'go', '마이콜', 'micole@naver.com', '010-5555-5555', null);

select * from CONTACTS;
