require 'java'

java_import 'jpfds.Seqs'
java_import 'jpfds.Size'

class ConstantSource
  attr_reader :count
  def initialize value, &block
    @count = 0
    @value = value
    @next = block
  end
  def get
    @count += 1
    ret = @value
    @value = @next.call @value if @next
    ret
  end
end

describe 'a lazy Seq' do

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
    source = ConstantSource.new "foo"
    ls = Seqs.infinite source
    1000.times do |i|
      ls = ls.tail
      ls.isEmpty.should == false
    end
  end

  it "should consume items from the iterator only as needed" do
    source = ConstantSource.new "foo"
    ls = Seqs.infinite source
    10.times do |i|
      source.count.should == i
      ls = ls.tail
    end
  end

  it "should consume items from the iterator at most once" do
    source = ConstantSource.new "foo"
    ls = Seqs.infinite source
    10.times do |i|
      source.count.should == i
      ls.head
      ls.isEmpty
      ls = ls.tail
      source.count.should == i + 1
    end
  end

  it "should be thread-safe" do
    source = ConstantSource.new(0) { |x| x + 1 }
    shared = Seqs.infinite source
    threads = Array.new(4) do
      Thread.new do
        ls = shared
        1000.times do |i|
          ls.head.should == i
          ls = ls.tail
        end
      end
    end
    threads.each { |t| t.join }
  end

end
