help_tasks = """
  you can reload this lis tof tasks gracefully into Rake by invoking
  the reload method of the launcher.
"""

project = "Jpfds"
jarname = project + ".jar"

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

files = {
  :src    => dir[:srcfiles],
  :test   => FileList['test/spec/*_spec.rb']
}

desc 'complete build task'
task :build => [:comp, :jar, :doc]

directory dir[:classes]

desc 'compile java sources'
#task :comp => files[:src] do
task :comp => dir[:classes] do
  ant.javac(compile_options) { compilerarg :value => "-Xlint:all" }
end

desc 'run all tests'
task :test => :load_jar do
  config = RSpec.configuration
  config.color = true
  RSpec::Core::Runner.run files[:test]
end

desc 'prepare a jar file for the project'
task :jar => :comp do
  ant.jar :destfile => dir[:build] + '/' + jarname, :basedir => dir[:classes]
end

task :load_jar => :jar do
  #require Dir.pwd + '/out/' + jarname
  jruby_loader = JRuby.runtime.jruby_class_loader
  urls = [ java.io.File.new(Dir.pwd + '/out/' + jarname).to_url ]
  jar_loader = java.net.URLClassLoader.new(urls.to_java(Java::java.net.URL), jruby_loader)
  loader.add_url(java.io.File.new(Dir.pwd + '/out/' + jarname).to_url)
end

desc 'generate javadoc'
task :doc do
  ant.javadoc :sourcefiles => dir[:srcfiles], :destdir => dir[:doc]
end

desc 'clean built classes files and jar artifacts'
task :clean do
  ant.delete :dir => dir[:build]
end
