1)  nawk -F":" '{print $2}' db
2)  nawk -F":" '/[dD]an/{print $2}' db
3)  nawk -F":" '/[sS]usan/{print $1, $2}' db
4)  nawk -F":| " '$2~/^[dD]/{print $2}' db
5)  nawk -F":| " '/^[ecEC]/{print $1}' db
6)  nawk '/^.... /{print $1}' db
7)  nawk '/\(916\)/{print $1}' db
8)  nawk -F":" '/^[mM]ike/{print $3}' db
9)  nawk '{$1=substr($1,1,1);$2=substr($2,1,1);print $1"."$2"."}' db
10) nawk -F":" '$4~/(11|12)\/[0-9]*\/[0-9]*/{print $0}' db


1. awk -F '[: ]' '{print $3 $4 }' db
2. awk -F '[: ]' '/^Dan/{print $3 $4 }' db
3. awk -F '[: ]' '/^Susan/{print $1,$2, $3 $4 }' db
4. awk -F '[: ]' '$2~/^D/{print $2}' db
5. awk '/^[CE]/{print $1}' db
6. awk -F '[: ]' '{if(length($1)==4){print $1}}' db
7. awk -F '[: ]' '$3=="(916)"{print $1}' db
8. awk -F '[: ]' '$1~/Mike$/{print $5}' db
9. awk -F '[: /]' '{print substr($1,1,1) " " substr($2,1,1)}' db
10. awk -F '[: /]' '$6==11 || $6==12 {print $0}' db