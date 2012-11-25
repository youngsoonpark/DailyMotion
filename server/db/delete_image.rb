$LOAD_PATH.push('./db/')
require 'mysql2'
require 'mysql_client'

id = ARGV[0]

if (id.nil?)
  p "argv is not enough"
  exit()
end

client = MysqlClient.get_client()

image_url = nil
select_query = <<-SQL
  SELECT * FROM gif_images WHERE id=#{id}
SQL
p select_query
client.query(select_query).each do |row|
  image_url = row["image_url"]
end

if (!image_url.nil?)
  system('rm public/' + image_url)
end

delete_query = <<-SQL
  DELETE FROM gif_images WHERE id=#{id}
SQL
p delete_query
client.query(delete_query)
