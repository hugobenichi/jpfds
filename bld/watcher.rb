require 'eventmachine'
require 'eventmachine-tail'

class DirWatcher < EventMachine::FileGlobWatch
  ScanInterval = 0.5
  def initialize(path)
    super(path, ScanInterval)
  end
  def file_deleted(path)
    puts "deleted file %s" % path
  end
  def file_found(path)
    puts "found file %s" % path
    begin
      EventMachine.watch_file(path, SourceHandler)
    rescue Exception => ex
      puts "err %s" % ex
    end
  end
end

# this does not work because watch_filename is not supported in jruby version
# of eventmachine
# I would have to use the java 7 file watcher api
module SourceHandler
  def file_modified
    puts "modified"
  end
  def file_moved
    puts "moved"
  end
end

EventMachine.run { DirWatcher.new("src/**/*.java") }
