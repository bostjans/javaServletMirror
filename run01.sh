#!/bin/sh
echo Start ..

PATH_PROG=.
FILE_PROG=servletMirror.war

PATH_JETTY_RUNNER=.
FILE_JETTY_RUNNER=jetty-runner.jar
PATH_WAR=.

VMparam="-server"
VMparam="-Xms256m -Xmx396m $VMparam"
VMparam="-Djetty.home=$PATH_PROG $VMparam"
VMparam="-Dsun.security.ssl.allowUnsafeRenegotiation=true $VMparam"

echo Starting server  ..
java $VMparam -jar $PATH_JETTY_RUNNER/$FILE_JETTY_RUNNER \
  --config jetty-runner.xml \
  --path /mirror $PATH_WAR/servletMirror.war

exit 0
