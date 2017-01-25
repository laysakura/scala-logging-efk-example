FROM verboseservice:0.1.0-SNAPSHOT

USER root

## 必要パッケージのインストール
RUN apk update
RUN apk add openjdk8

## パーミション
RUN chown -R fluent:fluent /opt/docker

## entrypoint.sh の配置から立ち上げまで
ADD entrypoint.sh /opt/
RUN chown fluent:fluent /opt/entrypoint.sh
USER fluent
RUN chmod 755 /opt/entrypoint.sh
ENTRYPOINT /opt/entrypoint.sh
