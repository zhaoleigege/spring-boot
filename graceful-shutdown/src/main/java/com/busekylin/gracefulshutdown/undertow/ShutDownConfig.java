package com.busekylin.gracefulshutdown.undertow;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

import static io.undertow.Undertow.*;

@Configuration
@Slf4j
public class ShutDownConfig {

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
