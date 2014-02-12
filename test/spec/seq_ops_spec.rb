require 'java'

java_import 'jpfds.Seqs'

describe 'Seq reverse' do

  before :each do
    @nil = Seqs.nil
    @x = @nil.cons("x").cons("y").cons("z")
    @y = @nil.cons("z").cons("y").cons("x")
  end

  it "should transform Seq.nil into Seq.nil" do
    @nil.reverse.eq(Seqs.nil).should == true
  end

  it "should transform singleton Seqs into themselves" do
    a = @nil.cons("a")
    a.reverse.eq(a).should == true
  end

  it "should more generally accept any palindrome seq as an eigenvector" do
    a = @nil.cons("foo").cons("foo")
    a.reverse.eq(a).should == true
    b = @nil.cons("foo").cons("bar").cons("foo")
    b.reverse.eq(b).should == true
  end

  it "should reverse the order of elements in a Seq" do
    @x.reverse.eq(@y).should == true
    @y.reverse.eq(@x).should == true
  end

  it "should be its own inverse" do
    a = @nil.cons("foo").cons("bar")
    a.reverse.reverse.eq(a).should == true
    @x.reverse.reverse.eq(@x).should == true
  end

end
