require 'mysql2'

class ImageStore

  def initialize(
    host = "localhost",
    username = "betterflow",
    password = "hogehoge",
    database = "dailymotion")
    
    @client = Mysql2::Client.new(
      :host => host,
      :username => username,
      :password => password,
      :database => database)
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

