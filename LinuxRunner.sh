#!/bin/bash

mkdir bin
javac  -d bin Main/Server.java
java -cp bin Main.Server 4444
