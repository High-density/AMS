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
	mkdir bin;javac src/*/*.java -d bin/;cp Main.mf bin/Main.mf;cp -r src/icon/ bin/icon;cd bin/;jar cfm ../AMS.jar Main.mf *;cd ../;chmod +x AMS.jar

.PHONY: dojar
dojar:
	make jar;java -jar AMS.jar

