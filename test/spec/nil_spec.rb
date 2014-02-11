require 'java'

java_import 'jpfds.Seqs'

describe 'an Empty Seq' do

  before do
    @nil = Seqs.nil
  end

  it "should be empty" do
    @nil.isEmpty.should == true
  end

  it "should be cons-able to form a singleton list" do
    l = @nil.cons("foo")
    l.isEmpty.should == false
    l.head.should == "foo"
    l.tail.isEmpty.should == true
  end

end
