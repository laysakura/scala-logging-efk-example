#!/bin/sh

fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins &
/opt/docker/bin/calculatorservice &

tail -f /dev/null
