# About
This example displays behaviour of enrich component with dynamic resource which can lead to "Too many open files" issue.
When used like:
```xml
<enrich>
    <simple>${header.targetUrl}</simple>
</enrich>
```
where targetUrl is constantly changing:
```
http://localhost:4447/api/test/${id}
...
https4://locahost:4447/api/test/1
https4://locahost:4447/api/test/2
https4://localhost:4447/api/test3
...
```
then Camel opens single connection to every unique endpoint. It seems  like entire url is taken as "HttpPath" instead of only schema:hostname:port.

To access static hostname with variable resource path, one can use this solution to enforce proper connection pooling:
```xml
...
<setHeader headerName="CamelHttpPath">
    <constant>/api/test/1</constant>
</setHeader>
<enrich>
    <constant>direct:producer</constant>
</enrich>
...

<route>
    <from uri="direct:producer" />
    <to uri="http4://localhost" />
</route>
```

## Run the test example
- every test case will run and sleep for 60 seconds so we can see amount of connections
- run mvn test in one window and netstat command in another one when all test requests are sent
### 1. Multiple connections to multiple endpoints without pooling
Camel enrich/simple/https4 will open new connection to each unique endpoint

`mvn test -Dtest=TestConnectionsRemainOpen#testMultipleEndpointsWithoutConnPooling`

`ps -ef |grep maven | grep -v grep | awk  '{print $2}' | netstat --tcp --numeric| grep 4447`
### 2. Single connection to single endpoint
Camel enrich/simple/https4 will open single connection for all messages going to the same endpoint

`mvn test -Dtest=TestConnectionsRemainOpen#testSingleEndpointWithoutConnPooling`

`ps -ef |grep maven | grep -v grep | awk  '{print $2}' | netstat --tcp --numeric| grep 4447`
### 3. Single connection to multiple endpoints with pooling
Camel enrich/constant/direct/https4 will open single connection to multiple endpoints

`mvn test -Dtest=TestConnectionsRemainOpen#testMultipleEndpointsWithConnPooling`

`ps -ef |grep maven | grep -v grep | awk  '{print $2}' | netstat --tcp --numeric| grep 4447`

## Results
### 1. Multiple connections to multiple endpoints without pooling
There is one connection to web server for each message going to unique endpoint
### 2. Single connection to single endpoint
There is single connetion to web server for each message going to the same endpoint
### 3. Single connection to multiple endpoints with pooling
There is single connection to web server for each message going to the same endpoint because of synchronous nature of the test
