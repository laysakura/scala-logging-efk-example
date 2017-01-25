#!/bin/sh

traceroute elasticsearch

sleep 10

fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins &
/opt/docker/bin/verboseservice &

sleep 3

cat /etc/hosts
traceroute elasticsearch
telnet elasticsearch 9200
curl -vv http://elasticsearch:9200

tail -f /dev/null

