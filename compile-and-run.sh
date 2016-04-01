#!/bin/bash

echo "BIT needed for instrumentation"
git clone https://web.tecnico.ulisboa.pt/~jose.pedro.arvela/git/BIT
cd BIT
mvn compile install

echo "Will compile and install instrumentation"
cd ../instrumentation
mvn compile
mvn install

echo "Will compile and install web server"
cd ../web-server
mvn compile

echo "Will replace IntFactorization class for another that is instrumented"
cd ../instrumentation
mvn exec:java

echo "Running project"
cd ../web-server
mvn exec:java
