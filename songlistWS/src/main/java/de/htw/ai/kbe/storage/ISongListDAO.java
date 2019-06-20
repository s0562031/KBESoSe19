package de.htw.ai.kbe.storage;

import java.util.List;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Userlist;

public interface ISongListDAO {
	
	public List<SongList> getAllOwnedSongLists(String userid);
	public List<SongList> getAllForeignSongLists(String userid);
	public String getUserFromToken(String token);
	public SongList getForeignSongLists(String id, Integer songlistid);
	public SongList getOwnedSongLists(String id, Integer songlistid);

}
