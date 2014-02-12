require 'java'

java_import 'jpfds.Seqs'

describe 'a SeqBuilder' do

  before :each do
    @bld = Seqs.builder
  end

  it "should create an empty Seq" do
    @bld.make.isEmpty.should == true
  end

  it "should accept input elements to make non-emtpy Seq" do
    seq = @bld.addThen('foo').make
    seq.isEmpty.should == false
    seq.head.should == 'foo'
    seq.tail.isEmpty.should == true
  end

end
