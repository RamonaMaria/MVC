package ro.teamnet.zth.api.dispatcher;

import org.codehaus.jackson.map.ObjectMapper;
import ro.teamnet.zth.api.annotations.MyAnnotationConstructor;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ramona.Raducu on 7/20/2017.
 */
public
class MyDispatcherServlet extends HttpServlet {
    private final Map<String, MethodAttributes> methods = new HashMap<>();
    private final Set<Class> services = new HashSet<>();

    protected
    void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchReply(request, response, "POST");
    }

    protected
    void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dispatchReply(request, response, "GET");
    }


    protected
    void dispatchReply(HttpServletRequest request, HttpServletResponse response, String methodType) {
        try {
            Object resultToDisplay = dispatch(request, methodType);
            reply(response, resultToDisplay);
        } catch (Exception e) {
            sendExceptionError(response, e);
        }
    }

    private
    void reply(HttpServletResponse response, Object result) {
        //return result
        ObjectMapper obj = new ObjectMapper();
        String res = null;
        try {
            res = obj.writeValueAsString(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.getWriter().print(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private
    Object getServiceImplementation(Class serviceInterface) {
        for (Class serviceImplementation : services) {
            if (serviceInterface.isAssignableFrom(serviceImplementation))
                try {
                    return serviceImplementation.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }


    private
    Object dispatch(HttpServletRequest request, String methodType) {
        // invoke the right method
        /*
            EmployeeContoller employeeContoller = new EmployeeContoller();
            DepartmentController departmentController = new DepartmentController();
            String[] url;
            url = request.getRequestURI().split("/");

            if (url[3].equals("employees"))
                return employeeContoller;

            if (url[3].equals("departments"))
                return departmentController;
    */

        String path = request.getPathInfo();
        if (path == null) {
            throw new RuntimeException("RunetimeException: requestGetPath");
        }

        String key = methodType + " " + path;
        MethodAttributes methodAttributes = methods.get(key);

        if (methodAttributes == null)
            throw new RuntimeException("RunetimeException: methodAtributes");

        try {
            Object obj = null;
            Class<?> controllerClass = Class.forName(methodAttributes.getControllerClass());
            Constructor<?>[] constructors = controllerClass.getConstructors();
            for (Constructor c : constructors) {
                Class<?>[] constructorParameterTypes = c.getParameterTypes();

                if (constructorParameterTypes.length == 0) {
                    try {
                        obj = controllerClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                if (c.isAnnotationPresent(MyAnnotationConstructor.class) && constructorParameterTypes.length == 1) {
                    Class<?> serviceInterface = constructorParameterTypes[0];
                    Object serviceImplementation = getServiceImplementation(serviceInterface);
                    if (serviceImplementation != null) {
                        try {
                            obj = c.newInstance(serviceImplementation);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }


            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }

    private
    void sendExceptionError(HttpServletResponse response, Exception e) {
        try {
            response.getWriter().print(e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}