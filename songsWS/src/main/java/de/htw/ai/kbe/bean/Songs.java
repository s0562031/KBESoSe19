package de.htw.ai.kbe.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "songs")
@Table(name="songs")
public class Songs {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
	private String title;
	private String artist;
	private String album;
	private int release;
	
	public Songs(String title, String artist, String album, int release) {
		super();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.release = release;
	}
	
	public Songs(Builder build) {
		//this.id = build.id;
		this.title = build.title;
		this.artist = build.artist;
		this.album = build.album;
		this.release = build.release;
	}

	// needed for JAXB
	public Songs() {
	}

//	public Integer getId() {
//		return id;
//	}

//	public void setId(Integer id) {
//		this.id = id;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public int getRelease() {
		return release;
	}

	public void setRelease(int release) {
		this.release = release;
	}

	// Example of a builder:
	public static class Builder {
		// required parameter
		//private Integer id;
		private String title;
		// optional 
		private String artist;
		private String album;
		private int release;

		public Builder(String title) {
			//this.id = id;
			this.title = title;
		}
		
		public Builder title(String val) {
			title=val;
			return this;
		}
		
		public Builder artist(String val) {
			artist = val;
			return this;
		}
		
		public Builder album(String val) {
			album = val;
			return this;
		}
		
		public Builder release(Integer val) {
			release = val;
			return this;
		}
		
		public Songs build() {
			return new Songs(this);
		}

	}

}

