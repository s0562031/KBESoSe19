package de.htw.ai.kbe.config;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htw.ai.kbe.services.TokenHandler;
import de.htw.ai.kbe.storage.ISongsDAO;
import de.htw.ai.kbe.storage.IUsersDAO;
import de.htw.ai.kbe.storage.SongsDBDAO;
import de.htw.ai.kbe.storage.UsersDBDAO;

public class DependencyBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(Persistence.createEntityManagerFactory("songDB-PU")).to(EntityManagerFactory.class);
        bind(SongsDBDAO.class).to(ISongsDAO.class).in(Singleton.class);
        bind(UsersDBDAO.class).to(IUsersDAO.class).in(Singleton.class);
        bind(TokenHandler.class).to(TokenHandler.class).in(Singleton.class);
    }
}
