package ro.teamnet.zth;

import org.codehaus.jackson.map.ObjectMapper;
import ro.teamnet.zth.fmk.MethodAttributes;
import ro.teamnet.zth.fmk.domain.HttpMethod;
import ro.teamnet.zth.utils.BeanDeserializator;
import ro.teamnet.zth.utils.ControllerScanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public
class Z2HDispatcherServlet extends HttpServlet {

    private ControllerScanner controllerScanner;

    @Override
    public
    void init() throws ServletException {
        controllerScanner = new ControllerScanner("ro.teamnet.zth.appl.controller");
        controllerScanner.scan();
    }

    @Override
    protected
    void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.GET);
    }

    @Override
    protected
    void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.POST);
    }

    @Override
    protected
    void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.DELETE);
    }

    @Override
    protected
    void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchReply(req, resp, HttpMethod.PUT);
    }

    private
    void dispatchReply(HttpServletRequest req, HttpServletResponse resp, HttpMethod methodType) {
        try {
            Object resultToDisplay = dispatch(req, methodType);
            reply(resp, resultToDisplay);
        } catch (Exception e) {
            try {
                sendExceptionError(e, resp);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private
    void sendExceptionError(Exception e, HttpServletResponse resp) throws IOException {
        resp.getWriter().print(e.getMessage());
    }

    private
    void reply(HttpServletResponse resp, Object resultToDisplay) {
        // todo searialize the output into JSOn usig ObjectMapper (jackson)
        // resultToDisplay = controllerInstance.invokeMETHOD(PARAMS)

        ObjectMapper objectMapper = new ObjectMapper();
        String responseAsString = null;
        try {
            responseAsString = objectMapper.writeValueAsString(resultToDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (responseAsString != null)
                resp.getWriter().print(responseAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private
    Object dispatch(HttpServletRequest req, HttpMethod methodType) {
        // todo to invoke  the controller method for the current request and the controller output
        String path = null;
        String urlPath = req.getPathInfo();
        BeanDeserializator b = new BeanDeserializator();

        if (urlPath == null)
            throw new RuntimeException("Runtime exception: dispatch - urlPath");


       // path = methodType + "/" + urlPath;
        MethodAttributes methodAttributes = controllerScanner.getMetaData(urlPath, methodType);

        if (methodAttributes == null)
            throw new RuntimeException("Runtime exception: dispatch - methodAttributes");
        else {
            try {

                try {
                    Object obj = methodAttributes.getControllerClass().newInstance();
                    Method method = methodAttributes.getMethod();
                    return method.invoke(obj, b.getMethodParams(method, req).toArray());

                } catch (Exception e) {
                    System.out.println("exception 1");
                }
            } catch (Exception e) {
                System.out.println("exception 2");
            }
            //  }
            return null;
        }
    }
}