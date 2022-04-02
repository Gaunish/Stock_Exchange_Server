JFLAGS = -g
JC = javac -sourcepath ./ -cp combined.jar

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  ./database/*.java \
		  ./account/*.java \
		  ./connection/*.java \
		  ./create/*.java \
		  ./symbol/*.java \
		  ./testing/*.java \
		  ./xml/*.java \
		  ./testing/*.java \
		  ./transaction/*.java \
		  Server.java \


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) ./database/*.class
	$(RM) ./account/*.class
	$(RM) ./connection/*.class
	$(RM) ./create/*.class
	$(RM) ./symbol/*.class
	$(RM) ./xml/*.class
	$(RM) ./testing/*.class
	$(RM) ./transaction/*.class

run:
	java -cp .:./combined.jar Server
