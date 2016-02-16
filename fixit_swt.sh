diffStr="nonEmpty!";
./clean.sh
rm -rf diffs
mkdir diffs
i=0
rm -rf swt_old
mkdir swt_old
cp -r swt/* swt_old
while [[ ($diffStr != "") && ($i -lt 5) ]];
do
i=$((i+1))
#compile
echo "compiling for iteration $i!"
rm -f swt.jar 
ant >warnings 
rm -f warnings
echo "compiling finished!"

#decompile
echo "decompiling for iteration $i!"
rm -rf swt_output
java -jar decompiler.jar -jar swt.jar -o swt_output >info
rm -f info
mv swt_output/org/eclipse/swt/* swt_output
rm -rf swt_output/org
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
#diffStr=$(diff -r swt_old swt_output);
diff -r swt_old swt_output > diffs/diff_$i
[ -s diffs/diff_$i ]
var=$?
if [ $var -eq 0 ]
then
diffStr="nonEmpty"
else
diffStr=""
fi
rm -rf swt_old
mkdir swt_old
cp -r swt_output/* swt_old
echo "folder comparing finished!"
done
