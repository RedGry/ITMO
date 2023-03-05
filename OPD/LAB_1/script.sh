#!/bin/bash

# Prepare environment
cd ~

until rm -rf `find lab0 2> /dev/null` 2> /dev/null; do
chmod -R 777 lab0 2> /dev/null
done

# Dirs

mkdir -p lab0/armaldo2/chinchou

cd lab0

mkdir armaldo2/croagunk

mkdir -p garchomp4/croconaw
mkdir garchomp4/kabuto

mkdir -p metagross2/electrode
mkdir metagross2/ponyta

# Files
echo "Способности Charm Growl Metronome Sweet Kiss Yawn Encore
Follow Me Bestow Wish Ancientpower Safeguard Baton Pass Double-Edge
Last Resort After You" >armaldo2/togepi

echo "Возможности Overland=7 Surface=2
Jump=3 Power=3 Intelligence=4 Sprouter=0" >armaldo2/victreebel

echo "Способности Torrent
Hyper Cutter Inner Focus">dewott8

echo "Ходы After You Block Covet Earth
Power Foul Play Helping Hand Low Kick Magic Coat Role Play Rollout
Sleep Talk Snore Stealth Rock Sucker Punch
Uproar">garchomp4/bonsly

echo "Способности Unbreakable Plus
Minus">metagross2/klinklang

echo "weight=357.1 height=244.0 atk=6 def=8">milotic6
echo "Тип
диеты Herbivore Phototroph">turtwig1

# Chmod

chmod ug-r+wx,o+rx-w armaldo2
chmod u-r+wx,g-r+wx,o+rx-w armaldo2/croagunk
chmod u+r-wx,g-rwx,o-rwx armaldo2/togepi
chmod u-x+rw,g-rx+w,o-wx+r armaldo2/victreebel
chmod 751 armaldo2/chinchou

chmod u-wx+r,g-rwx,o-wx+r dewott8

chmod u+rx-w,g-rw+x,o-r+wx garchomp4
chmod u+wx-r,g+rwx,o+rx-w garchomp4/croconaw
chmod u+r-wx,g+r-wx,o-rwx garchomp4/bonsly
chmod 355 garchomp4/kabuto

chmod u+rx-w,g-rx+w,o+r-wx metagross2
chmod u+rw-x,g+r-wx,o-rwx metagross2/klinklang
chmod u-r+wx,g+wrx,o-r+wx metagross2/ponyta
chmod 770 metagross2/electrode

chmod u+r-wx,g-rwx,o-rwx milotic6
chmod u+rw-x,g-rwx,o-rwx turtwig1

# Copiesand links

# FIX
chmod u+w garchomp4
# /FIX
ln -s dewott8 garchomp4/bonslydewott
# FIX
chmod u-w garchomp4
# /FIX

cat armaldo2/togepi armaldo2/togepi > milotic6_44
ln dewott8 armaldo2/victreebeldewott

# Файлы victreebeldewott и dewott8 идентичны, нет смысла следующей команды

cp dewott8 armaldo2/victreebeldewott

# FIX
chmod u+r armaldo2
chmod u+r armaldo2/croagunk
chmod u+r garchomp4/croconaw
# /FIX
cp -r armaldo2 garchomp4/croconaw
# FIX
chmod u-r armaldo2/croagunk
chmod u-r armaldo2
chmod u-r garchomp4/croconaw/armaldo2
chmod u-r garchomp4/croconaw
# /FIX

cp milotic6 metagross2/electrode
ln -s armaldo2 Copy_87

ls -lR

# Tasks
cat <<EOF
	4. Tasks
EOF

echo ' 1)'
wc -l ./armaldo2/{togepi,victreebel,victreebeldewott} 2>/dev/null | sort -rn

echo ' 2)'
find . -name 'm*' -type f -ls 2>/tmp/${LOGNAME}err | sort -n | tail -3

echo ' 3)'
find . -name 't*' -type f -exec cat -n '{}' ';' 2>&1 | sort -k 2

echo ' 4)'
cat ./metagross2/klinklang 2>/tmp/${LOGNAME}err2| grep '*h'

echo ' 5)'
ls -l ./{,*/,*/*/} | egrep 'cro'| sort=time

echo ' 6)'
find . -type f -name 'k*' 2>/dev/null | sort -rn | head -2

# Remove
rm -f dewott8
rm -f armaldo2/victreebel
rm -f Copy_*
rm -f armaldo2/victreebeldewo*

# FIX
chmod u+r armaldo2
# /FIX
rm -rf armaldo2

#Я уже удалить -_- директорию всю! Ну ладно
rm -rf armaldo2/chinchou 
