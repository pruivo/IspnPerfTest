#!/bin/bash


# Author: Bela Ban


export DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8787"

exec `dirname $0`/kube.sh $*
