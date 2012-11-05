#!/bin/sh
javac -d projetRobot/bin -sourcepath src -classpath $CLASSPATH:projetRobot/bin:projetRobot/lib/junit-4.1.jar:projetRobot/lib/jmock-1.1.0.jar `find projetRobot/src -name "*.java"`
