# Spring config server with git and vault example
> In this example we will pull configuration from git as well as from vault using spring cloud config server

## Reference Documentation
For further reference, please consider the following sections:

* [Config Server](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_server)
* [Hashicopr Vault](https://developer.hashicorp.com/vault/docs?product_intent=vault)


## Setup
> Public Repo to store configuration
* Create a public git repo and add yaml/properties file using below naming conventions
```sh
{application}-{profile}.properties
{application}-{profile}.yml
# Example
my-application-development.yml
my-application-staging.yml
my-application-production.yml
```
* For more details on naming convension please refer [here](org.springframework.cloud.config.server.config)
> Here application represent spring cloud config client application name and profile represent respective active profile like dev, qa, prod etc


> Vault can be installed on local machine using vault installer or same could be executing on docker using below config.
* [Installation guide](https://developer.hashicorp.com/vault/install)
* Please regere [here](/vault-setup/docker-compose.yaml) to run it on docker
```yaml
version: '3.8'
services:
  vault:
    image: hashicorp/vault
    container_name: hashicorp-vault
    ports:
      - "8200:8200"
    environment:
      VAULT_DEV_ROOT_TOKEN_ID: "root" # Change it with strong token for prod
    volumes:
      - ./vault/data:/vault/data
    cap_add:
      - IPC_LOCK # Required for Vault, it prevents secret swapping out from main memory
```
> Vault will be running on port 8200 and it its ui can be accessible using ```localhost:8200/ui/``` 

## Steps to add configuration in vault
### Using vault UI
* Navigate to vault dashboard and add key value pair for the required path
### Using vault command
> Use below command to add key/value pair for the required path
```sh
vault write <path> key1=val1 key2=val2
```
```sh
vault write cubbyhole/git-credentials username="student01" password="p@$$w0rd"
```


## Creating Spring cloud config server spring boot application
> Create spring boot app using [spring initlizer](https://start.spring.io/) or using your favourite IDE.
* Add following dependencies in pom.xml/build.gradle
```
implementation 'org.springframework.cloud:spring-cloud-config-server'
```
* Add ```@EnableConfigServer``` to mark current application as spring cloud config server application
* Update application.yml/application.properties like below
```yml
spring:
  application:
    name: config-server  # Spring application name
  profiles:
    active:  # Lsit of all the active profiles
    - vault
    - git
  cloud:
    config:
      server:
        git:   # Configuring git server to pull required config
          uri: https://github.com/rajpiyush220/config-server-vault.git # git url created in above steps
          clone-on-start: true 
          skipSslValidation: true
          default-label: master  # default branch name 
          order: 2  
        vault:  # configuring valut server
          kv-version: 2
          order: 1    # Specifying order to assign priotiry for server
server:
  port: 8888
```

## Execution steps
* Start spring boot app and it will connect with git repo to pull all the configuration and store it in local repo
* App will not connect with vault untill we send config access request as we have not supplied vault token in config

## How to access config using spring config clent application
* Use below url to access configuration from client app
```sh
curl --location 'http://localhost:8888/myapp/dev' --header 'X-Config-Token: root'
```

> **Explanation** : http://[config-server url]:[config-server port]/[config client name]/[config client profile]

## How does vault find respective props to display
> Vault will look for **/secret/[config client name]/[config client profile]** path, if present it will return all the configuration along with default configuration for **/secret/[config client name]**. If respective profile is not present then it will only return default configuration of the respective application.

## How does config server find respective props to display
> Config server will look for [config client name]-[config client profile].properties or [config client name]-[config client profile].yaml file in git repo if present it will return respective profile config with default config and if not present it will only return default config for the respective app.


## Pending items
* More details on path of vault and git config
* Usage of @RefreshScope to refresh config withouot restating client application





