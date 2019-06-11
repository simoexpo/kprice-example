#!/usr/bin/env bash

docker run -d --rm --name elasticsearch --net kprice-example_default -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:6.8.0
