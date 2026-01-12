create sequence EmpData_empId_seq start 1;

create table EmpData (
	empId varchar(20) primary key default 'tek' || nextval('EmpData_empId_seq'),
	empName varchar(50) not null,
	empDept varchar(20) not null,
	empDOB date not null,
	empAddress varchar(20) not null,
	empEmail varchar(50) unique not null
);

create table EmpLogin (
	empId varchar(20) primary key,
	empPass varchar(64) not null,

	constraint fk_emp_login 
		foreign key (empId)
		references EmpData(empId)
		on delete cascade
);

create type RoleEnum as enum ('ADMIN','MANAGER','USER');

create table EmpRole (
	empId varchar(20),
	empRole RoleEnum,

	primary key(empId, empRole),

	constraint fk_emp_role 
		foreign key (empId)
		references EmpData(empId)
		on delete cascade
);

INSERT INTO EmpData (empName, empDept, empDOB, empAddress, empEmail) VALUES
('Aarav Sharma', 'IT', '1995-03-12', 'Bangalore', 'aarav.sharma@gmail.com'),
('Rohan Verma', 'General', '1995-07-19', 'Secundarabad', 'rohan.verma@gmail.com'),
('Vikram Singh', 'Finance', '1992-11-05', 'Mumbai', 'vikram.singh@gmail.com'),
('Kunal Mehra', 'Sales', '1997-02-18', 'Ahmedabad', 'kunal.mehta@gmail.com'),
('Rahul Nair', 'IT', '1994-09-09', 'Kochi', 'rahul.nair@gmail.com'),
('Suresh Rao', 'Operations', '1991-06-27', 'Hyderabad', 'suresh.rao@gmail.com'),
('Ankit Gupta', 'Marketing', '1998-02-14', 'Jaipur', 'ankit.gupta@gmail.com'),
('Pranav Joshi', 'Support', '1995-10-30', 'Pune', 'pranav.joshi@gmail.com'),
('Arjun Reddy', 'IT', '1990-12-11', 'Warangal', 'arjun.reddy@gmail.com'),
('Nikhil Bansal', 'Finance', '1996-05-03', 'Chandigarh', 'nikhil.bansal@gmail.com'),
('Amit Kulkarni', 'Operations', '1993-08-22', 'Nagpur', 'amit.kulkarni@gmail.com'),
('Siddharth Jain', 'Sales', '1997-04-17', 'Indore', 'siddharth.jain@gmail.com'),
('Deepak Yadav', 'HR', '1992-09-29', 'Gurgaon', 'deepak.yadav@gmail.com'),
('Manish Patel', 'Marketing', '1998-06-06', 'Surat', 'manish.patel@gmail.com'),
('Sanjay Mishra', 'Finance', '1994-01-19', 'Varanasi', 'sanjay.mishra@gmail.com');

select * from empData;

INSERT INTO EmpLogin (empId, empPass) VALUES
('tek1', 'a91878a94357e9a8082a2dee1b070ad7408c76e224e643b76a7690bbe47241c7'),
('tek2', 'a91878a94357e9a8082a2dee1b070ad7408c76e224e643b76a7690bbe47241c7'),
('tek3', 'a91878a94357e9a8082a2dee1b070ad7408c76e224e643b76a7690bbe47241c7'),
('tek4', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek5', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek6', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek7', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek8', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek9', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek10', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek11', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek12', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek13', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek14', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c'),
('tek15', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c');

select * from empLogin;

INSERT INTO EmpRole (empId, empRole) VALUES
('tek1', 'ADMIN'), ('tek1', 'USER'), ('tek1', 'MANAGER'),
('tek2', 'USER'),
('tek3', 'MANAGER'),
('tek4', 'USER'),
('tek5', 'ADMIN'),
('tek6', 'MANAGER'),
('tek7', 'USER'),
('tek8', 'USER'),
('tek9', 'MANAGER'),
('tek10', 'USER'),
('tek11', 'ADMIN'),
('tek12', 'USER'),
('tek13', 'MANAGER'),
('tek14', 'USER'),
('tek15', 'ADMIN');

select * from empRole;
