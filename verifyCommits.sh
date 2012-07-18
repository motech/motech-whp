git checkout master
git pull --rebase origin master
git log --oneline > commits-master
git checkout release-0.2
git pull --rebase origin release-0.2
git log --oneline > commits-release
git checkout master
MISSING_LINES=""
FLAG=0
while read line
do
     COMMIT=`echo $line | awk '{print substr($0, index($0,$2))}'`;
     grep "$COMMIT" commits-release > /dev/null
     if [ $? -eq 1 ]; then
       grep "$line" commitignore > /dev/null
       if [ $? -eq 1 ]; then
        grep -i "commitignore" $line > /dev/null
        if [ $? -eq 1 ];then
        	MISSING_LINE=$MISSING_LINES"Did not find "$line" in release\n"
		FLAG=1
	fi
       fi
     fi
done < commits-master
git checkout release-0.2
while read line
do
     COMMIT=`echo $line | awk '{print substr($0, index($0,$2))}'`;
     grep "$COMMIT" commits-master > /dev/null
     if [ $? -eq 1 ]; then
       grep "$line" commitignore > /dev/null
       if [ $? -eq 1 ]; then
       	grep -i "commitignore" $line > /dev/null
       	if [ $? -eq 1 ];then
               MISSING_LINE=$MISSING_LINES"Did not find "$line" in master\n"
               FLAG=1
       	fi
       fi
     fi
done < commits-release
git checkout master
if [ $FLAG -eq 1 ];then
   echo $MISSING_LINES
   exit 1
fi
exit 0
