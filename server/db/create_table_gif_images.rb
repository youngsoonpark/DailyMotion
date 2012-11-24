require 'mysql'

client = Mysql.connect('localhost', 'betterflow', 'hogehoge', 'dailymotion')

client.query("DROP TABLE IF EXISTS gif_images")
client.query("CREATE TABLE IF NOT EXISTS gif_images(" +
             "id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
             "title VARCHAR(64) NOT NULL," +
             "image_url VARCHAR(64) NOT NULL);")

