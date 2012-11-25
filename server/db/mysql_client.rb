require 'mysql2'

module MysqlClient
  def get_client
    return Mysql2::Client.new(
      :host => "localhost",
      :username => "betterflow",
      :password => "hogehoge",
      :database => "dailymotion")
  end

  module_function :get_client
end
