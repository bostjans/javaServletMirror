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
curl -i http://localhost:11080/v1
curl -i -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:11080/v1
curl -i http://localhost:11080/v1/secure

curl -i --insecure https://localhost:11443/mirror/v1
curl -i --insecure https://localhost:11443/mirror/v1/secure
```
```
ab -v 2 -c 1 -n 10 -l http://localhost:11080/mirror/get01/
ab -v 1 -c 1 -n 1000 -l http://localhost:11080/mirror/get01/
ab -c 2 -n 11000 -l http://localhost:11080/mirror/get02/
ab -c 4 -n 22000 -l http://localhost:11080/mirror/get04/
ab -c 8 -n 42000 -l http://localhost:11080/mirror/get08/
ab -c 16 -n 82000 -l http://localhost:11080/mirror/get16/
```

.. to see the result:
```
curl -i http://localhost:11080/show/v1
curl -i --insecure https://localhost:11443/show/v1
```


Monitor page:
```
curl -i http://localhost:11080/monitor/v1
```


## Sample

URL:
* https://n19b.stupica.com/test/request


## Reference

* http://juplo.de/configure-https-for-jetty-maven-plugin-9-0-x/
* https://www.blackpepper.co.uk/blog/jetty-runner-https-xml-configuration
* ----
* https://webhook.site/
* https://hookbin.com/
* https://requestbin.fullcontact.com/
