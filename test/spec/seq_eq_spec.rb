require 'java'

java_import 'jpfds.Seqs'

describe 'Seq equality' do

  before :each do
    @nil = Seqs.nil
    @x = @nil.cons("x")
    @y = @x.cons("y")
    @z = @y.cons("z")
  end

  it "should say that Seq.nil is equal to Seq.nil" do
    @nil.eq(Seqs.nil).should == true
  end

  it "should say Seq.nil is not equal to non-empty Seqs" do
    @nil.eq(@x).should == false
    @nil.eq(@y).should == false
    @nil.eq(@z).should == false
  end

  it "should say two Seqs with same elements in same order are equal" do
    @x.cons('y').eq(@y).should == true
    @x.cons('y').cons('z').eq(@z).should == true
    @x.cons('y').cons('t').eq(@z).should == false
    @y.cons('z').eq(@z).should == true
  end

  it "should say two Seqs with same elements in different order aren't equal" do
    @nil.cons('y').cons('x').eq(@y).should == false
  end

  it "should handle null values properly" do
    @nil.cons(nil).eq(@nil.cons(nil)).should == true
    @x.eq(@nil.cons(nil)).should == false
    @nil.cons(nil).eq(@x).should == false
  end

end
