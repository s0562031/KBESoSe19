package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Userlist;

public class SongListDBDAO implements ISongListDAO{
	
    private EntityManagerFactory factory;

    @Inject
    public SongListDBDAO(EntityManagerFactory emf) {
        this.factory = emf;
    }

	public List<SongList> getAllOwnedSongLists() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SongList> getAllForeignSongLists() {
		// TODO Auto-generated method stub
		return null;
	}

	public Userlist getUserFromToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
