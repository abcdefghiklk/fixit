java -jar fernflower.jar ../compiledProject.jar ../newIter >../decompiledLog
rm -f ../decompiledLog
cd ../newIter
jar xvf compiledProject.jar
rm -f compiledProject.jar
rm -rf META-INF 
