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
('Aarav Sharma', 'IT', '1995-03-12', 'Bangalore', 'aarav.sharma@gmail.com');

select * from empData;

INSERT INTO EmpLogin (empId, empPass) VALUES
('tek1', 'e4a298c1518c2a4df1c001f8146e6412aa290500f94adc2a8a9d22269d30c5eb'); // Tek@1234

select * from empLogin;

INSERT INTO EmpRole (empId, empRole) VALUES
('tek1', 'ADMIN');

select * from empRole;

alter table empData alter column empaddress type varchar(100);
alter table empData add column isActive BOOLEAN not null default true;

ALTER TABLE EmpData
ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP NULL,
ADD COLUMN deleted_at TIMESTAMP NULL;

ALTER TABLE EmpLogin
ADD COLUMN password_changed_at TIMESTAMP NULL;

create FUNCTION update_emp_on_role_change_trigger()
RETURNS TRIGGER AS $$
BEGIN
	update empData
	set updated_at = CURRENT_TIMESTAMP
	where empId = COALESCE(NEW.empId, OLD.empId);
	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER emp_role_change
AFTER insert or delete on empRole
FOR EACH ROW 
EXECUTE FUNCTION update_emp_on_role_change_trigger();

