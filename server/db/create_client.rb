require 'mysql'

host = 'localhost'
user = 'user'
password = 'hogehoge'
database = 'dailymotion'
client = Mysql.connect(host, user, password, database)
