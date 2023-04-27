#!/bin/bash
docker build \
  --build-arg GCP_PROJECT_ID=lofty-root-378503 \
  -t spring-rest-labs .
