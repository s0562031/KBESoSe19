package de.htw.ai.kbe.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "songlists")
public class SongListWrapper {
	
	@XmlElement(name = "songlist")
	private List<SongList> songlist;
	

    public SongListWrapper() {
    	songlist = new ArrayList<SongList>();
    }
 
    public SongListWrapper(List<SongList> songlist) {
        this.songlist = songlist;
    }
 
   //@XmlAnyElement(lax=true)
    public List<SongList> getSongs() {
        return songlist;
    }

}


