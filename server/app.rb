require 'rubygems'
require 'sinatra'
require 'zipruby'
require 'find'
require 'mysql2'

set :port, 3000
set :public, File.dirname(__FILE__) + '/public'

client = Mysql2::Client.new(:host => "localhost", :username => "betterflow", :password => "hogehoge", :database => "dailymotion")

get '/' do
  @gif_image_hash = Hash.new
  client.query("SELECT title, image_url FROM gif_images ORDER BY id DESC").each do |row|
    @gif_image_hash[row["title"]] = row["image_url"]
  end

  erb :index
end

post '/api/convert' do
  output_path = 'images/gifs/1.gif'
  client.query("SELECT id FROM gif_images ORDER BY id DESC LIMIT 1").each do |row|
    index = row["id"].to_i + 1
    output_path = 'images/gifs/' + index.to_s + '.gif'
  end

  image_title = params['image_title']
  zipfile = params['contents']
  zipfile_path = "tmp/" + zipfile[:filename]
  File.binwrite(zipfile_path, zipfile[:tempfile].read)
  client.query("INSERT INTO gif_images (title, image_url) VALUES ('" +
               image_title + "', '" + output_path + "');")

  result = zip_to_gif(zipfile_path, "public/" + output_path, params['delay'])
end

def zip_to_gif(src_path, output_path, delay)
  image_path_array = Array.new

  tmpout_path = ("tmp/out" + "/").sub("//", "/")
  Zip::Archive.open(src_path) do |archives|
    archives.each do |archive|
      FileUtils.makedirs(tmpout_path)
      unless archive.directory?
        file_path = tmpout_path + archive.name
        File.open(file_path, "w+b") do |file|
          file.print(archive.read)
        end
        image_path_array.push file_path
      end
    end
  end
  
  command = convert_command_builder(image_path_array, delay, 360, output_path)
  system(command)
  system('rm -rf tmp/*')

  return "OK"
end

def convert_command_builder(image_path_array, delay, max_size, output_path)
  resize_option = '-resize ' + max_size.to_s + 'x' + max_size.to_s
  delay_option = '-delay ' + delay.to_s

  body = '';
  image_path_array.each do |image|
    body << image << ' '
  end

  return 'convert ' + resize_option + ' ' + delay_option + ' ' + body + output_path
end

