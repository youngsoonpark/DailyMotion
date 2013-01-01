$LOAD_PATH.push('./db/')
require 'mysql2'
require 'mysql_client'

client = MysqlClient.get_client()

image_url = nil
select_query = <<-SQL
  SELECT * FROM gif_images
SQL
p select_query

image_hash_array = Array.new
client.query(select_query).each do |row|
  image_hash_array.push(row)
end

image_hash_array.each do |image_hash|
  p image_hash
end


image_hash_array.each do |image_hash|
  id = image_hash["id"]
  image_url = image_hash["image_url"]

  if image_url.slice(0, 1) == "/"
    next
  end

  update_query = <<-SQL
    UPDATE gif_images SET image_url="/#{image_url}" WHERE id=#{id}
  SQL
  p update_query
  client.query(update_query)
end

