1. Entity:

   1. User

      - id
      - name
      - email
      - password
      - phone
      - roles

   2. Wallet

      - userId
      - walletId
      - category [personal,corporate]
      - balance
      - list<transactionId> transactions;

   3. Transaction
      - id
      - category [recharge,transfer]
      - walletId
      - amount
      - dateTime

2. End-points:

   1. signup a user [+]
      - localhost:8080/api/user/signup POST
   2. login a user [+]
      - localhost:8080/api/user/login POST
   3. logout a user [-]

      - localhost:8080/api/user/logout POST

   4. create wallet [+]

      - localhost:8080/api/user/wallet POST

   5. recharge a wallet and give some cashback [+]
      - localhost:8080/api/user/transaction POST
   6. transfer from wallet [+]
      - localhost:8080/api/user/transaction PUT ***
   7. view account statement [-]
      - localhost:8080/api/user/wallet GET
