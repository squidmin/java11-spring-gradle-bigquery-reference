#!/bin/bash
exec java -jar \
  -Dspring.profiles.active=local \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
  ./build/libs/spring-rest-labs-0.0.1-SNAPSHOT.jar
