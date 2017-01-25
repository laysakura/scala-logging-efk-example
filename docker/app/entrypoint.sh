#!/bin/sh

fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins &
/opt/docker/bin/verboseservice &

tail -f /dev/null
