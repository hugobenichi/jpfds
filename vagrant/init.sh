#! /bin/bash

set -x

# config variables
java8_dl="http://download.java.net/jdk8/archive/b120/binaries/"\
"jdk-8-ea-bin-b120-linux-x64-12_dec_2013.tar.gz?q=download"
ant_dl="http://ftp.yz.yamagata-u.ac.jp/pub/network/apache//ant/binaries/"\
"apache-ant-1.9.3-bin.tar.gz"
jruby_dl="http://jruby.org.s3.amazonaws.com/downloads/1.7.9/"\
"jruby-bin-1.7.9.tar.gz"

jdk8_path="/home/vagrant/jdk1.8.0/bin"
java_home="/home/vagrant/jdk1.8.0/"
ant_path="/home/vagrant/apache-ant-1.9.3/bin"
jruby_path="/home/vagrant/jruby-1.7.9/bin"

gems="rake rspec test-spec shoulda bundler jbundler eventmachine-tail"


# package installation
sudo apt-get update
sudo apt-get install -y curl make git vim tree screen tmux

$jdk8_path/java -version >/dev/null 2>&1 || {
  curl $java8_dl | tar -xz
  echo "export PATH=\$PATH:$jdk8_path" >> /home/vagrant/.bashrc
  echo "export JAVA_HOME=$java_home" >> /home/vagrant/.bashrc
  sudo $jdk8_path/java -Xshare:dump
}

$ant_path/ant -version >/dev/null 2>&1 || {
  curl $ant_dl | tar -xz
  export JAVA_HOME=$java_home
  echo "export PATH=\$PATH:$ant_path" >> /home/vagrant/.bashrc
}

$jruby_path/jruby -version >/dev/null 2>&1 || {
  curl $jruby_dl | tar -xz
  export PATH=$PATH:$jdk8_path:$jruby_path
  echo "export PATH=$PATH:$jruby_path" >> /home/vagrant/.bashrc
  cd /home/vagrant/jruby-1.7.9/tool/nailgun/ && ./configure && make
  jruby -S jgem install $gems
}

echo "export PATH=$PATH:/home/vagrant/jpfds" >> /home/vagrant/.bashrc
