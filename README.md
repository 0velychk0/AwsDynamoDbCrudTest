# AwsDynamoDbCrudTest
Aws Dynamo Db Crud Test. Spring Service, Java 11

Example 1:
https://github.com/uningsky/springboot-dynamodbmapper

Example 2:
https://github.com/xingtanzjr/DynamoDBAPITest

How to deploy via google apps engine console

Open console and run command to get your url

gcloud app browse

Open console and run next commands to deploy

git clone https://github.com/0velychk0/AwsDynamoDbCrudTest.git

cd AwsDynamoDbCrudTest

mvn -DskipTests package appengine:deploy
