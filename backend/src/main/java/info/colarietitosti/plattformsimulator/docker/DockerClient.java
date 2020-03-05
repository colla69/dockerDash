package info.colarietitosti.plattformsimulator.docker;

import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.LocalDocker;
import com.amihaiemil.docker.Networks;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.json.JsonObject;

@Slf4j
@Component
public class DockerClient {

    @Getter
    private Containers containerList;
    private Networks network;

    private Docker docker = null;

    public void createContainer(String name){
        try {
            //JsonObject details = new JsonObject();
            final Container created = docker.containers().create(name);
            log.info(created.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// ----------- INTIALIZATION --------------- //

    @PostConstruct
    public void init(){
        this.docker = new LocalDocker(
                new File("/var/run/docker.sock")
        );
        boolean available = false;
        try {
            available = docker.ping();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Docker instance available {}",String.valueOf(available));
        //loadNetwork();
        loadContainers();


        createContainer("docker.elastic.co/elasticsearch/elasticsearch-oss:6.2.3");
    }

    private void loadNetwork() {
        // throw new UnsupportedOperationException("Networks API is not yet implemented. If you can contribute please, do it here: https://www.github.com/amihaiemil/docker-java-api");
        this.network = docker.networks();
        this.network.forEach(n -> log.info(n.getString("Name")));
    }

    private void loadContainers() {
        this.containerList = docker.containers();
        containerList.forEach(ctn ->  {
            try{
                JsonObject inspct = ctn.inspect();
                log.debug("loaded container : {}", inspct.getString("Name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
