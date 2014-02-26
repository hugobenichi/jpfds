require 'java'

java_import 'jpfds.Seqs'

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

def randData
  ["foo", "bar", "baz", "a", "b", "c", "oo", "fp"][0..rand(7)].shuffle!
end

def fromArray array
  array.inject(Seqs.nil) { |l,x| l.cons x }
end

describe 'Seq#lcat()' do

  it "should return the Empty Seq when concatenating the Empty Seq to itself" do
    Seqs.nil.lcat(Seqs.nil).eq(Seqs.nil).should == true
  end

  it "should be a noop when concatenating the empty Seq to any Seq" do
    10.times do
      l = fromArray randData
      l.lcat(Seqs.nil).eq(l).should == true
    end
  end

  it "should the list concatenated to the empty Seq for any list" do
    10.times do
      l = fromArray randData
      Seqs.nil.lcat(l).eq(l).should == true
    end
  end

  it "should return the same result as Seq#union, not considering lazyness" do
    10.times do
      l = fromArray randData
      r = fromArray randData
      l.lcat(r).eq(l.union r).should == true
    end
  end

end

describe 'Seq#lmap()' do

  def id
    Class.new { def apply x; x end }.new
  end

  def rev
    Class.new { def apply x; x.reverse end }.new
  end

  it "should map the empty Seq to the empty Seq for any transformation" do
    Seqs.nil.lmap(id).isEmpty.should == true
    Seqs.nil.lmap(rev).isEmpty.should == true
  end

  it "should map any Seq to itself when the transformation is the identity" do
    10.times do
      l = fromArray randData
      l.lmap(id).eq(l).should == true
    end
  end

  it "should map any String Seq to itself when applying twice String#reverse" do
    10.times do
      l = fromArray randData
      l.lmap(rev).lmap(rev).eq(l).should == true
    end
  end

  it "should not consume items from a Lazy Seq unless needed" do
    source = Source.new "foo"
    ls = Seqs.lazy source
    lrev = ls.lmap(rev)
    10.times do |i|
      source.calls.should == i
      lrev.head()
      lrev = lrev.tail()
    end
  end

end

describe 'Seq#lfilter()' do

  def all
    Class.new { def test x; true end }.new
  end

  def none
    Class.new { def test x; false end }.new
  end

  def lenLessThan len
    Class.new {
      def initialize len; @len = len end
      def test x; x.length < @len end
    }.new len
  end

  it "should map the empty Seq to the empty Seq for any filter" do
    Seqs.nil.lfilter(all).isEmpty.should == true
    Seqs.nil.lfilter(none).isEmpty.should == true
  end

  it "should map any Seq to itself when the filter returns true" do
    10.times do
      l = fromArray randData
      l.lfilter(all).eq(l).should == true
    end
  end

  it "should map any Seq to the empty Seq when the filter returns false" do
    10.times do
      l = fromArray randData
      l.lfilter(none).eq(Seqs.nil).should == true
    end
  end

  it "should not consume items from a Lazy Seq unless needed" do
    source = Source.new "foo"
    ls = Seqs.lazy source
    lfilt = ls.lfilter(all)
    10.times do |i|
      source.calls.should == i
      lfilt.head()
      lfilt = lfilt.tail()
    end
  end

  it "should filter elements in the sequence while preserving order" do
    10.times do |i|
      filt = lenLessThan 3
      data = randData
      dataf = data.select{ |x| filt.test x }
      fromArray(data).lfilter(filt).eq(fromArray(dataf))
    end
  end

end

describe 'Seq#lflatMap()' do

  def toNil
    Class.new { def apply x; Seqs.nil; end }.new
  end

  def toBox
    Class.new { def apply x; Seqs.nil.cons(x) end }.new
  end

  def toConst y
    Class.new { def apply x; Seqs.nil.cons(y) end }.new
  end

  it "should map the empty Seq to the empty Seq for any op" do
    Seqs.nil.lflatMap(toNil).isEmpty.should == true
    Seqs.nil.lflatMap(toBox).isEmpty.should == true
  end

  it "should map any Seq to itself when flatMapping to a singleton Seq" do
    10.times do
      l = fromArray randData
      l.lflatMap(toBox).eq(l).should == true
    end
  end

  it "should map any Seq to the empty Seq when flatMapping to the empty Seq" do
    10.times do
      l = fromArray randData
      l.lflatMap(toNil).isEmpty.should == true
    end
  end

  it "should not consume items from a Lazy Seq unless needed" do
    source = Source.new "foo"
    ls = Seqs.lazy(source).lflatMap(toBox)
    10.times do |i|
      source.calls.should == i
      ls.head()
      ls = ls.tail()
    end
  end

end
