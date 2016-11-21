# Grouper's API Document in Untrusted Servers
This is the REST API document of Grouper Web service, which is a group finance manager application using multiple untrusted servers.

1. Group
====
(1)`group/register`
   
   - Register a new group in this untrusted server.
   - method: POST
   - param: 
      - id(String): group id
      - name(String): group name
   - return:
      - masterkey(String): master key for group creator
   - error:
      - ErrorGroupExsit(1001): Group id is exist in this server
      - ErrorGroupRegister(1002): Register group error
      
2. User
====
(1)`user/add`

   - Add a new user in this untrusted server.
   - method: POST
   - header:
      - key(String): master key
   - param: 
      - uid(String) : user id from facebook
      - name(String)
      - email(String)
      - gender(String)
      - pictureUrl(String): the picture url of user's avatar
      - owner(boolean): this user is group owner or not
   - return:
      - accesskey(String): access key for this new user
   -  error:
      - ErrorMasterKey(2001): Master key is wrong
      - ErrorAddUser(2002): Add user internel error
      
3. Transfer
====
(1)`transfer/send`

   - method: POST
   - param: 
      - sid(String): physical id in client
      - content(String): a share
      - object(String): class name of a object
   -  return:
      - count(int): sync times in this server
