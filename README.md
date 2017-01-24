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
`/var/log/verboseService.yyyy-MM-dd.{error,warn,info,debug}.log`
  |
  v
td-agent (tcp:22422)
  |
--|---------------------
  |
--|- elasticsearch -----
  |
  v
Elasticsearch (tcp:9200)
  |
--|---------------------
  |
--|----- kibana --------
  v
kibana (tcp:5601)
  ^
  |
--|---------------------
  |
(^o^) < GREAT VIEW!
```

## 動作確認

### `VerboseService` のDocker imageを作成
```bash
$ sbt 'verboseService/docker:publish-local'

$ docker images
REPOSITORY                   TAG                 IMAGE ID            CREATED             SIZE
verboseservice               0.1.0-SNAPSHOT      fea05aa859e6        18 minutes ago      215 MB
```

### DockerでEFKスタックと `VerboseService` を立ち上げる
```bash
docker-compose up
```

### `VerboseServiceTest` を立ち上げ、ログを吐かせる
```bash
sbt test
```

### Kibanaで諸々確認


## 開発フロー

### Scalaプロジェクトをいじってイメージを作り直して `docker-compose up`
```bash
(docker images |egrep 'scalaloggingefkexample|verboseservice' |awk '{print $3}' |xargs docker rmi -f) && docker-compose rm && sbt 'verboseService/docker:publish-local' && docker-compose up
```

### 各種ログファイルの確認
ホスト側の `/tmp/scala-logging-efk-example/logs/` 以下に、ゲスト側の各ログが吐かれるようにボリュームマウントをしている。
