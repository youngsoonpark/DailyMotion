#require 'mysql'
require 'mysql2'

host = 'localhost'
username = 'betterflow'
password = 'hogehoge'
database = 'dailymotion'
#client = Mysql.connect(host, username, password, database)
client = Mysql2::Client.new(:host => host, :username => username, :password => password, :database => database)
