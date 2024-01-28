#!/bin/sh
echo "Start .."

PATH_PROG=.
FILE_PROG=servletMirror.war
FILE_CONF=$PATH_PROG/properties.xml

PATH_JETTY_RUNNER=.
FILE_JETTY_RUNNER=jetty-runner.jar
PATH_WAR=$PATH_PROG

#echo Setting conf file ..
#export APP_CONF_FILE=$FILE_CONF

VMparam="-server"
VMparam="-Xms256m -Xmx396m $VMparam"
VMparam="-Djetty.home=$PATH_PROG $VMparam"
VMparam="-Dsun.security.ssl.allowUnsafeRenegotiation=true $VMparam"

echo "Starting server  .."
nohup java $VMparam -jar $PATH_JETTY_RUNNER/$FILE_JETTY_RUNNER \
  --config jetty-runner.xml \
  --path / $PATH_WAR/servletMirror.war &

exit 0
