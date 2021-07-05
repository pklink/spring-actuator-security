# Spring Actuator Security Demo App

Simple App using Spring Actuator and Spring Security

## Running

```
mvn spring-boot:run
```

**as unauthorized user**

```shell
curl -X GET --location "http://localhost:8080/actuator/health"
```

should respond

```json
{
  "status": "UP"
}
```

**as authorized user**  

```shell
curl -X GET --location "http://localhost:8080/actuator/health" \
    -H "Authorization: Basic YWRtaW46YWRtaW4="
```

should respond something like 

```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 999648391168,
        "free": 154762776576,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}

```


## Test 

```
mvn test
```
