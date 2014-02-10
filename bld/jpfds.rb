require 'rake'
require 'ant'
require 'pathname'

project = "Jpfds"
jarname = project + ".jar"

$CLASSPATH << Dir.pwd + '/out' # add compile classes to classpath

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

desc 'complete build task'
task :build => [:comp, :jar, :doc]

desc 'compile java sources'
task :comp do
  ant.mkdir :dir => dir[:classes]
  ant.javac(compile_options) { compilerarg :value => "-Xlint:all" }
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

def ant_do method, *args, &block
  begin
    ant.send method, *args, &block
  rescue
    raise "failed to invoke ant target :%s" % method
  end
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

Rake.application.options.trace = false

def format_task task
  pre = task.prerequisites
  task.name + (pre.empty? ? "" : " => %s" % pre.join(", "))
end

def main env
  puts "%s continuous build console" % env[:project]
  puts "list of targets:"
  Rake::Task.tasks.map{ |t| format_task t }.each{ |desc| puts "  %s" % desc}
end

main :project => project

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
        allow to reload the build script and gracefully reload tasks

    Bldit
      at startup: list number of tasks, top lvl task, project name, other info
      ant color scheme ?
eos
