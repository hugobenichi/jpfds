require 'java'

java_import 'jpfds.Seqs'
java_import 'jpfds.Size'

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

def lessThan x
  Class.new {
    def initialize x; @x = x end
    def test x; x < @x end
  }.new x
end

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

describe 'Seq#take()' do

  it "should return the empty Seq when taking 0" do
    10.times do
      fromArray(randData).take(0).isEmpty.should == true
    end
  end

  it "should takes the first items in a Seq" do
    l = Seqs.induce(0) { |x| x + 1 }
    Seqs.nil.cons(2).cons(1).cons(0).eq(l.take(3)).should == true
  end

  it "should return the same Seq when taking the number of items in a Seq" do
    10.times do
      l = fromArray(randData)
      l.take(l.sizeInfo.size).eq(l).should == true
    end
  end

  it "should return the same Seq when taking more items there is in a Seq" do
    10.times do
      l = fromArray(randData)
      l.take(2 * l.sizeInfo.size).eq(l).should == true
    end
  end

  it "should return a list with no more items than n for longer than n Seqs" do
    10.times do
      l = fromArray(randData)
      s = l.size / 2
      l.take(s).size.should == s
    end
  end

  it "should return a bounded Seq from an infinite Seq" do
    l = Seqs.constant "foo"
    #l.take(10).sizeInfo.should != Size.infinite # to implement
  end

end

describe 'Seq#skip()' do

  it "should return the empty Seq from the empty Seq" do
    Seqs.nil.skip(10).isEmpty.should == true
  end

  it "should skip the first items in a Seq" do
    l = Seqs.induce(0) { |x| x + 1 }
    10.times { |i| l.skip(i).head.should == i }
  end

  it "should return the empty Seq when skipping the number of items in a Seq" do
    10.times do
      l = fromArray(randData)
      l.skip(l.sizeInfo.size).isEmpty.should == true
    end
  end

  it "should return the empty Seq when skipping more items there is in a Seq" do
    10.times do
      l = fromArray(randData)
      l.skip(2 * l.sizeInfo.size).isEmpty.should == true
    end
  end

end

describe 'Seq#until()' do

  it "should return the empty Seq from any Seq when the test returns false" do
    10.times do
      fromArray(randData).until(none).isEmpty.should == true
    end
  end

  it "should return the same Seq when the test returns true" do
    10.times do
      l = fromArray(randData)
      l.until(all).eq(l).should == true
    end
  end

  it "should stop the Seq when the test fails for the first time" do
    l = Seqs.induce(0) { |x| x + 1 }
    l.until(lessThan 3).eq(l.take(3)).should == true
  end

  it "should be idempotent" do
    l = Seqs.induce(0) { |x| x + 1 }
    10.times { |i|
      t = l.until(lessThan 3)
      t.until(lessThan 3).eq(t).should == true
    }
  end

end
