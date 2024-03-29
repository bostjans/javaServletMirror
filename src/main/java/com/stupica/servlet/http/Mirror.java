package com.stupica.servlet.http;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;
import com.stupica.ConstGlobal;
import com.stupica.ConstWeb;
import com.stupica.ResultProces;
import com.stupica.core.UtilDate;
import com.stupica.core.UtilString;
import com.stupica.servlet.ServiceBase;
import com.stupica.servlet.ServiceMirror;
import com.stupica.config.Setting;

import javax.security.auth.x500.X500Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

import static com.stupica.ConstGlobal.DEFINE_STR_NEWLINE;
import static com.stupica.ConstWeb.*;


/**
 * Created by bostjans on 16/09/16.
 */
public class Mirror extends ServiceBase {

    private static int      iLenPayloadMax = 8196;
    private static boolean  bShouldDoFullResponse = false;

    private static Logger   logger = Logger.getLogger(Mirror.class.getName());


    static {
        bShouldDoFullResponse = Setting.getConfig().getBoolean("Http.Response.Full", true);
        iLenPayloadMax = Setting.getConfig().getInt("Http.Payload.Length", 8192);
    }


    /**
     * @api {get} /mirror/v1/ Mirror
     * @apiName Mirror
     * @apiGroup Monitor
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiDescription Web method to monitor system status/accessibility.
     *
     * @apiSuccessExample {text} Success-Response:
     *     HTTP/1.1 200 OK
     *
     *     ..
     *
     * @apiSampleRequest /mirror/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i --insecure https://localhost:11443/mirror/v1
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequestMirror(HTTP_METHOD_NAME_GET, request, response);
    }

    /**
     * @api {put} /mirror/v1/ Mirror
     * @apiName Mirror
     * @apiGroup Monitor
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiDescription Web method to monitor system status/accessibility.
     *
     * @apiSuccessExample {text} Success-Response:
     *     HTTP/1.1 200 OK
     *
     *     ..
     *
     * @apiSampleRequest /mirror/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i -X PUT --insecure https://localhost:11443/mirror/v1
     *     curl -i -X PUT --insecure https://localhost:11443/mirror/v1 --header "Content-Type: application/json" -d "{test:1}"
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequestMirror(HTTP_METHOD_NAME_PUT, request, response);
    }

    /**
     * @api {post} /mirror/v1/ Mirror
     * @apiName Mirror
     * @apiGroup Monitor
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiDescription Web method to monitor system status/accessibility.
     *
     * @apiSuccessExample {text} Success-Response:
     *     HTTP/1.1 200 OK
     *
     *     ..
     *
     * @apiSampleRequest /mirror/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i -X POST --insecure https://localhost:11443/mirror/v1
     *     curl -i -X POST --insecure https://localhost:11443/mirror/v1 --header "Content-Type: application/text" -d "test"
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequestMirror(HTTP_METHOD_NAME_POST, request, response);
    }

    /**
     * @api {patch} /mirror/v1/ Mirror
     * @apiName Mirror
     * @apiGroup Monitor
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiDescription Web method to monitor system status/accessibility.
     *
     * @apiSuccessExample {text} Success-Response:
     *     HTTP/1.1 200 OK
     *
     *     ..
     *
     * @apiSampleRequest /mirror/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i -X PATCH --insecure https://localhost:11443/mirror/v1
     */
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequestMirror(HTTP_METHOD_NAME_PATCH, request, response);
    }

    /**
     * @api {delete} /mirror/v1/ Mirror
     * @apiName Mirror
     * @apiGroup Monitor
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiDescription Web method to monitor system status/accessibility.
     *
     * @apiSuccessExample {text} Success-Response:
     *     HTTP/1.1 200 OK
     *
     *     ..
     *
     * @apiSampleRequest /mirror/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i -X DELETE --insecure https://localhost:11443/mirror/v1
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequestMirror(HTTP_METHOD_NAME_DELETE, request, response);
    }

    /**
     * @api {options} /mirror/v1/ Mirror
     * @apiName Mirror
     * @apiGroup Monitor
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiDescription Web method to monitor system status/accessibility.
     *
     * @apiSuccessExample {text} Success-Response:
     *     HTTP/1.1 200 OK
     *
     *     ..
     *
     * @apiSampleRequest /mirror/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i -X OPTIONS --insecure https://localhost:11443/mirror/v1
     */
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequestMirror(HTTP_METHOD_NAME_OPTIONS, request, response);
    }

    protected void doRequestMirror(String asMethod, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Local variables
        int             iResult;
        boolean         bRequestForm = false;
        boolean         bResponseJson = false;
        boolean         bResponseXml = false;
        String          sTemp;
        String          sHttpData = null;
        StringBuilder   sResponse = new StringBuilder();
        JsonObject      objResponseJson = null;
        PrintWriter     objOut = null;
        ResultProces    objResult;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        bVerifyReferal = false;
        objResult = new ResultProces();

        // Check, if specific response type is requested (JSON, XML, ..)
        sTemp = request.getHeader("Accept");
        if (!UtilString.isEmptyTrim(sTemp)) {
            if (sTemp.toLowerCase().contains("json")) {
                bResponseJson = true;
                objResponseJson = Json.object();
            }
            if (sTemp.toLowerCase().contains("xml"))
                bResponseXml = true;
        }
        // Assume: if input Content-Type is JSON then produce Response in JSON
        sTemp = request.getHeader("Content-Type");
        if (!UtilString.isEmptyTrim(sTemp)) {
            if (sTemp.toLowerCase().contains("json")) {
                bResponseJson = true;
                objResponseJson = Json.object();
            }
            if (sTemp.toLowerCase().contains("xml"))
                bResponseXml = true;
            if (sTemp.toLowerCase().contains("x-www-form-urlencoded"))
                bRequestForm = true;
        }

        sResponse.append("Method: ");
        if (asMethod.contentEquals(HTTP_METHOD_NAME_GET)) {
            sResponse.append(HTTP_METHOD_NAME_GET);
            if (bResponseJson)
                objResponseJson.add("Method", HTTP_METHOD_NAME_GET);
            super.doGet(request, response);
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_PUT)) {
            sResponse.append(HTTP_METHOD_NAME_PUT);
            if (bResponseJson)
                objResponseJson.add("Method", HTTP_METHOD_NAME_PUT);
            super.doPut(request, response);
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_POST)) {
            sResponse.append(HTTP_METHOD_NAME_POST);
            if (bResponseJson)
                objResponseJson.add("Method", HTTP_METHOD_NAME_POST);
            super.doPost(request, response);
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_OPTIONS)) {
            sResponse.append(HTTP_METHOD_NAME_OPTIONS);
            if (bResponseJson)
                objResponseJson.add("Method", HTTP_METHOD_NAME_OPTIONS);
            super.doOptions(request, response);
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_PATCH)) {
            sResponse.append(HTTP_METHOD_NAME_PATCH);
            if (bResponseJson)
                objResponseJson.add("Method", HTTP_METHOD_NAME_PATCH);
            super.doPatch(request, response);
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_DELETE)) {
            sResponse.append(HTTP_METHOD_NAME_DELETE);
            if (bResponseJson)
                objResponseJson.add("Method", HTTP_METHOD_NAME_DELETE);
            super.doDelete(request, response);
        } else {
            iResult = response.getStatus();
            objResult.sText = "doRequest(): Unknown method! asMethod: " + asMethod;
            logger.warning(objResult.sText);
            response.setStatus(ConstWeb.HTTP_RESP_NOT_FOUND);
            sResponse.append("NA");
            if (bResponseJson)
                objResponseJson.add("Method", "NA")
                        .add("Text", "doRequest(): Unknown method! asMethod: " + asMethod);
        }

        // Check ..
        if (response.getStatus() != ConstWeb.HTTP_RESP_OK) {
            iResult = response.getStatus();
            objResult.sText = "doRequest(): StatusCode indicates error in prior verification! Status: " + response.getStatus();
            logger.warning(objResult.sText);
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            if (bResponseJson)
                response.setContentType("application/json; charset=UTF-8");
            else
                response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            sHttpData = getReportText(request, asMethod, objResponseJson, bRequestForm);
            if (UtilString.isEmptyTrim(sHttpData)) {
                objResult.sText = "doRequest(): Request info. not collected! Status: " + response.getStatus();
                logger.warning(objResult.sText);
                iResult = ConstGlobal.RETURN_WARN;
            }
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            sResponse.append("\t> Response: ").append(response.getStatus());
            sResponse.append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("Response", response.getStatus());
        }

        // Check previous step
        if (iResult != ConstGlobal.RETURN_ERROR) {
            objOut = response.getWriter();
            if (objOut == null) {
                iResult = ConstGlobal.RETURN_ERROR;
            }
            //objOut.println("<pre>");
        }
        //objOut.println("</pre>");
        if (objOut != null) {
            objOut.write(DEFINE_STR_NEWLINE);
            if (bResponseJson) {
                objOut.write(objResponseJson.toString(WriterConfig.PRETTY_PRINT));
            } else {
                objOut.write(sResponse.toString());
                if (bShouldDoFullResponse) {
                    objOut.write(sHttpData);
                }
            }
        }
        if (objOut != null) {
            objOut.close();
        }
    }


    protected String getReportText(HttpServletRequest request, String asMethod, JsonObject aobjResponseJson,
                                   boolean abRequestForm) {
        // Local variables
        int             iResult;
        boolean             bResponseJson = false;
        String              sMsgLog = null;
        String              headers = "";
        StringBuilder       headersLong = new StringBuilder();
        Enumeration<String> headerNames = null;
        StringBuilder       sHttpData = new StringBuilder();
        StringBuilder       sHttpPayload = new StringBuilder();
        StringBuilder       sDataPayload = new StringBuilder();
        JsonObject          objResponseJson = null;
        JsonObject          objHeaderJson = null;
        JsonObject          objCertJson = null;
        JsonObject          objPayloadJson = null;
        JsonObject          objTempJson = null;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        if (aobjResponseJson != null) {
            bResponseJson = true;
            objResponseJson = Json.object();
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            if (bResponseJson) {
                objHeaderJson = Json.object();
            }
            headers = "Header(s): \n";
            headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers = headers + "  " + headerName + " :\t";
                String headerValue = request.getHeader(headerName);
                if (headerValue.length() > 80) {
                    headersLong.append("\nHeader Name: ").append(headerName);
                    headersLong.append("\n\tValue: ").append(headerValue);
                    headers = headers + headerValue.substring(0, 77) + " ..";
                } else {
                    headers = headers + headerValue;
                }
                headers = headers + "\n";
                if (bResponseJson)
                    objHeaderJson.add(headerName, headerValue);
            }
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            sHttpData.append("--").append(DEFINE_STR_NEWLINE);
            sHttpData.append(".. HTTP Mirror").append(DEFINE_STR_NEWLINE);
            sHttpData.append("--").append(DEFINE_STR_NEWLINE);
            if (bResponseJson) {
                objResponseJson.add("httpStart11", "--");
                objResponseJson.add("httpTitle", "HTTP Mirror");
                objResponseJson.add("httpStart12", "--");
                objTempJson = Json.object();
            }

            //long currentTimeMillis = System.currentTimeMillis();
            Date    dtNow = new Date();
            String  requestIdent = "IdAccess/IdDostopa: " + ServiceMirror.iCountReq.get() + ":" + dtNow.getTime();
            if (bResponseJson)
                objTempJson.add("IdAccess", ServiceMirror.iCountReq.get() + ":" + dtNow.getTime());
            logger.info("getReportText(): " + requestIdent);
            logger.fine(headers);
            sHttpData.append(requestIdent);
            sHttpData.append("\t\t-> TimeStamp: ").append(UtilDate.toUniversalString(dtNow)).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objTempJson.add("TimeStamp", UtilDate.toUniversalString(dtNow));
            sHttpData.append("Method: ").append(asMethod).append(DEFINE_STR_NEWLINE);
            if (bResponseJson) {
                objTempJson.add("Method", asMethod).add("filler11", "--");
                objResponseJson.add("Id", objTempJson);
            }
            sHttpData.append(DEFINE_STR_NEWLINE);
            sHttpData.append("--").append(DEFINE_STR_NEWLINE);
            sHttpData.append("AuthType:\t").append(request.getAuthType()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("AuthType", request.getAuthType());
            sHttpData.append("ContextPath:\t").append(request.getContextPath()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("ContextPath", request.getContextPath());
            sHttpData.append("PathInfo:\t").append(request.getPathInfo()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("PathInfo", request.getPathInfo());
            sHttpData.append("RemoteUser:\t").append(request.getRemoteUser()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("RemoteUser", request.getRemoteUser());
            sHttpData.append("RequestedSessionId:\t").append(request.getRequestedSessionId()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("RequestedSessionId", request.getRequestedSessionId());
            sHttpData.append("RequestURI:\t").append(request.getRequestURI()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("RequestURI", request.getRequestURI());
            sHttpData.append("RemoteAddr:\t").append(request.getRemoteAddr()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("RemoteAddr", request.getRemoteAddr());
            sHttpData.append("RemoteHost:\t").append(request.getRemoteHost()).append("\t\t");
            if (bResponseJson)
                objResponseJson.add("RemoteHost", request.getRemoteHost());
            sHttpData.append("RemotePort:\t").append(request.getRemotePort()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("RemotePort", request.getRemotePort());
            sHttpData.append("Scheme:\t").append(request.getScheme()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("Scheme", request.getScheme());
            sHttpData.append("ServerName:\t").append(request.getServerName()).append("\t\t");
            if (bResponseJson)
                objResponseJson.add("ServerName", request.getServerName());
            sHttpData.append("ServerPort:\t").append(request.getServerPort()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("ServerPort", request.getServerPort());
            sHttpData.append("SessionId:\t").append(request.getSession().getId()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("SessionId", request.getSession().getId());
            sHttpData.append("\t").append(request.getSession()).append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objResponseJson.add("Session", request.getSession().toString());

            sHttpData.append(DEFINE_STR_NEWLINE).append("--").append(DEFINE_STR_NEWLINE);
            sHttpData.append(headers);
            if (headersLong.length() > 0) {
                sHttpData.append("--").append(DEFINE_STR_NEWLINE);
                sHttpData.append(headersLong.toString()).append(DEFINE_STR_NEWLINE);
            }
            if (bResponseJson)
                objResponseJson.add("Header", objHeaderJson);
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            if (bResponseJson)
                objCertJson = Json.object();
            sHttpData.append("--").append(DEFINE_STR_NEWLINE);
            X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
            if (certs != null) {
                X509Certificate x509Certificate = certs[0];
                X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
                String dn = subjectX500Principal.getName();
                x509Certificate.getSerialNumber();
                sMsgLog = "javax.servlet.request.X509Certificate (serial): " + x509Certificate.getSerialNumber().toString(16)
                        + "\n\t" + dn;
                if (bResponseJson)
                    objCertJson.add("javax.servlet.request.X509Certificate.serial", x509Certificate.getSerialNumber().toString(16));
            } else {
                sMsgLog = "No (client) Certificate received. / Nismo prejeli klient (client) certifikata (po HTTP_Header: [javax.servlet.request.X509Certificate]).";
                if (bResponseJson)
                    objCertJson.add("javax.servlet.request.X509Certificate.serial", "No (client) Certificate received. / Nismo prejeli klient (client) certifikata.");
            }
            //logger.info(sMsgLog);
            sHttpData.append(DEFINE_STR_NEWLINE).append(sMsgLog).append(DEFINE_STR_NEWLINE);
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            String clientCertString = request.getHeader("SSL_CLIENT_CERT");
            X509Certificate clientCert = createCertificateFromString(clientCertString);
            if (clientCert != null) {
                String sslClientSerial = clientCert.getSerialNumber().toString(16);
                sHttpData.append("--").append(DEFINE_STR_NEWLINE);
                sHttpData.append("SSL_CLIENT_CERT: ").append(sslClientSerial).append(DEFINE_STR_NEWLINE);
                sHttpData.append("\t").append(clientCert.getSubjectX500Principal().getName()).append(DEFINE_STR_NEWLINE);
                //logger.info("SSL_CLIENT_CERT:" + sslClientSerial);
                if (bResponseJson) {
                    objCertJson.add("SSL_CLIENT_CERT.serial", sslClientSerial);
                    objCertJson.add("SSL_CLIENT_CERT.subject", clientCert.getSubjectX500Principal().getName());
                }
            }
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            String clientCertVSString = request.getHeader("SSL_CLIENT_CERT_VS");
            X509Certificate clientCertVS = createCertificateFromString(clientCertVSString);
            if (clientCertVS != null) {
                String sslClientSerial = clientCertVS.getSerialNumber().toString(16);
                sHttpData.append("--").append(DEFINE_STR_NEWLINE);
                sHttpData.append("SSL_CLIENT_CERT_VS: ").append(sslClientSerial).append(DEFINE_STR_NEWLINE);
                sHttpData.append("\t").append(clientCertVS.getSubjectX500Principal().getName()).append(DEFINE_STR_NEWLINE);
                if (bResponseJson) {
                    objCertJson.add("SSL_CLIENT_CERT_VS.serial", sslClientSerial);
                    objCertJson.add("SSL_CLIENT_CERT_VS.subject", clientCertVS.getSubjectX500Principal().getName());
                }
            }
        }
        if (bResponseJson)
            objResponseJson.add("Certificate", objCertJson);

        sHttpData.append(DEFINE_STR_NEWLINE).append("--").append(DEFINE_STR_NEWLINE);

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            iResult = readRequestData(request, sDataPayload, abRequestForm);
        }
        if (iResult == ConstGlobal.RETURN_OK) {
            if (bResponseJson)
                objPayloadJson = Json.object();
            if (sDataPayload.length() > 0) {
                sHttpPayload.append("PayLoad: present  >  Length: ").append(sDataPayload.length()).append(DEFINE_STR_NEWLINE);
                if (sDataPayload.length() < iLenPayloadMax) {
                    sHttpPayload.append("PayLoad data:").append(DEFINE_STR_NEWLINE);
                    sHttpPayload.append(sDataPayload).append(DEFINE_STR_NEWLINE);
                    if (bResponseJson) {
                        objPayloadJson.add("PayLoad.length", sDataPayload.length());
                        objPayloadJson.add("PayLoad", sDataPayload.toString());
                    }
                }
            } else {
                sHttpPayload.append("PayLoad: not present  >  Length: ").append(sDataPayload.length()).append(DEFINE_STR_NEWLINE);
                if (bResponseJson)
                    objPayloadJson.add("PayLoad.length", sDataPayload.length())
                            .add("PayLoad", "not present");
            }
        } else {
            sHttpPayload.append("PayLoad: Unknown - error at data retrieve!").append(DEFINE_STR_NEWLINE);
            if (bResponseJson)
                objPayloadJson.add("PayLoad", "Unknown - error at data retrieve!");
        }
        if (iResult == ConstGlobal.RETURN_OK) {
            if (sHttpPayload.length() > 0) {
                sHttpData.append(sHttpPayload);
                if (bResponseJson)
                    objResponseJson.add("PayLoad", objPayloadJson);
            }
        }

        sHttpData.append(DEFINE_STR_NEWLINE);
        sHttpData.append("-- End of Request report. --<");

        // Notify Service
        ServiceMirror.getInstance().notifyOnRequest(asMethod, sHttpData.toString());
        if (bResponseJson) {
            aobjResponseJson.add("http", objResponseJson);
            return objResponseJson.toString(WriterConfig.PRETTY_PRINT);
        } else
            return sHttpData.toString();
    }

    private X509Certificate createCertificateFromString(String certFromSSLClientCertHeader) {
        X509Certificate userCert = null;
        CertificateFactory cf;

        try {
            if (!UtilString.isEmptyTrim(certFromSSLClientCertHeader)) {
                String strcert1 = certFromSSLClientCertHeader.replace(' ', '\n'); // replace .. presledkov damo newline
                String strcert2 = strcert1.substring(28, strcert1.length() - 26); // remove -----BEGIN CERTIFICATE----- in
                // -----END CERTIFICATE----- del, ker sta
                // imela presledke, ki so zdaj newline
                String strcert3 = new String(ConstGlobal.sCertStart + "\n"); // add -----BEGIN CERTIFICATE-----
                String strcert4 = strcert3.concat(strcert2); // dodamo vsebino certa
                String rebuildedCert = strcert4.concat("\n" + ConstGlobal.sCertStop + "\n"); // add -----END CERTIFICATE-----

                try {
                    cf = CertificateFactory.getInstance("X.509");
                    userCert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(rebuildedCert.getBytes("UTF-8")));
                } catch (CertificateException e) {
                    logger.severe("createCertificateFromString(): Error .."
                            + " Msg.: " + e.getMessage());
                    // throw createSOAPFault("VSInvalidParameter.", "VSInvalidParameter");
                } catch (UnsupportedEncodingException e) {
                    logger.severe("createCertificateFromString(): Error .."
                            + " Msg.: " + e.getMessage());
                    // throw createSOAPFault("VSInvalidParameter.", "VSInvalidParameter");
                }
            }
        } catch (Exception e) {
            logger.severe("createCertificateFromString(): Error .."
                    + " Msg.: " + e.getMessage());
        }
        return userCert;
    }
}
