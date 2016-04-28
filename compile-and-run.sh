#!/bin/bash

echo "Compile the entire project"
mvn -q compile

echo "Install instrumentation class to repository"
mvn -q -pl instrumentation install

echo "Replace IntFactorization class for another that is instrumented"
mvn -q -pl instrumentation exec:java -Dexec.args=./web-server/target/classes/pt/tecnico/cnv/webserver/IntFactorization.class

echo "Install the remaining projects"
mvn -q install

echo "Running project"
mvn -q -pl web-server exec:java
