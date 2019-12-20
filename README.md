# bank-transactions
API to make transactions between bank accounts

## Main goal:
Create bank transactions api ensuring no money loss. Optimistic locking

### Created using:
<pre>
Java spark
jooq
guice
jackson
lombook
H2 in memory DB
</pre>

#### guide to run app: 
<pre>
mvn package
java -Dport=8000 -jar target/launchMe.jar 
</pre>

application running on : localhost:8000

#### GET api to view data:
<pre>

http://localhost:8000/accounts
http://localhost:8000/transactions
</pre>

#### to make transaction:
request:
<pre>

curl -X POST \
  http://localhost:8000/transactions \
  -H 'content-type: application/json' \
  -d '{
	"from":1,
	"to":2,
	"amount":700
}'
</pre>
example response:
<pre>

{"from":1,"to":2,"amount":700,"status":true}
</pre>
### to create account
<pre>
curl -X PUT \
  http://localhost:8000/accounts \
  -H 'content-type: application/json' \
  -d '{
	"owner":"myName",
	"balance":1000000
}'
</pre>
response:
<pre>
{"id":9,"owner":"myName","balance":1000000}
</pre>

### status codes
<pre>
200 - ok
201 - created
400 - bad request
500 - server error
</pre>
