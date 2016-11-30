make: dojar

.PHONY: all
all: class jar javadoc

.PHONY: class
class:
	mkdir -p bin; javac src/*/*.java -d bin/;

.PHONY: javadoc
javadoc:
	javadoc -docencoding "utf-8" -version -private -d doc/ -windowtitle 研究管理支援システム src/*/*.java

.PHONY: do
do:
	mkdir -p bin; javac src/*/*.java -d bin/ && java -cp bin main.Main

.PHONY: jar
jar:
	mkdir -p bin; javac src/*/*.java -d bin/;\
	cp Main.mf bin/Main.mf; cd bin/;\
	jar cfm ../AMS.jar Main.mf * ../src/icon/* ../src/properties/*;\
	cd ../; chmod +x AMS.jar;\

.PHONY: dojar
dojar: jar
	java -jar AMS.jar

.PHONY: clean-file
clean-file:
	rm -rf file
