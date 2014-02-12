require 'java'

java_import 'jpfds.Seqs'

describe 'an Empty Seq' do

  before do
    @nil = Seqs.nil
  end

  it "should be empty" do
    @nil.isEmpty.should == true
    @nil.nonEmpty.should == false
  end

  it "should be cons-able to form a singleton list" do
    l = @nil.cons("foo")
    l.isEmpty.should == false
    l.head.should == "foo"
    l.tail.isEmpty.should == true
  end

  it "should throw an error when its head is accessed" do
    expect { @seq.head }.to raise_error
  end

  it "should throw an error when its tail is accessed" do
    expect { @seq.tail }.to raise_error
  end

end
