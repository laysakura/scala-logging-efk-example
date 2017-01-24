FROM verboseservice:0.1.0-SNAPSHOT

ADD entrypoint.sh /opt/
RUN chown fluent:fluent /opt/entrypoint.sh
USER fluent
RUN chmod 755 /opt/entrypoint.sh

ENTRYPOINT /opt/entrypoint.sh
