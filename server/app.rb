require 'rubygems'
require 'sinatra'
require 'zipruby'
require 'find'
require 'RMagick'

set :port, 3000
set :public, File.dirname(__FILE__) + '/public'

get '/' do
  erb :index
end

post '/' do
  zipfile = params['upload_file_contents']
  zipfile_path = 'tmp/' + zipfile[:filename]
  File.binwrite(zipfile_path, zipfile[:tempfile].read)

  output_path = 'tmp/out'
  result = zip_to_gif(zipfile_path, 'tmp/out')

  if result
    'OK'
  elsif
    'NG'
  end
end

def convert_command_builder(image_path_array, delay, max_size)
  resize_option = '-resize ' + max_size.to_s + 'x' + max_size.to_s
  delay_option = '-delay ' + delay.to_s

  body = '';
  image_path_array.each do |image|
    body << image << ' '
  end

  count = Find.find('public').count

  return 'convert ' + resize_option + ' ' + delay_option + ' ' + body + 'public/' + count.to_s + '.gif'
end

def zip_to_gif(src_path, output_path)
  image_path_array = Array.new

  output_path = (output_path + "/").sub("//", "/")
  Zip::Archive.open(src_path) do |archives|
    archives.each do |archive|
      FileUtils.makedirs(output_path)
      unless archive.directory?
        file_path = output_path + archive.name
        File.open(file_path, "w+b") do |file|
          file.print(archive.read)
        end
        image_path_array.push file_path
      end
    end
  end
  
  command = convert_command_builder(image_path_array, 20, 480)
  p command
  rerunt = system(command)
end
