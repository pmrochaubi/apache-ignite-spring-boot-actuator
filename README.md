# Spring Boot + Apache Ignite + Spring Boot Actuator + TestNG

This is a sample project to showcase a Bug while trying to run tests (with TestNG) in a Spring Boot application that 
implements Spring Boot actuator and Apache Ignite. 


When we boot the app everything works as expected, although if we try to run tests they fail while trying to 
register Ignite SpringCacheManager on the CacheMetricsRegistrarConfiguration. 

# 1st Run the App with spring boot actuator

Run the app:

```bash
./gradlew clean bootRun
```

Run tests:

```bash
./gradlew clean test
```

As you may see the application fails to boot on the test environment with the following exception:

`org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'org.springframework.boot.actuate.autoconfigure.metrics.cache.CacheMetricsRegistrarConfiguration': Invocation of init method failed; nested exception is java.lang.AssertionError`

# 2nd Run the App without spring boot actuator

Edit the file `build.gradle` and on line `37` comment the line `implementation 'org.springframework.boot:spring-boot-starter-actuator:'+springBootVersion` 


Run the app:

```bash
./gradlew clean bootRun
```

Run tests:

```bash
./gradlew clean test
```

As you may see everything work as expected.