
# Spring-Vault Implementation

## Getting Started
## Reference Documentation
For further reference, please consider the following sections:

* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#web)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#features.docker-compose)
* [Vault Client Quick Start](https://docs.spring.io/spring-cloud-vault/docs/current/reference/html/#client-side-usage)

## Vault Setup 
### Steps to set up vault Windows
* [Vault Download Link](https://www.vaultproject.io/downloads)
* Copy file at [Vault Config](/spring-vault/vaultconfig.hcl) at the same location as vault.exe
* Start vault server using below command
  ```vault server -config ./vaultconfig.hcl```
* Left it running and open another command prompt to run below command
    1. ```set VAULT_ADDR=http://localhost:8200 [Keep the same port defined in config file]```
    2. ```set VAULT_SKIP_VERIFY=true # Not recomonded for prod```
    3. ```vault operator init```
* Above command will generate some key and token like below, please keep it safe
  ```
    Unseal Key 1: W6pi/vcJarZirLPfogfuaDxJbhTntMFCv+c81TmI0sKm
    Unseal Key 2: SdfKQ7Z1zuFvv8V5Gv1SonDJOgyNzsGor/f92Naq2dlK
    Unseal Key 3: mWcbmIXdpzcDh1ehchYaqedS3eFFofOIZ63628Db3/tm
    Unseal Key 4: M26bzvgfOmoqKm/bUbBozUkQEvHIG0BuYpMHwDwpwzVH
    Unseal Key 5: B+jKNb5DHfzoEp0l66MLkewIHdt9GYPJKXYa/uObtitg

    Initial Root Token: hvs.rhFwCWNomlCek24Qe57N5uUD
  ```
* Run set of below command to proceed further
    1. ```set VAULT_TOKEN=<above generated token>```
    2. ```vault status```
    3. Its time to unseal vault because above command will show vault is sealed
    4. ```vault operator unseal <any 3 key generate by init command>```
    5. Run ```vault status``` to check whether vault is unsealed or not
    6. Run ```vault secrets enable -path=secret/ kv   ``` to enable key value store with named secret
    7. Run ```vault kv put secret/<app_name> key=value key1=value``` to add key
        * <app_name> --> This will be used to access secrets and same app name will be used as app name in client
        * key -> key name could be prefix.keyname and same prefix can be used as prefix for ConfigProperties annotation
    8. This ```vault kv get secret/<app_name>``` command can be used to get values from vault
### Steps to set up vault on Docker
> Please refer [docker-compose file](compose.yaml) and use below command to start vault
```sh
    docker-compose up -d
```

### Storing secret
#### Using vault ui 
> We have to enable vault UI in prod, it would be enabled in dev \


> Please [refer here](localhost:8200/dashboard) to add secret using UI

##### Command to create secret in vault
```shell
vault kv put secret/gs-vault-config example.username=demouser example.password=demopassword
vault kv put secret/gs-vault-config/cloud example.username=clouduser example.password=cloudpassword
# Above command will add two different path gs-vault-config and gs-vault-config/cloud
```
> gs-vault-config will be treated as default profile secrets and gs-vault-config/cloud will be treated as secrets for cloud profile
    
### For more details on usage please refer Source code



