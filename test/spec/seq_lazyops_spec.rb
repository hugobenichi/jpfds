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

describe 'Seq#lmap()' do

  def id
    Class.new { def apply x; x end }.new
  end

  def rev
    Class.new { def apply x; x.reverse end }.new
  end

  def randData
    ["foo", "bar", "baz", "a", "b", "c", "oo", "fp"][0..rand(7)].shuffle!
  end

  def fromArray array
    array.inject(Seqs.nil) { |l,x| l.cons x }
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

  def randData
    ["foo", "bar", "baz", "a", "b", "c", "oo", "fp"][0..rand(7)].shuffle!
  end

  def fromArray array
    array.inject(Seqs.nil) { |l,x| l.cons x }
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
