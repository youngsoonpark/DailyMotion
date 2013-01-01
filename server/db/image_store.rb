$LOAD_PATH.push('./db/')
require 'mysql2'
require 'mysql_client'


class ImageStore

  def initialize
    @client = MysqlClient.get_client()
  end

  def get_image_hash(id)
    image_hash = Hash.new

    select_query = <<-SQL
      SELECT * FROM gif_images WHERE id=#{id}
    SQL
    p select_query

    @client.query(select_query).each do |row|
      image_hash = row
    end

    return image_hash
  end

  def get_all_image_hash
    image_hash_array = Array.new

    select_query = <<-SQL
      SELECT * FROM gif_images ORDER BY id DESC
    SQL
    p select_query

    @client.query(select_query).each do |row|
      image_hash_array.push(row)
    end

    return image_hash_array
  end

  def get_image_json(id)
    return get_image_hash(id).to_json()
  end

  def get_all_image_json
    return get_all_image_hash().to_json()
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

  def like(id)
    update_query = <<-SQL
      UPDATE gif_images SET like_count=like_count+1 WHERE id=#{id}
    SQL
    p update_query

    @client.query(update_query)
  end
end

