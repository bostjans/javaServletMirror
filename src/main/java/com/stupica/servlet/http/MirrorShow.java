package com.stupica.servlet.http;


import com.stupica.ConstGlobal;
import com.stupica.ConstWeb;
import com.stupica.ResultProces;
import com.stupica.servlet.ServiceMirror;
import com.stupica.servlet.ServiceBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;


public class MirrorShow extends ServiceBase {

    private static Logger logger = Logger.getLogger(MirrorShow.class.getName());

    /**
     * @api {get} /mirrorShow/v1/ MirrorShow
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
     * @apiSampleRequest /mirrorShow/v1/
     *
     * @apiExample {curl} Example usage:
     *     curl -i http://localhost:11080/mirror/show/v1
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Local variables
        int             iResult;
        String          sHttpData = null;
        PrintWriter     objOut = null;
        ResultProces    objResult;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;
        bVerifyReferal = false;
        objResult = new ResultProces();

        super.doGet(request, response);

        // Check ..
        if (response.getStatus() != ConstWeb.HTTP_RESP_OK) {
            iResult = response.getStatus();
            objResult.sText = "doGet(): StatusCode indicates error in prior verification! Status: " + response.getStatus();
            logger.warning(objResult.sText);
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            response.setContentType("text/plain; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            sHttpData = ServiceMirror.getInstance().getRequest();
        }

        // Check previous step
        if (iResult == ConstGlobal.RETURN_OK) {
            objOut = response.getWriter();
            if (objOut == null) {
                iResult = ConstGlobal.RETURN_ERROR;
            }
        }
        if (objOut != null) {
            objOut.write(sHttpData);
        }
        if (objOut != null) {
            objOut.close();
        }
    }
}
