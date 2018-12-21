package com.busekylin.gracefulshutdown.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     *
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
