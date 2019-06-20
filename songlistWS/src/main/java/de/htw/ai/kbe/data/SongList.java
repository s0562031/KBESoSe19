package de.htw.ai.kbe.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import de.htw.ai.kbe.data.Songs.Builder;

@Entity
@XmlRootElement(name = "songlist")
@Table(name="songlist")
public class SongList {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    
    @ManyToOne
    @JoinColumn(name="owner")
	private Userlist owner;
    
    
	private Boolean isprivate;
	private String name;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(
			name="songlist_songs",
			joinColumns = {@JoinColumn(name="songlist_id", referencedColumnName ="id")},
			inverseJoinColumns = {@JoinColumn(name="songs_id", referencedColumnName = "id")}
	)
	private List<Songs> songlist;
	
	public SongList(Builder build) {
		this.owner = build.owner;
		this.isprivate = build.isprivate;
		this.name = build.name;
		this.songlist = build.songlist;
	}
	
	public SongList() {
		
	}
	
	public int getId() {
		return id;
	}	
	public void ListId(int id) {
		this.id = id;
	}
	public Userlist getOwner() {
		return owner;
	}
	public void ListOwner(Userlist owner) {
		this.owner = owner;
	}
	public Boolean getIsprivate() {
		return isprivate;
	}
	public void ListIsprivate(Boolean isprivate) {
		this.isprivate = isprivate;
	}
	public String getName() {
		return name;
	}
	public void ListName(String name) {
		this.name = name;
	}
	
	public List<Songs> getSongList() {
		return songlist;
	}
	
	public void ListSongList(List<Songs> songlist) {
		this.songlist = songlist;
	}
	
	

	public static class Builder {

		private String name;
		private Userlist owner;
		private Boolean isprivate;
		private List<Songs> songlist;

		public Builder(Userlist owner, Boolean isprivate, List<Songs> songlist) {
			this.owner = owner;
			this.isprivate = isprivate;
			this.songlist = songlist;
		}
		
		public Builder name(String val) {
			name=val;
			return this;
		}
		
		public Builder owner(Userlist val) {
			owner = val;
			return this;
		}
		
		public Builder isprivate(Boolean val) {
			isprivate = val;
			return this;
		}
		
		public Builder songlist(List<Songs> val) {
			songlist=val;
			return this;
		}
		
		
		public SongList build() {
			return new SongList(this);
		}

	}
	

}
