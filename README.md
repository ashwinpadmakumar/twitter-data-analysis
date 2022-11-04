# Twitter-Data-Analysis

Twitter data analysis is a event driven pipelined project which is used to fetch,store and analyze twitter data based on the given trending topics. 

Workflow:
- A twitter consumer client (HoseBird Client) is used to consume tweets from the twitter.
- Using a kafka producer client, the consumed data from twitter is produced to a message queue topic (Kafka) to handle high throughput of data from the client.
- A kafka consumer client is used to consume data from the kafka and then an elasticsearch client is used to produce the data to elasticsearch.

Further the data accumulated in the elasticsearch can be visualized using Kibana.