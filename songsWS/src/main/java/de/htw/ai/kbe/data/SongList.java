package de.htw.ai.kbe.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "songs")
public class SongList {
	
	@XmlElement(name = "song")
	private List<Songs> songs;
	

    public SongList() {
        songs = new ArrayList<Songs>();
    }
 
    public SongList(List<Songs> songs) {
        this.songs = songs;
    }
 
    @XmlAnyElement(lax=true)
    public List<Songs> getSongs() {
        return songs;
    }

}
