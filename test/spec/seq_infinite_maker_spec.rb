require 'java'

java_import 'jpfds.Seqs'
java_import 'jpfds.Size'

describe 'Seqs#constant()' do

  it "should return a non-empty infinite Seq" do
    l = Seqs.constant "foo"
    l.isEmpty.should == false
    l.sizeInfo.should == Size.infinite
  end

  it "should return an infinite Seq which repeats the same value" do
    l = Seqs.constant "foo"
    100.times do
      l.head.should == "foo"
      l = l.tail
    end
  end

end

describe 'Seqs#induce()' do

  it "should create an non-empty infinite Seq" do
    l = Seqs.induce("foo") { |x| x }
    l.isEmpty.should == false
    l.sizeInfo.should == Size.infinite
  end

  it "should create an non-empty infinite Seq" do
    c = 0
    l = Seqs.induce("foo") { |x| c += 1; x }
    100.times do |i|
      c.should == i
      l.head.should == "foo"
      l = l.tail
      c.should == i + 1
    end
  end

  it "should create an infinite Seq of values made from the induction rule" do
    l = Seqs.induce(0) { |x| x += 1 }
    100.times do |i|
      l.head.should == i
      l = l.tail
    end
  end

end

describe 'Seqs#cycle()' do

  before do
    @x = Seqs.nil.cons("foo")
    @y = @x.cons("bar")
    @z = @y.cons("baz")
  end

  it "should create an non-empty infinite Seq" do
    l = Seqs.cycle @z
    l.isEmpty.should == false
    #l.sizeInfo.should == Size.infinite # currently only unknown
  end

  it "should create a constant Seq out of a singleton Seq" do
    l = Seqs.cycle @x
    100.times do
      l.head.should == "foo"
      l = l.tail
    end
  end

  it "should create a 2-cycle Seq out of a two elements Seq" do
    l = Seqs.cycle @y
    100.times do
      l.head.should == "bar"
      l = l.tail
      l.head.should == "foo"
      l = l.tail
    end
  end

  it "should create a 3-cycle Seq out of a two elements Seq" do
    l = Seqs.cycle @z
    100.times do
      l.head.should == "baz"
      l = l.tail
      l.head.should == "bar"
      l = l.tail
      l.head.should == "foo"
      l = l.tail
    end
  end

end
