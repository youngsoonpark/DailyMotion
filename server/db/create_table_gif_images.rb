require 'mysql2'

client = Mysql2::Client.new(:host => "localhost", :username => "betterflow", :password => "hogehoge", :database => "dailymotion")

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

