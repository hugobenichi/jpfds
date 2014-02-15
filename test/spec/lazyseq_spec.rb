require 'java'

java_import 'jpfds.Seqs'
java_import 'jpfds.Size'

class Source
  attr_reader :calls
  def initialize value, &block
    @calls = 0
    @value = value
    @iterate = block
  end
  def get
    @calls += 1
    ret = @value
    @value = @iterate.call @value if @iterate
    ret
  end
  def iterator
    SourceIterator.new self
  end
end

class SourceIterator
  def initialize origin
    @origin = origin
  end
  def hasNext
    true
  end
  def next
    @origin.get
  end
  def remove
  end
end

describe 'a LazySeq' do

  def randData
    ["foo", "bar", "baz", "a", "b", "c", "oo", "fp"][0..rand(7)].shuffle!
  end

  def fromArray array
    [Seqs.lazy(array), array.inject(Seqs.nil) { |l,x| l.cons x }]
  end

  it "should have unknown size" do
    ls, l = fromArray randData
    ls.sizeInfo.should == Size.unknown
  end

  it "should consumes all elements of an iterator in order" do
    10.times do
      ls, l = fromArray randData
      Seqs.nil.addAll(ls).eq(l).should == true
    end
  end

  it "should allow to wrap infinite iterators" do
    source = Source.new "foo"
    ls = Seqs.lazy source
    1000.times do |i|
      ls = ls.tail
      ls.isEmpty.should == false
    end
  end

  it "should consume items from the iterator only as needed" do
    source = Source.new "foo"
    ls = Seqs.lazy source
    10.times do |i|
      source.calls.should == i
      ls = ls.tail
    end
  end

  it "should consume items from the iterator at most once" do
    source = Source.new "foo"
    ls = Seqs.lazy source
    10.times do |i|
      source.calls.should == i
      ls.head
      ls.isEmpty
      ls = ls.tail
      source.calls.should == i + 1
    end
  end

  it "should be thread-safe" do
    source = Source.new(0) { |x| x + 1 }
    shared = Seqs.lazy source
    threads = Array.new(4) do
      Thread.new do
        ls = shared
        100.times do |i|
          ls.head.should == i
          ls = ls.tail
        end
      end
    end
    threads.each { |t| t.join }
  end

end

describe "an InfiniteSeq" do

  it "should allow to wrap supplier functions and have infinite size" do
    source = Source.new "foo"
    ls = Seqs.infinite source
    ls.sizeInfo.should == Size.infinite
    1000.times do |i|
      ls = ls.tail
      ls.isEmpty.should == false
    end
  end

  it "should consume items from the source only as needed" do
    source = Source.new "foo"
    ls = Seqs.infinite source
    10.times do |i|
      source.calls.should == i
      ls = ls.tail
    end
  end

  it "should consume items from the source at most once" do
    source = Source.new "foo"
    ls = Seqs.infinite source
    10.times do |i|
      source.calls.should == i
      ls.head
      ls.isEmpty
      ls = ls.tail
      source.calls.should == i + 1
    end
  end

  it "should be thread-safe" do
    source = Source.new(0) { |x| x + 1 }
    shared = Seqs.infinite source
    threads = Array.new(4) do
      Thread.new do
        ls = shared
        100.times do |i|
          ls.head.should == i
          ls = ls.tail
        end
      end
    end
    threads.each { |t| t.join }
  end

end
