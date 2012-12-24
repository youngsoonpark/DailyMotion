$LOAD_PATH.push('./db/')
require 'mysql2'
require 'mysql_client'

client = MysqlClient.get_client()

altertable_query = <<-SQL
  ALTER TABLE gif_images
  ADD COLUMN like_count INT(11) DEFAULT 0 NOT NULL
SQL
p altertable_query
client.query(altertable_query)

