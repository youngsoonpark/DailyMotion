require 'rubygems'
require 'sinatra'
require 'zipruby'

set :port, 3000
set :public, File.dirname(__FILE__) + '/public'

get '/' do
  erb :index
end

post '/' do
  zipfile = params['upload_file_contents']
  File.binwrite("public/" + zipfile[:filename], zipfile[:tempfile].read)

  "Hello World!"
end
