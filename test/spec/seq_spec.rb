require 'java'

java_import 'jpfds.Seqs'

describe 'a singleton Seq' do

  before do
    @elem = 'foo'
    @seq = Seqs.nil.cons @elem
  end

  it "should not be empty" do
    @seq.isEmpty.should == false
    @seq.nonEmpty.should == true
  end

  it "should have size 1" do
    @seq.sizeInfo.size.should == 1
  end

  it "should have a head" do
    @seq.head.should == 'foo'
  end

  it "should have an empty tail" do
    @seq.tail.isEmpty.should == true
  end

end

describe 'a 3 elements Seq' do

  before do
    @elems = ['foo', 'bar', 'baz']
    @seq = Seqs.nil
    @elems.reverse.each{ |x| @seq = @seq.cons x}
  end

  it "should not be empty" do
    @seq.isEmpty.should == false
    @seq.nonEmpty.should == true
  end

  it "should have size 3" do
    @seq.sizeInfo.size.should == 3
  end

  it "should have a head" do
    @seq.head.should == @elems[0]
  end

  it "should have a non-empty tail which has size 2, a head and a tail" do
    @seq.tail.isEmpty.should == false
    @seq.tail.head.should == @elems[1]
    @seq.tail.tail
    @seq.tail.sizeInfo.size.should == 2
  end

end
