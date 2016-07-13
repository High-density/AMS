.PHONY: all
all: class javadoc

.PHONY: class
class:
	javac src/*/*.java

.PHONY: javadoc
javadoc:
	javadoc -private -d src/doc/ -windowtitle 研究管理支援システム src/*/*.java
