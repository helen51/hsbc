# HSBC

The project contains basically three layers, entity, repository and service.

#### entity
For entity layer, there are three entities, User, Role and Token.
User entity consists of userName, encrypted password and a set of roles.
Role entity consists of roleName;
Token entity consists of tokenName and expiredTime;

#### repository
For repository layer, there are four tables, User, Role, Token and TokenUser.
User table stores user info: userName, encrypted password and set of roles assigned to the user.
Role table stores role info: roleName;
Token table stores token info:  tokenName and expiredTime.
TokenUser table stores the relationship between user and token using userName and tokenName. This is a 1-on-1 mapping.

#### service (API)
For service layer, it provides the following APIs:
* createUser - create user for input userName and password

* deleteUser - delete user for input userName

* createRole - create role for input roleName

* deleteRole - delete role for input roleName

* addRoleToUser - add intut role to input 

* authenticate - authenticate the input username and password, update token if user login correctly

* invalidate - invalidate the token immediately

* checkRole - check whether the input role associated with the user of the input token

* allRoles - get all the roles associated with the user of the input token

*（setTokenExpireSecond api is created here for Junit test purpose. It can modify the default expiring time of token in second unit);

#### Design

![Design diagram](https://github.com/helen51/hsbc/blob/main/src/main/resources/design.png)

#### To Execute

Open the project in IntelliJ with Maven build. Run junit test for all the APIs
