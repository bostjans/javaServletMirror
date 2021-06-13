# javaServletMirror
HTTP Mirror or feedback page

![CI-Maven-ServletMirror](https://github.com/bostjans/javaServletMirror/workflows/CI-Maven-ServletMirror/badge.svg)

## DEV

To generate Keystore/Truststore file:
```
rm jetty.keystore
keytool -genkey -alias jetty -keyalg RSA -keystore jetty.keystore -storepass secret \
  -keypass secret -dname "CN=localhost, ou=DEV, o=Dev404, st=Lj., c=SI" \
  -validity 3650 -ext SAN=dns:localhost
```

The file(-s) are also available on:
> https://cloud.dev404.net/index.php/s/HYspJem8aaHZbJN

.. in the Map: cert02/


### Test - local

Mirror page:
```
curl -i http://localhost:11080
curl -i http://localhost:11080/mirror/
```
```
curl -i http://localhost:11080/mirror/v1
curl -i -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:11080/mirror/v1
curl -i http://localhost:11080/mirror/v1/secure

curl -i --insecure https://localhost:11443/mirror/v1
curl -i --insecure https://localhost:11443/mirror/v1/secure
```

.. to see the result:
```
curl -i http://localhost:11080/mirror/show/v1
curl -i --insecure https://localhost:11443/mirror/show/v1
```


Monitor page:
```
curl -i http://localhost:11080/mirror/monitor/v1
```


## Sample

URL:
* https://storitve.ezdrav.si/servis/preveriDostopnost
* https://euez-test.ezdrav.si/servis/preveriDostopnost


## Reference

* http://juplo.de/configure-https-for-jetty-maven-plugin-9-0-x/
* https://www.blackpepper.co.uk/blog/jetty-runner-https-xml-configuration
* ----
* https://webhook.site/
* https://hookbin.com/
* https://requestbin.fullcontact.com/
