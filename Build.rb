require 'rake'
require 'ant'
require 'pathname'

Rake.application.options.trace = false

project_dir = Pathname.new(__FILE__).dirname
class_dir = project_dir.to_path + '/build'

# TODO: find a way to hook efficiently to nailgun jruby process
Dir.chdir project_dir     # unless ng server runs in same dir
$CLASSPATH << class_dir   # add compile classes directory to java classpath

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
  :build      => 'build',
  :classes    => 'build/classes',
  :doc        => 'build/doc'
}

# TODO: pass this as argument to build task
compile_options = {
  :srcdir   => dir[:src],
  :destdir  => dir[:classes],
  :source   => '1.8',
  :target   => '1.8'
}

# TODO: pass this as environment conf
jarname = 'FooBarBaz.jar'

desc 'compile java sources'
task :build do
  ant.mkdir :dir => dir[:classes]
  ant_do :javac, compile_options
end

desc 'run all tests'
task :test => :build do
  # TODO: find a better way to run and rerun test instead of requiring
  require 'test/foo_test-unit'
#  require 'test/foo_test-spec'
#  require 'test/foo_test-shoulda'
end

# TODO: add jar versioning
desc 'prepare a jar file for the project'
task :jar => :build do
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

def bld name
  begin
    Rake::Task[name].execute if Rake::Task.task_defined? name
  rescue Exception => ex
    puts "err: %s" % ex
  end
end

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
