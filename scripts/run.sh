#!/bin/bash

mkdir -p ./data

docker run --name mysqldb -p3306:3306 \
	-e MYSQL_DATABASE=spread \
	-e MYSQL_USER=manager \
	-e MYSQL_PASSWORD=123qwe \
	--volume $PWD/data:/var/lib/mysql \
	--detach \
	mysql:5.7
