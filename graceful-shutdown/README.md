# spring boot优雅的停机

1. tomcat

   ```java
   @Configuration
   @Slf4j
   public class ShutdownConfig {
       @Bean
       public ServletWebServerFactory serverFactory() {
           TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
           tomcatFactory.addConnectorCustomizers(gracefulShutdown());
   
           return tomcatFactory;
       }
   
       /**
        * 用于接受shutdown事件
        * @return
        */
       @Bean
       public GracefulShutdown gracefulShutdown() {
           return new GracefulShutdown();
       }
   
       private static class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
   
           private volatile Connector connector;
           private static final int TIME_OUT = 30;
   
           /**
            * tomcat事件
            *
            * @param connector
            */
           @Override
           public void customize(Connector connector) {
               this.connector = connector;
           }
   
           /**
            * spring boot应用关闭事件
            *
            * @param contextClosedEvent
            */
           @Override
           public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
               this.connector.pause();
   
               log.info("Spring boot关闭");
   
               Executor executor = this.connector.getProtocolHandler().getExecutor();
               if (executor instanceof ThreadPoolExecutor) {
                   try {
                       ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                       threadPoolExecutor.shutdown();
   
                       if (!threadPoolExecutor.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                           log.warn("Tomcat线程池在" + TIME_OUT + "秒内无法结束，尝试强制结束");
                       }
                   } catch (InterruptedException ex) {
                       Thread.currentThread().interrupt();
                   }
               }
   
           }
       }
   }
   ```

2. Undertow

   ```java
   @Configuration
   @Slf4j
   public class Config {
   
       @Component
       @RequiredArgsConstructor
       public class GracefulShutdown implements ApplicationListener<ContextClosedEvent> {
   
           private final GracefulShutdownWrapper gracefulShutdownWrapper;
           private final ServletWebServerApplicationContext context;
   
           @Override
           public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
               log.info("spring boot关闭");
   
               gracefulShutdownWrapper.getGracefulShutdownHandler().shutdown();
               try {
                   UndertowServletWebServer webServer = (UndertowServletWebServer) context.getWebServer();
                   Field field = webServer.getClass().getDeclaredField("undertow");
                   field.setAccessible(true);
                   Undertow undertow = (Undertow) field.get(webServer);
                   List<ListenerInfo> listenerInfo = undertow.getListenerInfo();
                   ListenerInfo listener = listenerInfo.get(0);
                   ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
                   while (connectorStatistics.getActiveConnections() > 0) {
                   }
               } catch (Exception e) {
   
               }
           }
       }
   
       @Component
       public class GracefulShutdownWrapper implements HandlerWrapper {
   
           private GracefulShutdownHandler gracefulShutdownHandler;
   
           @Override
           public HttpHandler wrap(HttpHandler handler) {
               if (gracefulShutdownHandler == null) {
                   this.gracefulShutdownHandler = new GracefulShutdownHandler(handler);
               }
               return gracefulShutdownHandler;
           }
   
           public GracefulShutdownHandler getGracefulShutdownHandler() {
               return gracefulShutdownHandler;
           }
   
       }
   
       @Component
       @RequiredArgsConstructor
       public class UndertowExtraConfiguration {
           private final GracefulShutdownWrapper gracefulShutdownWrapper;
   
           @Bean
           public UndertowServletWebServerFactory servletWebServerFactory() {
               UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
               factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownWrapper));
               factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
               return factory;
           }
   
       }
   }
   ```

3. jetty

   ```java
   // 没找到
   ```
