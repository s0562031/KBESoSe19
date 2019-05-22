package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import de.htw.ai.kbe.bean.Songs;

public class InMemoryAddressBook {

    private static InMemoryAddressBook instance = null;
    
    private Map<Integer,Songs> storage;
    
    private InMemoryAddressBook () {
        storage = new ConcurrentHashMap<Integer,Songs>();
        initSomeSongs();
    }
    
    public static InMemoryAddressBook getInstance() {
        if (instance == null) {
            instance = new InMemoryAddressBook();
        }
        return instance;
    }
    
    private void initSomeSongs() {
              
    	// TODO
        Songs myfirstsong = new Songs.Builder(1,"Bittersweet Symphony")
                .artist("The Verve")
                .album("Urban Hymnes")
                .release(1997).build();
        
        storage.put(myfirstsong.getId(), myfirstsong);      
    }
    
    public Songs getSong(Integer id) {
        return storage.get(id);
    }
    
    public Collection<Songs> getAllSongs() {
        return storage.values();
    }
    
    public Integer addSong(Songs song) {
        song.setId((int)storage.keySet().stream().count() + 1);
        storage.put(song.getId(), song);
        return song.getId();
    }
    
    // updates a contact in the db
    public boolean updateSong(Songs contact) {
        throw new UnsupportedOperationException("updateContact: not yet implemented");
    }
    
    // returns deleted contact
    public Songs deleteSong(Integer id) {
        throw new UnsupportedOperationException("deleteContact: not yet implemented");
    }
}