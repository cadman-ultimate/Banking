# Banking

Prerequisites:
JDK: 1.8
Maven

Run Application:
Open cmd and locate project folder directory and execute:
./mvnw spring-boot:run

Eclipse Instructions:
Right-click on project
Run As > Java Application or Spring Boot App

Host = localhost:8080

Get all accounts - GET:
/accounts/all

Get Balance - GET:
/accounts/balance/{accountNumber}

Add account - POST:
/accounts/add
Request Body sample:
{
    "firstName" : "Joe",
    "lastName" : "Bloggs",
    "accNumber" : "11111111",
    "email" : "Joe.Bloggs@Google.com"
}

Add funds to account - PATCH:
/accounts/funds/add
Request Params:
Key: "amount", Value 100.00
Key: "accountNumber", Value "12345678"

Withdraw funds from account - PATCH:
/accounts/funds/withdraw
Request Params:
Key: "amount", Value 100.00
Key: "accountNumber", Value "12345678"

Transfer funds between accounts - PATCH:
/accounts/funds/add
Request Params:
Key: "amount", Value 100.00
Key: "fromAccount", Value "12345678"
Key: "toAccount", Value "98765432"

Assumptions
- Error Handling kept at basic exception level
- Application does not have validation or pre-requisites for account number
- Account number is entered manually in the request body with name, is it not auto generated by the code
- Only small number of unit tests added to demonstrate testing style
- Not accounting for any vulernatbilities e.g. SQL injection, passing account number in url to get balance for example
- Transferring funds does not give new balances that can be checked through the check balance call
- Transferring funds is done by 2 update calls to satisfy HSQLDB, would be changed to 1 update with case method in SQL method