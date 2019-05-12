package de.htw.ai.kbe.songsServlet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;

public class SongList {
	
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
