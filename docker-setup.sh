#!/usr/bin/env bash

docker exec broker bash -c "kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic users &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic users-verification &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic users-with-verification &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic valid-users &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic invalid-users &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic order-requests &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic orders-to-process &&
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic order-count"
