require 'java'

java_import 'jpfds.Seqs'

describe 'a SeqBuilder' do

  before :each do
    @bld = Seqs.builder
    @elems = ["foo", "bar", "baz", "a", "b", "c"]
  end

  def randData
    data = @elems.shuffle[0..rand(@elems.length)]
  end

  def toList array
    array.inject(Seqs.nil) { |l,x| l.cons x }
  end

  def toBuilder array
    array.inject(Seqs.builder) { |b,x| b.addThen x }
  end

  it "should create an empty Seq" do
    @bld.make.isEmpty.should == true
  end

  it "should accept input elements to make non-emtpy Seq" do
    @bld.addThen("foo").make.eq(Seqs.nil.cons("foo")).should == true
  end

  it "should not allow calling make() againt" do
    @bld.addThen("foo").make.eq(Seqs.nil.cons("foo")).should == true
    expect { @bld.make }.to raise_error
  end

  it "should makes Seq with known size matching the number of added elements" do
    10.times {
      data = randData
      toBuilder(data).make.sizeInfo.size.should == data.length
    }
  end

  it "should create list in left to right order with add()" do
    10.times {
      data = randData
      toBuilder(data).make.eq(toList(data).reverse).should == true
    }
  end

  it "should create list in right to left order with cons()" do
    10.times {
      data = randData
      l = data.inject(Seqs.builder) { |b,x| b.consThen x }.make
      l.eq(toList(data)).should == true
    }
  end

end
