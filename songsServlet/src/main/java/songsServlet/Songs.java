package songsServlet;

public class Songs {
	
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
	
}
