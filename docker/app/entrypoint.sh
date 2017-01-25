#!/bin/sh

fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins &
/opt/docker/bin/verboseservice &

sleep 10

cat /etc/hosts
telnet elasticsearch 9200
curl http://elasticsearch:9200

tail -f /dev/null
