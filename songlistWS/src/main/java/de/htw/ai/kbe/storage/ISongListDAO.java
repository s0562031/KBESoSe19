package de.htw.ai.kbe.storage;

import java.util.List;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Userlist;

public interface ISongListDAO {
	
	public List<SongList> getAllOwnedSongLists();
	public List<SongList> getAllForeignSongLists();
	public Userlist getUserFromToken(String token);

}
