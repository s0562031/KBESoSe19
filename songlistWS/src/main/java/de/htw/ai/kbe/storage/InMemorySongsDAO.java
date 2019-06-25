package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Userlist;

public class InMemorySongsDAO implements ISongsDAO {

    private static InMemorySongsDAO instance = null;
    
    
    private Map<Integer,Songs> songStorage;
    private Map<Integer,Userlist> userStorage;
    private Map<Integer, SongList> songlistStorage;
    
    private InMemorySongsDAO () {
    	songStorage = new ConcurrentHashMap<Integer,Songs>();
    	userStorage = new ConcurrentHashMap<Integer,Userlist>();
    	songlistStorage = new ConcurrentHashMap<Integer,SongList>();
        initSomeSongs();
        initSomeUsers();
        initSomeSongLists();
    }
    
    public static InMemorySongsDAO getInstance() {
        if (instance == null) {
            instance = new InMemorySongsDAO();
        }
        return instance;
    }
        
    public Collection<Userlist> getAllUsers() {
    	return userStorage.values();
    }
    
    private void initSomeUsers() {
    	
    	Userlist myfirstuser = new Userlist.Builder("mmuster","password123")
    			.firstname("Maxime")
    			.lastname("Muster")
    			.token("")
    			.build();
    	
    	Userlist myseconduser = new Userlist.Builder("eschueler","password123")
    			.firstname("Elena")
    			.lastname("Schuler")
    			.token("").build();
    	
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
    
    public void initSomeSongLists() {
    	
    	List<Songs> myfirstcontent = new LinkedList<Songs>();
    	myfirstcontent.add(songStorage.get(1));
    	myfirstcontent.add(songStorage.get(2));
    	
    	SongList myfirstsonglist = new SongList.Builder(userStorage.get(1), true, myfirstcontent).build();
    	songlistStorage.put(1, myfirstsonglist);
    	
    	
    	List<Songs> mysecondcontent = new LinkedList<Songs>();
    	mysecondcontent.add(songStorage.get(3));
    	mysecondcontent.add(songStorage.get(4));
    	mysecondcontent.add(songStorage.get(6));
    	
    	SongList mysecondsonglist = new SongList.Builder(userStorage.get(2), false, mysecondcontent).build();
    	songlistStorage.put(2, mysecondsonglist);
    	
    	
    	List<Songs> mythirdontent = new LinkedList<Songs>();
    	mythirdontent.add(songStorage.get(6));
    	mythirdontent.add(songStorage.get(7));
    	mythirdontent.add(songStorage.get(8));
    	
    	SongList mythirdsonglist = new SongList.Builder(userStorage.get(1), false, mythirdontent).build();
    	songlistStorage.put(3, mythirdsonglist);
    	
    	List<Songs> myfourthcontent = new LinkedList<Songs>();
    	myfourthcontent.add(songStorage.get(3));
    	myfourthcontent.add(songStorage.get(4));
    	myfourthcontent.add(songStorage.get(6));
    	
    	SongList myfourthsonglist = new SongList.Builder(userStorage.get(2), true, myfourthcontent).name("This even got a name").build();
    	songlistStorage.put(4, myfourthsonglist);
    }

	@Override
	public Songs getSong(Integer id) {
		return songStorage.get(id);
	}
	
    @Override
    public List<Songs> getAllSongs() {
    	return (List<Songs>) songStorage.values();
    }
    
    public Collection<Songs> getAllSongsAsCollection() {
    	return songStorage.values();
    }
    
    public List<SongList> getAllSongLists() {
    	return (List<SongList>) songlistStorage.values();
    }
    
    public Collection<SongList> getAllSongListsAsCollection() {
    	return songlistStorage.values();
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
			songToUpdate.setAlbum(updatesong.getAlbum());
			songToUpdate.setRelease(updatesong.getRelease());
			songToUpdate.setTitle(updatesong.getTitle());			
			return true;
		}
		else return false;
	}

	@Override
	public boolean deleteSong(int id) {
		songStorage.remove(id);
		if(songStorage.get(id) == null) return true;
		else return false;
	}


    
}