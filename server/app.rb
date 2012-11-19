require 'rubygems'
require 'sinatra'

set :port, 3000
set :public, File.dirname(__FILE__) + '/public'

get '/' do
  erb :index
end

post '/' do
  p 'receive'
end
