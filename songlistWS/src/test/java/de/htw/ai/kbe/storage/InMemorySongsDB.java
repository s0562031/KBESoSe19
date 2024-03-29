package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Userlist;

public class InMemorySongsDB implements ISongsDAO {

    
    private Map<Integer,Songs> songStorage;
    private Map<Integer,Userlist> userStorage;
    
    public InMemorySongsDB () {
    	songStorage = new ConcurrentHashMap<Integer,Songs>();
    	userStorage = new ConcurrentHashMap<Integer,Userlist>();
        initSomeSongs();
        initSomeUsers();
    }
    
        
    public Collection<Userlist> getAllUsers() {
    	return userStorage.values();
    }
    
    private void initSomeUsers() {
    	
    	Userlist myfirstuser = new Userlist.Builder("mmuster","321drowssap")
    			.firstname("Maxime")
    			.lastname("Muster")
    			.token("MzIxZHJvd3NzYXA=")
    			.build();
    	Userlist myseconduser = new Userlist.Builder("eschueler","321drowssap")
    			.firstname("Elena")
    			.lastname("Schuler")
    			.token("MzIxZHJvd3NzYXA=").build();
    	
    	userStorage.put(1,myfirstuser);
    	userStorage.put(2,myseconduser);
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
        
        songStorage.put(1, song_one); 
        songStorage.put(2, song_two); 
        songStorage.put(3, song_three); 
        songStorage.put(4, song_four); 
        songStorage.put(5, song_five); 
        songStorage.put(6, song_six); 
        songStorage.put(7, song_seven); 
        songStorage.put(8, song_eight); 
        songStorage.put(9, song_nine); 
        songStorage.put(10, song_ten); 
        
        songStorage.put(11, myfirstsong);      
    }

	@Override
	public Songs getSong(Integer id) {
		return songStorage.get(id);
	}
	
    @Override
    public List<Songs> getAllSongs() {
    	return (List<Songs>) songStorage.values();
    }


	@Override
	public Integer addSong(Songs newsong) {
		newsong.setId((int) songStorage.keySet().stream().count() +1);
		songStorage.put(newsong.getId(), newsong);
		return newsong.getId();
	}

	@Override
	public boolean updateSong(Songs updatesong, Integer id) {
		
		if(songStorage.get(id) != null) {
			
			Songs songToUpdate = songStorage.get(id);
			songToUpdate.setArtist(updatesong.getArtist());
			songToUpdate.setAlbum(updatesong.getAlbum());
			songToUpdate.setRelease(updatesong.getRelease());
			songToUpdate.setTitle(updatesong.getTitle());			
			return true;
		}
		else return false;
	}

	@Override
	public boolean deleteSong(int id) {
		Songs del = songStorage.remove(id);
		if(del == null) return false;
		else return true;
	}


    
}