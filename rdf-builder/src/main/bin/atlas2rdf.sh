#!/bin/sh

base=${0%/*}/..;
current=`pwd`;
java='/Library/Java/JavaVirtualMachines/jdk1.8.0_31.jdk/Contents/Home/bin/java'; # ${java.location};
#args="${java.args}";


for file in `ls $base/lib`
do
  jars=$jars:$base/lib/$file;
done

classpath="$base/config:$jars";

$java -classpath $classpath uk.ac.ebi.spot.rdf.cli.Gxa2RdfDriver $@ 2>&1;
exit $?;