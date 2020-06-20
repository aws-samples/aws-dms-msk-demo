## AWS DMS to Amazon MSK Demo App

Streaming data to Amazon MSK via AWS DMS and consuming it.
The repository contains the sample code for the blog post "Streaming data to MSK using AWS DMS"

### It contains three modules
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

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This library is licensed under the MIT-0 License. See the LICENSE file.

