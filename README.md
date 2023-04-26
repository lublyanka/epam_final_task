# epam_final_task
## Credit Cards system storage


The customer registers in the system and has one or more Credit Cards, each of which corresponds to a specific Account in the system. 
The Client may make a Payment using the Account.

The payment has one of two statuses: 'prepared' or 'sent'. (Optional: implement the ability to generate a pdf-report on payment).

The client has a personal account where he can view information about his payments and accounts. 
It is necessary to implement the possibility of sorting: 
- payments: 
  1. by number; 
  2. by date (old to new, new to old); 
- accounts: 
  1. by number; 
  2. by account name; 
  3. by the balance amount on the account. 

The client can replenish or block one of his accounts. 
To unlock the account, the customer must make a request to unlock.

The system administrator has the rights:
- blocking / unblocking the user; 
- blocking / unblocking one of the user's accounts.


## Configuration
### Add [application.properties]
```
server.port=8080

jwt.secret= *put here real jwt.token*

spring.datasource.url=jdbc:postgresql: *put here real db host+name*
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username= *put here real db user*
spring.datasource.password= *put here real db pass*

spring.postgres.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.hibernate.type_contributors=org.hibernate.type.contributor.BasicTypeContributor


org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG
```
