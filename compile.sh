find -name "*.java" > sources.txt

javac -Xlint:all -Xdiags:verbose --enable-preview --release $1 -d ./classes @sources.txt 