#!/bin/bash

classFolder=$1
output=$2

OIFS=$IFS
IFS=$'\n'


for semester in $(ls $classFolder); do
    semesterFolder="$output/$semester"
    mkdir "$semesterFolder"

    for assignment in $(ls -d "$classFolder/$semester/"*/); do
        assignmentNumber=$(ls "$assignment" | sed -r 's|.*([0-9]+).*|\1|')

        for student in $(ls -d "${assignment}Assignment $assignmentNumber/"*/); do

            studentID=$(basename $student)
            studentWork="${student}Submission attachment(s)"

            [ ! -d "$studentWork" ] && continue

            dataFile=$(find $studentWork/ -name *"assignment$assignmentNumber"*"csv"* | grep -m1 "" )

            if [ ! -z "$dataFile" ]; then
                [ ! -d "$semesterFolder/$studentID" ] && mkdir "$semesterFolder/$studentID"
                cp "$dataFile" "$semesterFolder/$studentID"
            fi

        done
    done
done

IFS=$OIFS
echo "done"

