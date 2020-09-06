## Streaming Data to Amazon MSK via AWS DMS

This repository provides you cloudformation scripts and sample code on how to implement 
end to end pipeline for replicating transactional data from MySQL DB to Apache Kafka.

This is a companion source code for the blog post "Streaming data to MSK using AWS DMS"
### Streaming Pipeline  
Below diagram shows what we are implementing.

![Alt text](content/images/DMS-MSK-Pipeline.png?raw=true "Pipeline")
### Repository Modules And Folders
 * dashboard
    * A simple dashboard showing incoming orders data and displaying it by states.
    * This module contains springboot based Kafka listener. 
    it depicts how a custom application can be built to listen to incoming stream of 
    data in kafka topics and sent to a live dashboard. 
    * It leverages websocket connection to connect to server.
    * It uses open source chartjs for building simple graph on the data.
 * data-gen-utility
   * This small command line utility can be used to generate dummy order data 
   that can be fed to source mysql database. 
 * msk-to-s3-feeder
    * Independent springboot application that shows how you can take streaming data from 
    Amazon MSK and implement batch listener to club streaming data and feed to S3 bucket
    provided by user in one or more objects.
 * content & bin folders: Content contains the cloudformation for the automatic creation of the pipeline.
 Bin folder contains the binaries to run tests. You can also generate them yourself using commands mentioned 
 in the build section.


### Setup the pipeline
* Refer the documentation and create a dms-vpc-role. [Create IAM Role](https://docs.aws.amazon.com/dms/latest/userguide/CHAP_Security.html#CHAP_Security.APIRole)
* Run the cloudformation at content/cfn folder in your account. Or just click below button <br />
 [![Launch Stack](https://cdn.rawgit.com/buildkite/cloudformation-launch-stack-button-svg/master/launch-stack.svg)](https://us-east-2.console.aws.amazon.com/cloudformation/home?region=us-east-2#/stacks/create/review?templateURL=https://github.com/aws-samples/aws-dms-msk-demo/blob/master/content/cfn/master-cfn.yaml&stackName=Streaming-DMS-MSK-Pipeline)
* Enter required parameters and click create.
* Your pipeline should be up and running in 15-20 mins.

### Setup the sample Applications and then the testing pipeline
* Login to the client ec2 instance created by the cloudformation.
* Download the sample code via below command
    ```
    git clone https://github.com/aws-samples/aws-dms-msk-demo.git
    ```
* Run the below commands to build the applications
    
  ```
    cd aws-dms-msk-demo
    
    mvn clean install
  ```
* Connect to Mysql by running the below command. Replace the db host name by Aurora
db host endpoint that was created by the cloudformation. It can be found from the RDS console under Connectivity & 
endpoint section. Default username is 'master' and default password is 'Password1'. testdb is the default DB getting created
via cloudformation.
  ```
    mysql –u <username> -p -h <hostname or IP address> testdb 
  ```   

* At SQL prompt run the below command to create the sample table named ‘orders’ in database: ‘testdb’.
    ```
    SQL > create table orders (orderid bigint(20) NOT NULL,
    source varchar(45) NOT NULL default 'andriod',
    amount varchar(45) NOT NULL default '0',
    state varchar(45) NOT NULL default 'New Jersey',
    date datetime NOT NULL default current_timestamp,
    Primary key (orderid));
    ```     
     
        
* Also run this to AWS DMS has bin log access that is required for replication
   ```
    call  mysql.rds_set_configuration('binlog retention hours', 24);
   ```    
* Hit cmd + z and come out of the SQL prompt.Run the below command to launch the dashboard in client ec2 instance. You have to replace the broker endpoints before running.
These can be found in MSK cluster's (_created by cloudformation above_) client information. 
[Refer this link](https://docs.aws.amazon.com/msk/latest/developerguide/msk-get-bootstrap-brokers.html) to get broker list.

Note: Get the plaintext link and not the TLS as it requires some extra configuration at client side to work. 
[Refer this link](https://docs.aws.amazon.com/msk/latest/developerguide/msk-authentication.html) if you want to connect via TLS.  
    ```
    java –jar aws-dms-msk-demo/dashboard/target/dashboard-1.0.jar --kafka.bootstrapEndpoints=<broker-endpoint>:9092 –-kafka.topic=dms-blog
    ```
* From your laptop's browser open http://<Public_IP_of_the_EC2_instance>:8080/
    You should see something like below screen
   ![Alt text](content/images/screen-1.png?raw=true "Pipeline")
   
* Generate test data and test the dashboard.
    * Open a new ssh session to the client EC2.
    * Use the datagen.jar utility present in the cloned git repo to generate sample data in bulk of 2000 records.
    
    ```
    java –jar aws-dms-msk-demo/data-gen-utility/target/datagen.jar
    ```
    * When prompted enter 2000 for records and 1 for start index.
        *    *.sql file is generated with 2000 dummy order records.
    * Connect to the database again using below command. This will insert all your dummy 
    data into your Aurora mysql DB that was generated via CloudFormation.
    
    ```
    mysql –u <username> -p database_name –h <hostname or IP> testdb <xxx.sql 
    ```
    * Now, let’s Start DMS task via aws cli so as our data starts getting replicated to MSK. Before running replace the 
    DMS task ARN. You can find it in the AWS Console, under DMS service.
    
    ```
    aws dms start-replication-task –-replication-task-arn <dms task arn> --start-replication-task-type start-replication
    ```
    * Check the dashboard and you will see graph updating on it.
    
    * You have successfully created the pipeline and transferred the data. Feel free to checkout the code or 
    insert/remove/update data from your database. It should get reflected on your board.  

## CleanUp
*	Stop the DMS Replication task by replacing the ARN in below command.
    ```
    aws dms stop-replication-task –-replication-task-arn <dms task arn>
    ```
*   Delete the CloudFormation stack.
*	Clean the resources that are dynamically created.
     * Go to Services, then DMS and click endpoints in the left navigation.
     *	Delete “dms-blog-kafka-target” DMS endpoints.
*	Delete any CloudWatch log-groups if got created.
     * Go to Services, then CloudWatch and click “Log groups” in the navigation pane.    
     * Delete any Log groups with name “Streaming-DMS-MSK” or use the stack name if you changed it from default while creating the stack.
    
## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This library is licensed under the MIT-0 License. See the LICENSE file.

