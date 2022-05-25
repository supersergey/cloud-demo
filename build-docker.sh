#!/bin/bash
cd gateway || exit
docker build -t kadmos-demo/gateway .
cd ..
cd account-service || exit
docker build -t kadmos-demo/account-service .