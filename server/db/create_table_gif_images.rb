$LOAD_PATH.push('./db/')
require 'mysql2'
require 'mysql_client'

client = MysqlClient.get_client()

drop_query = <<-SQL
  DROP TABLE IF EXISTS gif_images
SQL
p drop_query
client.query(drop_query)

create_query = <<-SQL
  CREATE TABLE IF NOT EXISTS gif_images(
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(64) NOT NULL,
    image_url VARCHAR(64) NOT NULL
  ) CHARSET 'utf8'
SQL
p create_query
client.query(create_query)
