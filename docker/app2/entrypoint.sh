#!/bin/sh

fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins &
/opt/docker/bin/calculatorservice -Dlog.service.output=calculatorService.log &

tail -f /dev/null
