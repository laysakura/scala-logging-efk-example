# scala-logging-efk-example
EFK (Elasticsearch, Fluentd, Kibana) スタックを使って、finagleアプリケーションのログを集約・閲覧する例。

## 構成図
```
------ localhost -------

VerboseServiceTest
  |
--|---------------------
  |
--|------ app ----------
  |
  v
VerboseService (tcp:4000)
  |
  v
`/app/scala-logging-efk-example/logs/application.yyyyMMdd.{error,warn,info,debug}.log`
  |
  v
td-agent (tcp:22422)
  |
--|---------------------
  |
--|-- log-collector ----
  |
  v
Elasticsearch (tcp:9200)
  |
  v
kibana (tcp:5601)
  ^
  |
--|---------------------
  |
(^o^) < GREAT VIEW!
```

## 動作確認

### DockerでEFKスタックと `VerboseService` を立ち上げる
```bash
docker-compose up
```

### `VerboseServiceTest` を立ち上げ、ログを吐かせる
```bash
sbt test
```

### Kibanaで諸々確認
