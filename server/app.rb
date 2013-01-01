require 'rubygems'
require 'sinatra'
require 'json'
require 'zipruby'
require File.dirname(__FILE__) + '/db/image_store'

set :port, 3000
set :public_folder, File.dirname(__FILE__) + '/public'


get '/' do
  erb :index
end

get '/detail/:id' do
  image_store = ImageStore.new
  @image_hash = image_store.get_image_hash(params[:id])
  erb :detail
end

post '/api/like' do
  image_store = ImageStore.new
  image_store.like(params[:id])
end

get '/api/get/image' do
  image_store = ImageStore.new
  image_store.get_image_json(params[:id])
end

get '/api/get/images' do
  image_store = ImageStore.new
  image_store.get_all_image_json()
end

post '/api/convert' do
  output_path = 'images/gifs/1.gif'

  image_store = ImageStore.new
  last_id = image_store.get_last_id()
  output_path = 'images/gifs/' + last_id.to_s + '.gif'

  image_title = params['image_title']
  zipfile = params['contents']
  zipfile_path = "tmp/" + zipfile[:filename]
  File.binwrite(zipfile_path, zipfile[:tempfile].read)

  image_store.save_image(image_title, output_path)

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
  p command
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

