#!/bin/sh

fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins &
/opt/docker/bin/verboseservice -Dlog.service.output=verboseService.log &

tail -f /dev/null
