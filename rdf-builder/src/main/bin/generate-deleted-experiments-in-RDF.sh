#!/bin/sh

# read experiments from old dir and check which don't exist in new dir
# print out void statements that link to the old dataset description URI


OLDDIR=$1
NEWDIR=$2
VOIDURI=$3

if [ ! $OLDDIR ] ; then
    echo `date` "Missing arg1: old file directory (e.g. '/nfs/production2/linked-data/atlas/13.07')"
    exit 1
fi

if [ ! $NEWDIR ] ; then
    echo `date` "Missing arg2: new file directory (e.g. '/nfs/production2/linked-data/atlas/15-08-2014')"
    exit 1
fi

if [ ! $VOIDURI ] ; then
     echo `date` "Missing arg3: void URI (e.g. 'http://rdf.ebi.ac.uk/dataset/atlas/13.07)"
     exit 1
fi

for fname in $OLDDIR/E-*;
do
    basename=${fname##*/} ;
    experiment=${basename%.*}
    filename=$NEWDIR/$experiment.ttl
    if [ -f $filename ] ; then
    	echo "<http://rdf.ebi.ac.uk/resource/atlas/$experiment> a <http://rdf.ebi.ac.uk/terms/atlas/Experiment> ."
    	echo "<http://rdf.ebi.ac.uk/resource/atlas/$experiment> <http://rdfs.org/ns/void#inDataset> <$VOIDURI> ."
    fi
done

