package de.htw.ai.kbe.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "songs")
public class SongWrapper {
	
	@XmlElement(name = "song")
	private List<Songs> songs;
	

    public SongWrapper() {
        songs = new ArrayList<Songs>();
    }
 
    public SongWrapper(List<Songs> songs) {
        this.songs = songs;
    }
 
   //@XmlAnyElement(lax=true)
    public List<Songs> getSongs() {
        return songs;
    }

}
