run: 
	java -jar executable.jar
	jar -cMf food.zip .


nosrc: application/Main.java
	javac -d . application/*.java
	jar -cfm executable.jar manifest.txt application


srcbin: application/Main.java
	mkdir -p 	bin
	javac -d bin application/*.java
	jar -cfm executable.jar manifest_srcbin.txt -C bin application

clean:
	\rm *.class	


cleanbin:
	\rm bin/application/*.class

