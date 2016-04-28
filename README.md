# Project Nebraska

Projecto de CNV

* [Enunciado](http://grupos.ist.utl.pt/~meic-cnv.daemon/project/Enunciado_projecto_CNV_2015_16.pdf)
* [Tips and FAQ](https://docs.google.com/document/d/1Di1Eg4VCqRzGxdBUzTgcCMtlQB8kn716PUHNK68uqPA)

## Compilar o projeto

```bash
source setup-java.sh
mvn compile
mvn -pl instrumentation install
mvn -pl instrumentation exec:java -Dexec.args=./web-server/target/classes/pt/tecnico/cnv/webserver/IntFactorization.class
mvn install
```

## Correr o web server

```bash
mvn -pl web-server exec:java
```

## Correr o load balancer

```bash
mvn -pl load-balancer exec:java
```

