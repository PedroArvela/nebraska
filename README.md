# Project Nebraska

Project for the class of Cloud Computing and Virtualization.

## Compiling

```bash
source setup-java.sh
mvn compile
mvn -pl instrumentation install
mvn -pl instrumentation exec:java -Dexec.args=./web-server/target/classes/pt/tecnico/cnv/webserver/IntFactorization.class
mvn install
```

## Running the web server

```bash
mvn -pl web-server exec:java
```

## Running the load balancer

```bash
mvn -pl load-balancer exec:java [ -Dexec.args=Scheduler [ PORT ] ]
```

