package de.htw.ai.kbe.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/rest")
public class Application extends ResourceConfig {
    public Application() {
        register(new DependencyBinder());
        packages(true,"de.htw.ai.kbe.api");
    }
}
