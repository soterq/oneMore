# Simbox project description
**General goals:** Create an application that will allow users to make phone calls and send sms messages, ussd requests via web. Only authorized users are allowed to access the services.
**Components**
**1.** Front-end application (**Angular**).
*a. Display webpages (Login, Simbox), routing logic.
b. Connect to back-end.
c. Security (to be implemented)*.

***
**2. Back-end (**Java**)**
*a. Handle and generate responses at specific end-points. (Auth - remote LDAP server authentication, Simbox - webphone - WebRTC)
b. Ari Manager.
c. Local DB (store logs, call duration, simbox access info).*
***

#### Java App description
Init project using [quarkus](https://quarkus.io/get-started/) and add all the required dependencies.
###### Endpoints
@Auth
@Simbox

###### ARI Manager
Description

###### Ldap authentication
1. Setup local LDAP server: ```docker run --name ldap-service --hostname ldap-service --env LDAP_ORGANISATION="Fun International" --env LDAP_DOMAIN="fun.in" -p 389:389 -p 636:636 --detach osixia/openldap:1.1.8```
2. Set up LDAP admin to access the LDAP server DB: ```docker run -p 6443:443 -p 6080:80 --name phpldapadmin-service --hostname phpldapadmin-service --link ldap-service:ldap-host --env PHPLDAPADMIN_LDAP_HOSTS=ldap-host --detach osixia/phpldapadmin:0.7.2```
3. Access https://localhost:6443 using the following credentials: 	*Login DN: cn=admin,dc=fun,dc=in
	Password: admin*
4. Add users. [Here](https://www.techrepublic.com/article/how-to-populate-an-ldap-server-with-users-and-groups-via-phpldapadmin/) is a good guide on how to do that.
5. Code description

###### DB operations
User Entity
MariaDB
***
#### Angular App description
Modules
Services