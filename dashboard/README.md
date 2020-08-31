## A simple dashboard showing incoming orders data and displaying it by states.

#### Running Locally
`cd ~/kafka_2.12-2.5.0`

#### Run zookeeper
`bin/zookeeper-server-start.sh config/zookeeper.properties`

#### Start Kafka Server
`bin/kafka-server-start.sh config/server.properties`

#### Start sending messages
`bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic dms-blog`
`bin/kafka-console-producer.sh --bootstrap-server "b-2.mskmmcluster1.x1z1i4.c2.kafka.ap-southeast-1.amazonaws.com:9094,b-1.mskmmcluster1.x1z1i4.c2.kafka.ap-southeast-1.amazonaws.com:9094" --topic dms-blog`



#### Run application
`java -jar app/springboot-msk-viewer-1.0.jar`