# SongsServlet

Song Webservice, der GET und POST Anfragen erlaubt 

## Beschreibung

Dieser Webservice liest zunächst eine XML Datei mit entsprechenden Songs ein. Die Songs können per GET request abgefragt werden. Per POST request können neue Songs hinzugefügt werden. 

### Vorraussetzungen

Tomcat7 Server  
Maven  
Java 11  
songs.xml

### Installation

Die songs.xml Datei wird auf dem lokalen PC abgelegt. Pfad muss in der web.xml angepasst werden. 
Default-Pfad ist /var/tmp/songs.xml

```
<param-value>/var/tmp/songs.xml</param-value>
```

## Deployment

Der deploy wird folgendermaßen ausgeführt

```
mvn clean install tomcat7:deploy

```
Der Webservice kann nun aufegrufen werden

```
http://127.0.0.1:8080/songsServlet/
```

## Anfragen senden

Get Anfragen

Gibt alle Songs zurück

```
http://127.0.0.1:8080/songsServlet/
```

Gibt einen bestimmten Song zurück

```
http://127.0.0.1:8080/songsServlet/?songId=2
```

Post Anfragen

Post Anfrage an 

```
http://127.0.0.1:8080/songsServlet/
```

mit Body

```
{"title" : "a new title","artist" : "YOU","album" : "First Album","released" : 2017}
```

Accept Header

```
application/json
```

## Authors

Dustin ...  
Katharina Kozlowska - s0553332
