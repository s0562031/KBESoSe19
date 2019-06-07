package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.List;

import de.htw.ai.kbe.data.Songs;

public interface ISongsDAO {
	
	public Songs getSong(Integer id);
	public List<Songs> getAllSongs();
	public Integer addSong(Songs newsong);
	public boolean updateSong(Songs updatesong);
	public Songs deleteSong(Integer id);

}
