package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.data.Songs;

public class InMemorySongsDB implements ISongsDAO {

    private static InMemorySongsDB instance = null;
    
    private Map<Integer,Songs> storage;
    
    private InMemorySongsDB () {
        storage = new ConcurrentHashMap<Integer,Songs>();
        initSomeSongs();
    }
    
    public static InMemorySongsDB getInstance() {
        if (instance == null) {
            instance = new InMemorySongsDB();
        }
        return instance;
    }
    
    private void initSomeSongs() {
              
    	// TODO
        Songs myfirstsong = new Songs.Builder("Bittersweet Symphony")
                .artist("The Verve")
                .album("Urban Hymnes")
                .release(1997).build();
        
        Songs song_ten = new Songs.Builder("7 Years")
                .artist("Lukas Graham")
                .album("Lukas Graham (Blue Album)")
                .release(2015).build();
        
        Songs song_nine = new Songs.Builder("Private Show")
                .artist("Brithney Spears")
                .album("Glory")
                .release(2016).build();
        
        Songs song_eight = new Songs.Builder("No")
                .artist("Meghan Trainor")
                .album("Thank You")
                .release(2016).build();
        
        Songs song_seven = new Songs.Builder("i hate u, i love u")
                .artist("Gnash")
                .album("Top Hits 2017")
                .release(2017).build();
        
        Songs song_six = new Songs.Builder("I Took a Pill in Ibiza")
                .artist("Mike Posner")
                .album("At Night, Alone.")
                .release(2016).build();
        
        Songs song_five = new Songs.Builder("Bad Things")
                .artist("Camila Cabello, Maschine Gun Kelly")
                .album("Bloom")
                .release(2017).build();
        
        Songs song_four = new Songs.Builder("Ghostbusters (I'm not a fraid")
                .artist("Fall Out Boy, Missy Elliott")
                .album("Ghostbusters")
                .release(2016).build();
        
        Songs song_three = new Songs.Builder("Team")
                .artist("Iggy Azalea")
                .release(2016).build();
        
        Songs song_two = new Songs.Builder("Mom")
                .artist("Meghan Trainor, Kelli Trainor")
                .album("Thank You")
                .release(2016).build();
        
        Songs song_one = new Songs.Builder("Can't Stop the Feeling")
                .artist("Justin Timberlake")
                .album("Trolls")
                .release(2016).build();
        
        storage.put(1, song_one); 
        storage.put(2, song_two); 
        storage.put(3, song_three); 
        storage.put(4, song_four); 
        storage.put(5, song_five); 
        storage.put(6, song_six); 
        storage.put(7, song_seven); 
        storage.put(8, song_eight); 
        storage.put(9, song_nine); 
        storage.put(10, song_ten); 
        
        storage.put(11, myfirstsong);      
    }
    
    @Override
    public Songs getSong(Integer id) {
        return storage.get(id);
    }
    
    @Override
    public List<Songs> getAllSongs() {
        return (List<Songs>) storage.values();
    }
    
    @Override
    public Integer addSong(Songs song) {
//        song.setId((int)storage.keySet().stream().count() + 1);
//        storage.put(song.getId(), song);
//        return song.getId();
    	return null;
    }
    
    // updates a contact in the db
    @Override
    public boolean updateSong(Songs contact) {
        throw new UnsupportedOperationException("updateContact: not yet implemented");
    }
    
    // returns deleted contact
    @Override
    public Songs deleteSong(Integer id) {
        throw new UnsupportedOperationException("deleteContact: not yet implemented");
    }
}