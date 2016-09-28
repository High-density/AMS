.PHONY: all
all: class javadoc

.PHONY: class
class:
	javac src/*/*.java

.PHONY: javadoc
javadoc:
	javadoc -private -d doc/ -windowtitle 研究管理支援システム src/*/*.java

.PHONY: do
do:
	javac src/*/*.java && java -cp src main.Main

.PHONY: jar
jar:
	cp Main.mf bin/Main.mf;cd bin/; jar cfm ../AMS.jar Main.mf *;cd ../;chmod +x AMS.jar;

