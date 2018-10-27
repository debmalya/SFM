package org.deb.SFM;

import org.deb.SFM.watcher.directory.FolderWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableIntegration
//@RefreshScope
@ComponentScan
@EnableScheduling
public class SFMApplication {
    @Autowired
    FolderWatcher folderWatcher;

    public static void main(String[] args) {
        SpringApplication.run(SFMApplication.class, args);
    }

}
