package com.codegym.web;

import java.io.File;
import java.io.IOException;

import com.codegym.web.servlets.*;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.jasper.servlet.JasperInitializer;
import com.codegym.web.repository.InMemoryUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatMain {
    private static final Logger logger = LoggerFactory.getLogger("TomcatMain");

    public static void main(String[] args) throws LifecycleException, IOException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        String baseDir = new File("target/tomcat").getAbsolutePath();
        tomcat.setBaseDir(baseDir);
        String webAppDir = new File("src/main/webapp").getCanonicalPath();
        new File(webAppDir).mkdirs();
        Context context = tomcat.addWebapp("", webAppDir);
        context.addWelcomeFile("index.jsp");
        context.addServletContainerInitializer(new JasperInitializer(), null);

        ContextConfig ctxCfg = new ContextConfig();
        context.addLifecycleListener(ctxCfg);
        ctxCfg.setDefaultWebXml("org/apache/catalina/startup/NO_DEFAULT_XML");
        context.setXmlValidation(false);
        context.setXmlNamespaceAware(false);

        InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();
        Wrapper jsp = Tomcat.addServlet(context, "jsp", "org.apache.jasper.servlet.JspServlet");
        jsp.addInitParameter("fork", "false");
        jsp.setLoadOnStartup(3);
        context.addServletMappingDecoded("*.jsp", "jsp");
        Tomcat.addServlet(context, "userServlet", new UserServlet(inMemoryUserRepository));
        Tomcat.addServlet(context, "UserUpdateServlet", new UserUpdateServlet(inMemoryUserRepository));
        Tomcat.addServlet(context, "UserDeleteServlet", new UserDeleteServlet(inMemoryUserRepository));
        Tomcat.addServlet(context, "UserCreateServlet", new UserCreateServlet(inMemoryUserRepository));
        Tomcat.addServlet(context, "NewUserServlet", new NewUserServlet());
        Tomcat.addServlet(context, "UserEditServlet", new UserEditServlet(inMemoryUserRepository));
        context.addServletMappingDecoded("/users", "userServlet");
        context.addServletMappingDecoded("/users/new", "NewUserServlet");
        context.addServletMappingDecoded("/users/create", "UserCreateServlet");
        context.addServletMappingDecoded("/users/update", "UserUpdateServlet");
        context.addServletMappingDecoded("/users/edit", "UserEditServlet");
        context.addServletMappingDecoded("/users/delete", "UserDeleteServlet");

        tomcat.start();
        logger.info("Tomcat started on port {}", tomcat.getConnector().getLocalPort());
        tomcat.getServer().await();
    }
}