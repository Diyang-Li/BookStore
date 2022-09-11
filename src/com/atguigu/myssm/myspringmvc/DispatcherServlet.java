package com.atguigu.myssm.myspringmvc;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static com.atguigu.myssm.util.StringUtil.isEmpty;

/**
 * @author Diyang Li
 * @create 2022-04-11 9:56 AM
 */
// 通配符，wildcard，表示所有的,这里不需要/， * 可以在req中获取
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    //    private Map<String, Object> beanMap = new HashMap<>(); //id: id对应的calss的实例对象
    private BeanFactory beanFactory;

    public DispatcherServlet() {

    }

    public void init() throws ServletException {
        super.init();
        //beanFactory = new ClassPathXmlApplicationContext();
        //之前是在此处主动创建IOC容器的
        //现在优化为从application作用域去获取
        //beanFactory = new ClassPathXmlApplicationContext();
        //中央控制器是mvc中的角色，而IOC是应用程序所有组件的管理，这两者的角色没关系的，所以如果在dispatcher中完成ioc的创建是不合适的，
        //IOC事实上角色要比中央控制器大很多，dispactcher只是一个接受request，找到对应的controller，但是ioc是层的集合，而且包含了他们之间的
        //dependency的一个注入
        ServletContext servletContext = getServletContext();
        Object beanFactoryObj = servletContext.getAttribute("beanFactory");
        if (beanFactoryObj != null) {
            beanFactory = (BeanFactory) beanFactoryObj;
        } else {
            throw new RuntimeException("IOC Container failed to be obtained...");
        }
        /*try {
            //读取xml文件，通过类的加载器， 类装载器负责从Java字符文件将字符流读入内存，并构造Class类对象，所以通过它可以得到一个文件的输入流。
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3.创建Document对象, org.3c.dom
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                //元素节点：一般是拥有一对开闭合标签的元素整体
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    //通过className获取Class对象
                    Class controllerBeanClass = Class.forName(className);
                    //获取实例对象, 这里就是fruitController
                    Object beanObj = controllerBeanClass.getDeclaredConstructor().newInstance();
                    //上面调整之后把beanID放进map中，供后面的id匹配
                    beanMap.put(beanId, beanObj);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/
    }

    //这里选的是不一样的servlet，之前servlet的subclass
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //在Filter中设置了
//        req.setCharacterEncoding("UTF-8");
        //假设url是：  http://localhost:8080/pro15/hello.do
        //那么servletPath是：    /hello.do
        // 我的思路是：

        // 第1步： /hello.do ->   hello   或者  /fruit.do  -> fruit
        // 第2步： hello -> HelloController 或者 fruit -> FruitController
        String servletPath = req.getServletPath();
        //System.out.println("servletPath = " + servletPath);
        servletPath = servletPath.substring(1);//去掉/
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);

        //获取到 servletPath在 beanMap中找到所对应的object
        Object controllerBeanObj = beanFactory.getBean(servletPath);

        String operate = req.getParameter("operate");

        if (isEmpty(operate)) {
            operate = "index";
        }

        try {
            //通过反射的方式，获取到和operate同名的方法,可以用来替代Method获取所有方法，然后switch判断
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (operate.equals(method.getName())) {

                    //1.同意获取请求参数
                    //获取当前参数的数组
                    //获取parametersname需要setting JavaComplier 改成-parameter， 这样获得的名字就不是arg0， arg1...
                    Parameter[] parameters = method.getParameters();
                    //存放参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        //这里不考虑复选框的情况，checkbox那种，目前值考虑单个值的情况
                        //如果参数名是request,response,session 那么就不是通过请求中获取参数的方式了
                        String parameterName = parameter.getName();
                        if ("req".equals(parameterName)) {
                            parameterValues[i] = req;
                        } else if ("resp".equals(parameterName)) {
                            parameterValues[i] = resp;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = req.getSession();
                        } else {
                            //从请求中获取参数值
                            String parameterValue = req.getParameter(parameterName); //在req中拿到参数值
                            //有些值，如fid，price，pageNo等是integer，parameterValue全部都是string，所以要做如下转换
                            String typeName = parameter.getType().getName(); //获取typename

                            Object parameterObj = parameterValue;

                            if (parameterObj != null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }

                            parameterValues[i] = parameterObj;
                        }

                    }

                    //2.controller组件中的方法调用
                    //由dispatcherServlet来调用controllerBean中的方法
                    method.setAccessible(true);
                    //更加标准的做法不是直接强转，而是判断一下是否为null
                    Object returnObj = method.invoke(controllerBeanObj, parameterValues);

                    String methodReturnStr = null;

                    //3.视图处理
/*                    if (StringUtil.isEmpty(methodReturnStr)){
                        return;
                    }*/
                    if (returnObj != null) {
                        methodReturnStr = (String) returnObj;
                    }
                    if (methodReturnStr.startsWith("redirect")) {                  //比如：  redirect:fruit.do
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        resp.sendRedirect(redirectStr);
                    }else if (methodReturnStr.startsWith("json:")){
                        String jsonStr = methodReturnStr.substring("json:".length());
                        PrintWriter out = resp.getWriter();
                        out.println(jsonStr);
                        out.flush();
                    }
                    else {                                                        //return "edit"
                        super.processTemplate(methodReturnStr, req, resp);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DispatcherServletException("DispatcherServlet has Exception");
        }

//        //获取当前类所有的方法
//        Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
//
//        //找到和operate同名的方法
//        for (Method m: methods){
//            String MethodName = m.getName();
//            if (operate.equals(MethodName)){
//                try {
//                    //使用反射技术调用
//                    m.invoke(this, req, resp);
//                    return;
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


    }
}

// 常见错误： IllegalArgumentException: argument type mismatch