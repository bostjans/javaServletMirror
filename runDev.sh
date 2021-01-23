#!/bin/sh
echo Start ..

PATH_PROG=.
FILE_PROG=servletMirror.war

PATH_JETTY_RUNNER=target/dependency
FILE_JETTY_RUNNER=jetty-runner.jar
PATH_WAR=target

#VMparam="-server"
VMparam="-Xms256m -Xmx396m"
VMparam="-Djetty.home=$PATH_PROG $VMparam"
VMparam="-Dsun.security.ssl.allowUnsafeRenegotiation=true $VMparam"

echo Var ..
echo $PATH_JETTY_RUNNER/$FILE_JETTY_RUNNER

#java -jar $PATH_JETTY_RUNNER/$FILE_JETTY_RUNNER --help
echo Starting server  ..
#java -jar $PATH_JETTY_RUNNER/$FILE_JETTY_RUNNER --port 11080 --path /mirror $PATH_WAR/servletMirror.war
java $VMparam -jar $PATH_JETTY_RUNNER/$FILE_JETTY_RUNNER \
  --config jetty-runner.xml \
  --path /mirror $PATH_WAR/servletMirror.war

exit 0
