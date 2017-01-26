# scala-logging-efk-example
EFK (Elasticsearch, Fluentd, Kibana) スタックを使って、finagleアプリケーションのログを集約・閲覧する例。

## 構成図
```
------ localhost -------

VerboseServiceTest
  |
--|---------------------
  |
--|--------- app1 ------------ | --------------- app2 ---------------
  |                            |
  v                            |
[ VerboseService (tcp:4000) ] ---> [ CalculatorService (tcp:5000) ]
  |                            |       |
  v                            |       v
`/var/log/verboseService.log`  |   `/var/log/calculatorService.log`
  |                            |       |
  v                            |       v
[ td-agent (tcp:22422)      ]  |   [ td-agent (tcp:22422)          ]
  |                            |       |
--|--------------------------- | ------|-----------------------------
  |                                    |
--|------------------ elasticsearch ---|-----------------------------
  |                                    |
  v                                    v
[                     Elasticsearch (tcp:9200)                     ]
                               |
-------------------------------|-------------------------------------
                               |
--------------------- kibana --|-------------------------------------
                               v
[                     kibana (tcp:5601)                            ]
                               ^
                               |
-------------------------------|-------------------------------------
                               |
                      (^o^) < GREAT VIEW!
```

## 動作確認

### `VerboseService` , `CalculatorService` のDocker imageを作成
```bash
$ sbt 'verboseService/docker:publish-local'
$ sbt 'calculatorService/docker:publish-local'

$ docker images
REPOSITORY                   TAG                 IMAGE ID            CREATED             SIZE
verboseservice               0.1.0-SNAPSHOT      fea05aa859e6        18 minutes ago      215 MB
calculatorservice            0.1.0-SNAPSHOT      ad5274fe1ac9        34 seconds ago      228 MB
```

### DockerでEFKスタックと各種サービスを立ち上げる
```bash
docker-compose up
```

### `VerboseServiceTest` を立ち上げ、ログを吐かせる（推奨）
```bash
sbt test
```
多様なアプリログを吐くための手順。各種サービスの起動ログはテストを走らせないでも出力されていてKibanaで見られるので、必須ではない。

### Kibanaからログの確認
- http://localhost:5601 にアクセス
- インデックス名を `fluentd*` , time-field名を `@timestamp` に設定
  ![インデックス指定画面](https://cloud.githubusercontent.com/assets/498788/22278137/6486a90a-e304-11e6-89f2-a24a59d9a803.png)
- Discoverタブから色々見られる。
  ![ログを見る画面](https://cloud.githubusercontent.com/assets/498788/22278127/53ee53b8-e304-11e6-8e85-ae6a09f26bbd.png)

#### traceIdによる絞込
これにより、クライアントからの1つのリクエストに対するレスポンスを返すのに複数のサービスが関わっていたとしても、その1つのリクエストで起こったことをサービス横串で確認できる。

![traceIdによる絞込](https://cloud.githubusercontent.com/assets/498788/22317159/5c1c7770-e3b5-11e6-9335-2deff3f57e6d.png)

## 開発フロー

### Scalaプロジェクトをいじってイメージを作り直して `docker-compose up`
```bash
(docker images |egrep 'scalaloggingefkexample|verboseservice|calculatorservice' |awk '{print $3}' |xargs docker rmi -f) && docker-compose rm && sbt 'verboseService/docker:publish-local' && sbt 'calculatorService/docker:publish-local' && docker-compose up --build
```

### 各種ログファイルの確認
ホスト側の `/tmp/scala-logging-efk-example/logs/` 以下に、ゲスト側の各ログが吐かれるようにボリュームマウントをしている。
