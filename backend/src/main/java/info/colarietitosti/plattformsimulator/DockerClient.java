package info.colarietitosti.plattformsimulator;

import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.LocalDocker;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Supplier;

@Log
@Component
public class DockerClient {

    @PostConstruct
    public void init(){
        final Docker docker = new LocalDocker(
                new File("/var/run/docker.sock")
        );
        boolean available = false;
        try {
            available = docker.ping();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(String.valueOf(available));

        final Containers containers = docker.containers();

        for(final Container ctn : containers) {
            System.out.println(ctn.toString());
            try {
                JsonObject inspct = ctn.inspect();
                log.info(inspct.getString("Id"));
                log.info(inspct.getString("Name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\n");
        }
    }
}
