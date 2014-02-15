require 'rake'
require 'ant'
require 'pathname'
require 'rspec'
require 'java'

def help_launcher
puts """
You can run tasks with:

> run :task_name_as_a_symbol    # run once and run needed dependencies

If you need to force running a task, use instead:

> exec :task_symbol     # does not run dependencies

You can reload the task file with

> reload :project_name_as_a_symbol

or reload the launcher with

> reload :launcher

Todo:
  find out how to do correct file dependencies for :comp and :test
  keep history of commands between runs
  have a watcher
    for reenabling tasks when a dependency change
    for recompile and run test
    (associate src to test for smart rerunning test)
  color scheme for ant and javac ?
"""
end

def reload project
  if project == :launcher
    load __FILE__
  else
    Rake::Task.clear
    load File.dirname(__FILE__) + "/#{project.to_s}.rb"
  end
end

def timing &block
  start = Time.now
  block.call
  "#{Time.now - start} sec"
end

def safe &block
  begin
    block.call
  rescue Exception => ex
    puts "err: %s" % ex
  end
end

def rake_do &block
  timing { safe &block }
end

def run target
  rake_do { Rake::Task[target].invoke }
end

def exec target
  rake_do { Rake::Task[target].execute }
end

def format_tasks
  tasks = Rake::Task.tasks.reject{ |t| t.comment.nil? }
  names = tasks.map { |t| t.name }
  comments = tasks.map { |t| t.comment }
  preq = tasks.map { |t| t.prerequisites.join ", " }

  max = names.map {|c| c.length }.max
  names = names.map { |c| c + " " * (max - c.length) }

  [max, [names, comments, preq].transpose]
end

def print_tasks offset, task_info
  #TODO: add colors
  task_info.each { |name, comment, preq|
    puts "  :%s  %s" % [name, comment]
    puts " " * offset + "       =>  " + preq unless preq.empty?
  }
end

def main env
  reload env[:project]
  puts "%s continuous build console" % env[:project]
  puts "#{Rake::Task.tasks.length} existing tasks:"
  print_tasks *format_tasks
end

Rake::TaskManager.record_task_metadata = true
Rake.application.options.trace = false

main :project => "Jpfds"

class ColoredStream < IO
  def initialize stream, code
    @stream, @code = stream, code
  end
  def write string
    @stream.write "\e[#{@code}m#{string}\e[0m"
  end
end
