$LOAD_PATH.push('./db/')
require 'mysql2'
require 'mysql_client'


class ImageStore

  def initialize
    @client = MysqlClient.get_client()
  end

  def get_image_hash
    image_hash = Hash.new
    select_query = <<-SQL
      SELECT title, image_url FROM gif_images ORDER BY id DESC
    SQL
    p select_query
    @client.query(select_query).each do |row|
      image_hash[row["title"]] = row["image_url"]
    end

    return image_hash
  end

  def get_image_json
    image_array = Array.new
    select_query = <<-SQL
      SELECT id, title, image_url, like_count FROM gif_images ORDER BY id DESC
    SQL
    p select_query
    @client.query(select_query).each do |row|
      image_hash = {
        'id'         => row['id'],
        'title'      => row['title'],
        'image_url'  => row['image_url'],
        'like_count' => row['like_count'],
      }
      image_array.push(image_hash)
    end

    return image_array.to_json
  end

  def get_last_id
    last_id = 1
    select_query = <<-SQL
      SELECT id FROM gif_images ORDER BY id DESC LIMIT 1
    SQL
    p select_query
    @client.query(select_query).each do |row|
      last_id = row["id"].to_i + 1
    end

    return last_id
  end

  def save_image(title, image_url)
    insert_query = <<-SQL
      INSERT INTO gif_images (title, image_url)
      VALUES ('#{title}', '#{image_url}')
    SQL
    p insert_query
    @client.query(insert_query)
  end
end

