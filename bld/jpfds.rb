require 'rake'
require 'ant'
require 'pathname'

Rake.application.options.trace = false

$CLASSPATH << Dir.pwd + '/out' # add compile classes to classpath

def ant_do method, *args
  begin
    ant.send method, *args
  rescue
    raise "failed to invoke ant target :%s" % method
  end
end

# TODO: pass this as environment
dir = {
  :src        => 'src',
  :srcfiles   => FileList['src/**/*.java'],
  :build      => 'out',
  :classes    => 'out/classes',
  :doc        => 'out/doc'
}

# TODO: pass this as argument to build task
compile_options = {
  :srcdir   => dir[:src],
  :destdir  => dir[:classes],
  :source   => '1.8',
  :target   => '1.8'
}

# TODO: pass this as environment conf
jarname = 'Jpfds.jar'

desc 'complete build task'
task :build => [:comp, :jar, :doc]

desc 'compile java sources'
task :comp do
  ant.mkdir :dir => dir[:classes]
  ant_do :javac, compile_options
end

desc 'run all tests'
task :test => :build do
  # TODO: find a better way to run and rerun test instead of requiring
#  require 'test/foo_test-unit'
#  require 'test/foo_test-spec'
#  require 'test/foo_test-shoulda'
end

desc 'prepare a jar file for the project'
task :jar => :comp do
  ant.jar :destfile => dir[:build] + '/' + jarname, :basedir => dir[:classes]
end

desc 'generate javadoc'
task :doc do
  ant.javadoc :sourcefiles => dir[:srcfiles], :destdir => dir[:doc]
end

desc 'clean built classes files and jar artifacts'
task :clean do
  ant.delete :dir => dir[:build]
end

module RakeForwarder
  {:run => :invoke, :exec => :execute}.each do |name, action|
    define_method name do |target|
      begin
        Rake::Task[target].send action
      rescue Exception => ex
        puts "err: %s" % ex
      end
    end
  end
end

include RakeForwarder

<<eos
  notes:

    too hard to find where jruby spawn subsystem. it will not scale well to try
    too keep startup time low with nailgun.

    alternative approach:
      keep the build tool active as some server.
      use irb as the console !
        pute a wrapper to swallow task exceptions
        keep history of command
        have a watcher mode for recompile and run test
          (associate src to test for smart rerunning test)

    Bldit
      at startup: list number of tasks, top lvl task, project name, other info
      ant color scheme ?
eos
