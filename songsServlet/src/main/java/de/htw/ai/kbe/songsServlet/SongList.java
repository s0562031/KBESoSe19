package de.htw.ai.kbe.songsServlet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "songs")
public class SongList {
	
	@JacksonXmlProperty(localName = "song")
    @JacksonXmlElementWrapper(useWrapping = false)
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
