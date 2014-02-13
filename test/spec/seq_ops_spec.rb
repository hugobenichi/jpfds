require 'java'

java_import 'jpfds.Seqs'

describe 'Seq reverse' do

  before :each do
    @nil = Seqs.nil
    @x = @nil.cons("x").cons("y").cons("z")
    @y = @nil.cons("z").cons("y").cons("x")
    @a = @nil.cons("a")
    @foo = @nil.cons("foo").cons("foo")
    @foobar = @nil.cons("foo").cons("bar").cons("foo")
  end

  it "should transform Seq.nil into Seq.nil" do
    @nil.reverse.eq(Seqs.nil).should == true
  end

  it "should transform singleton Seqs into themselves" do
    @a.reverse.eq(@a).should == true
  end

  it "should more generally accept any palindrome seq as an eigenvector" do
    @foo.reverse.eq(@foo).should == true
    @foobar.reverse.eq(@foobar).should == true
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

  it "should preserve size" do
    [@nil, @x, @y, @a, @foo, @foobar].each do |l|
      l.reverse.sizeInfo.size.should == l.sizeInfo.size
    end
  end

end
