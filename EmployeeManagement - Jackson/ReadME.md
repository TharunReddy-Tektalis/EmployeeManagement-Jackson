# Employee Management System
 
-> Employee management system that provides different operations to manipulate employee data based on employee roles.
 
 
## Features
 
-- ADD, VIEW, DELETE, UPDATE, VIEW_BY_ID, CHANGE_PASSWORD, RESET_PASSWORD, GRANT_ROLE, REVOKE_ROLE,     FETCH_INACTIVE_EMPLOYEES, LOGOUT
-- Role based access control (ADMIN, MANAGER, USER)
-- Two persistant data storage - File system, Database
-- Follows MVC architecture
-- Input validations
-- Password hashing using SHA256
-- Implemented soft delete
-- Password follows specific format
-- Timestamps created (CreatedAt, UpdatedAt, PasswordChangedAt, DeletedAt) in database
-- Used triggers for automatic timestamp insertion into database

## Usage
 
###  Admin Login
 
- id = tek1
- password = Tek@1234
 
###  Manager Login
 
- id = tek3
- password = K8t%xzPr

###  User Login
 
- id = tek2
- password = Pen11n1%

 
## Project Structure
 
```
EmployeeManagement-Jackson/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.employee/
│   │   │
│   │   │   ├── controller/
│   │   │   │   ├── EmployeeController.java
│   │   │   │   ├── LoginController.java
│   │   │   │   └── MenuController.java
│   │   │   │
│   │   │   ├── dao/
│   │   │   │   ├── EmployeeDAO.java
│   │   │   │   ├── EmployeeDbDAOImpl.java
│   │   │   │   ├── EmployeeFileDAOImpl.java
│   │   │   │   └── ServerSideValidations.java
│   │   │   │
│   │   │   ├── enums/
│   │   │   │   ├── EMSLoginResult.java
│   │   │   │   ├── EMSOperations.java
│   │   │   │   ├── EMSRoles.java
│   │   │   │   ├── RolePermission.java
│   │   │   │   └── StorageTypes.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── DataAccessException.java
│   │   │   │   ├── EmployeeDoesNotExistException.java
│   │   │   │   └── ValidationException.java
│   │   │   │
│   │   │   ├── main/
│   │   │   │   └── EmployeeApp.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── Employee.java
│   │   │   │   ├── EmpLoginResult.java
│   │   │   │   └── UserContext.java
│   │   │   │
│   │   │   ├── services/
│   │   │   │   ├── EmployeeServices.java
│   │   │   │   └── LoginServices.java
│   │   │   │
│   │   │   └── util/
│   │   │       └── EmployeeUtil.java
│   │   │
│   │   └── resources/
│   │       ├── Postgres/
│   │       │   └── EMS_Script.sql
│   │       ├── employeeDetails.json
│   │       └── EmsDbConfig.properties
│   │
│   └── test/
│       ├── java/
│       └── resources/
│
└── pom.xml

```

## Execution flow

### Login flow

EmployeeApp (MAIN)
	↓
MenuController
    ↓
LoginController
    ↓
LoginServices
    ↓
EmployeeDAO.validateLogin()
    ↓
Database/File

### Operations flow

EmployeeApp (MAIN)
	↓
MenuController
    ↓
EmployeeController / LoginController
    ↓
Service Layer
    ↓
DAO Layer
    ↓
Database/File

