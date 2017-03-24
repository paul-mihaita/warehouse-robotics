num=0
for entry in ./**/*.java
do
    add=$(git blame "$entry" | grep "rav" | wc -l)
    num=$(python -c "print $num+$add")
done
echo "$num"
