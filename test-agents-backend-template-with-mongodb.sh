#!/usr/bin/env bash

if [ -f ./build.sbt ] && [ -d ./src/main/g8 ]; then

    export TEMPLATE=`pwd | xargs basename`
    echo ${TEMPLATE}
    mkdir -p target/sandbox
    cd target/sandbox
    sudo rm -r new-shiny-service-with-mongodb
    g8 file://../../../${TEMPLATE} --servicename="New Shiny Service With Mongodb" --serviceport=9999 --mongodb=true "$@"
    cd new-shiny-service-with-mongodb
    git init
    git add .
    git commit -m start
    sbt test it:test

else

    echo "WARNING: run test in the template root folder"
    exit -1

fi

