#!/bin/sh

export _JAVA_OPTIONS="-XX:-UseSplitVerifier"
cd bin/
java pt.tecnico.cnv.webserver.WebServer
