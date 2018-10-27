package org.deb.SFM;

import org.deb.SFM.watcher.directory.FolderWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableIntegration
@RefreshScope
@ComponentScan
@EnableScheduling
public class SFMApplication {
    @Autowired
    FolderWatcher folderWatcher;

    public static void main(String[] args) {
        SpringApplication.run(SFMApplication.class, args);
    }

}
