#!/usr/bin/env bash

if [ -f ./build.sbt ] && [ -d ./src/main/g8 ]; then

    export TEMPLATE=`pwd | xargs basename`
    echo ${TEMPLATE}
    mkdir -p target/sandbox
    cd target/sandbox
    sudo rm -r new-shiny-service-frontend
    g8 file://../../../${TEMPLATE} --servicename="New Shiny Service" --serviceport=9999
    cd new-shiny-service-frontend
    git init
    git add .
    git commit -m start
    sbt test it:test

else

    echo "WARNING: run test in the template root folder"
    exit -1

fi

