.PHONY: all
all: class javadoc

.PHONY: class
class:
	javac src/*/*.java

.PHONY: javadoc
javadoc:
	javadoc -docencoding "utf-8" -version -private -d doc/ -windowtitle 研究管理支援システム src/*/*.java

.PHONY: do
do:
	javac src/*/*.java && java -cp src main.Main

.PHONY: jar
jar:
	mkdir bin;\
	javac src/*/*.java -d bin/;\
	cp Main.mf bin/Main.mf;\
	cd bin/;\
	jar cfm ../AMS.jar Main.mf * ../src/icon/* ../src/properties/*;\
	cd ../;\
	chmod +x AMS.jar;\
	# finish making AMS.jar

.PHONY: dojar
dojar: jar
	java -jar AMS.jar
