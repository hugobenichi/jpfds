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

describe 'Reducible#into()' do

  it "should push elements into a Seq in reverse order" do
    10.times do
      l = fromArray randData
      l.into(Seqs.nil).eq(l.reverse).should == true
    end
  end

  it "should push nothing into a Col when the source is empty" do
    10.times do
      l = fromArray randData
      Seqs.nil.into(l).eq(l).should == true
    end
  end

  it "should push elements into a Builder and return a Col" do
    10.times do
      l = fromArray randData
      l.into(Seqs.builder()).eq(l).should == true
    end
  end

end

describe 'Reducible#filter() with Seq' do

  it "should return the Empty Seq when filtering the empty Seq" do
    Seqs.nil.filter(all).into(Seqs.nil).isEmpty.should == true
  end

  it "should return all elements when filtering nothing" do
    10.times do
      l = fromArray randData
      l.filter(all).into(Seqs.nil).eq(l.reverse).should == true
    end
  end

  it "should return no elements when filtering everything" do
    10.times do
      l = fromArray randData
      l.filter(none).into(Seqs.nil).isEmpty.should == true
    end
  end

  it "should filter elements in the sequence while preserving order" do
    10.times do |i|
      filt = lenLessThan 3
      data = randData
      expected = fromArray data.select{ |x| filt.test x }
      source = fromArray(data).filter(filt)
      source.into(Seqs.nil).eq(expected.reverse).should == true
    end
  end

end

describe 'Reducer#map() on Seq' do

  def id
    Class.new { def apply x; x end }.new
  end

  def rev
    Class.new { def apply x; x.reverse end }.new
  end

  it "should map the empty Seq to the empty Seq for any transformation" do
    Seqs.nil.map(id).into(Seqs.builder()).isEmpty.should == true
    Seqs.nil.map(rev).into(Seqs.builder()).isEmpty.should == true
  end

  it "should map any Seq to itself when the transformation is the identity" do
    10.times do
      l = fromArray randData
      l.map(id).into(Seqs.builder()).eq(l).should == true
    end
  end

  it "should map any String Seq to itself when applying twice String#reverse" do
    10.times do
      l = fromArray randData
      l.map(rev).map(rev).into(Seqs.builder).eq(l).should == true
    end
  end

end

describe 'Reducible#flatMap() on Seq' do

  def toNil
    Class.new { def apply x; Seqs.nil; end }.new
  end

  def toBox
    Class.new { def apply x; Seqs.nil.cons(x) end }.new
  end

  it "should map the empty Seq to the empty Seq for any op" do
    Seqs.nil.flatMap(toNil).into(Seqs.nil).isEmpty.should == true
    Seqs.nil.flatMap(toBox).into(Seqs.nil).isEmpty.should == true
  end

  it "should map any Seq to itself when flatMapping to a singleton Seq" do
    10.times do
      l = fromArray randData
      l.flatMap(toBox).into(Seqs.builder).eq(l).should == true
    end
  end

  it "should map any Seq to the empty Seq when flatMapping to the empty Seq" do
    10.times do
      l = fromArray randData
      l.flatMap(toNil).into(Seqs.nil).isEmpty.should == true
    end
  end

end
