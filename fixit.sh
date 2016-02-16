diffStr="nonEmpty!";
./clean.sh
. fixit.cfg
rm -rf diffs
mkdir diffs
i=0
rm -rf oldIter
mkdir oldIter
cp -r datasets/$project oldIter
while [[ ($diffStr != "") && ($i -lt $maxIterCount) ]];
do
i=$((i+1))
#compile
echo "compiling for iteration $i!"
rm -f compiledProject.jar 
ant -f compilers/build.xml >warnings 
rm -f warnings
echo "compiling finished!"

#decompile
echo "decompiling for iteration $i!"
rm -rf newIter 
#java -jar cfr.jar swt.jar --outputdir swt_output >info
#java -jar decompilers/fernflower.jar compiledProject.jar newIter >info
#rm -f info
#cd newIter 
#jar xvf compiledProject.jar
#rm -f compiledProject.jar
../decompilers/$decompiler.sh
cd ..
mv newIter/org/eclipse/swt/* newIter 
rm -rf newIter/org
echo "decompiling finished!"

#compare
#for filename in $(ls -R /mct_txl_nicad/swt);
#   do if [[ ${filename}==*.java* ]]; 
#   then 
#	fileIn=/mct_txl_nicad/swt/$filename;
#	fileOut=/mct_txl_nicad/swt_output/$filename; 
#	diffStr=[diff $fileIn $fileOut];
#	nodiff = $diff$diffStr;
#   fi; 
echo "folder comparing for iteration $i!" 
#diffStr=$(diff -r oldIter newIter);
diff -r oldIter newIter > diffs/diff_$i
[ -s diffs/diff_$i ]
var=$?
if [ $var -eq 0 ]
then
diffStr="nonEmpty"
else
diffStr=""
fi
rm -rf oldIter 
mkdir oldIter 
cp -r newIter/* oldIter 
patch oldIter/widgets/Widget.java -R widget_patch
echo "folder comparing finished!"
done
