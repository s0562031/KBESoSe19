package de.htw.ai.kbe.storage;

import javax.inject.Singleton;
import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import de.htw.ai.kbe.api.SongsWebService;

public class SongsWebServiceTest extends JerseyTest {
	
    @Override
    protected Application configure() {
        return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(InMemorySongsDB.class).to(ISongsDAO.class).in(Singleton.class);
            }
        });
    }	

}
