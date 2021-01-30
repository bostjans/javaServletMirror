package com.stupica.servlet;


import com.stupica.ConstGlobal;
import com.stupica.core.UtilString;

import java.util.concurrent.atomic.AtomicLong;

import static com.stupica.ConstGlobal.DEFINE_STR_NEWLINE;
import static com.stupica.ConstWeb.*;


public class ServiceMirror {

    /**
     * Object instance variable;
     */
    private static ServiceMirror objInstance;

    public static boolean   isEnabled = true;

    private final int   iCountMaxCache = 11;
    private int         iCountCache = 0;
    String[]            arrCache = null;

    public static AtomicLong  iCountReq = new AtomicLong();
    private AtomicLong  iCountReqGet = new AtomicLong();
    private AtomicLong  iCountReqPut = new AtomicLong();
    private AtomicLong  iCountReqPost = new AtomicLong();
    private AtomicLong  iCountReqPatch = new AtomicLong();
    private AtomicLong  iCountReqDelete = new AtomicLong();
    private AtomicLong  iCountReqOptions = new AtomicLong();
    private AtomicLong  iCountReqTrace = new AtomicLong();
    private AtomicLong  iCountReqNA = new AtomicLong();


    private ServiceMirror() {
        if (isEnabled) {
            arrCache = new String[iCountMaxCache];
        }
    }


    public static ServiceMirror getInstance() {
        if (objInstance == null) {
            objInstance = new ServiceMirror();
        }
        return objInstance;
    }


    //public synchronized int notifyOnRequest(String asHttpData) {
    //    return notifyOnRequest("NA", asHttpData);
    //}
    public synchronized int notifyOnRequest(String asMethod, String asHttpData) {
        // Local variables
        int             iResult;

        // Initialization
        iResult = ConstGlobal.RETURN_OK;

        iCountReq.getAndIncrement();
        if (asMethod.contentEquals(HTTP_METHOD_NAME_GET)) {
            iCountReqGet.getAndIncrement();
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_PUT)) {
            iCountReqPut.getAndIncrement();
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_POST)) {
            iCountReqPost.getAndIncrement();
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_OPTIONS)) {
            iCountReqOptions.getAndIncrement();
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_PATCH)) {
            iCountReqPatch.getAndIncrement();
        } else if (asMethod.contentEquals(HTTP_METHOD_NAME_DELETE)) {
            iCountReqDelete.getAndIncrement();
        } else {
            iCountReqNA.getAndIncrement();
        }

        if (arrCache != null) {
            arrCache[iCountCache++] = asHttpData;
            if (iCountCache == iCountMaxCache) iCountCache = 0;
        }
        return iResult;
    }


    public synchronized String getRequest() {
        // Local variables
        int             iCount = 1;
        StringBuilder   sResult = new StringBuilder();

        // Initialization

        sResult.append("Showing all Requests:").append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount:\t").append(iCountReq.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_GET).append(":\t").append(iCountReqGet.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_PUT).append(":\t").append(iCountReqPut.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_POST).append(":\t").append(iCountReqPost.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_PATCH).append(":\t").append(iCountReqPatch.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_DELETE).append(":\t").append(iCountReqDelete.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_OPTIONS).append(":\t").append(iCountReqOptions.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount ").append(HTTP_METHOD_NAME_TRACE).append(":\t").append(iCountReqTrace.get()).append(DEFINE_STR_NEWLINE);
        sResult.append("\tCount NA:\t").append(iCountReqNA.get()).append(DEFINE_STR_NEWLINE);
        if (arrCache != null) {
            for (String asLoop : arrCache) {
                if (!UtilString.isEmpty(asLoop)) {
                    sResult.append(DEFINE_STR_NEWLINE).append("----------------------------------------").append(DEFINE_STR_NEWLINE);
                    sResult.append(DEFINE_STR_NEWLINE).append("Request: ").append(iCount++).append(DEFINE_STR_NEWLINE);
                    sResult.append(asLoop);
                }
            }
        }
        if (sResult.length() < 1) {
            sResult.append("No Data.");
        }
        return sResult.toString();
    }
}
