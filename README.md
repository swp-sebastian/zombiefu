The Final Exam - Die Anwesenheitspflicht schl�gt zur�ck
(Softwarepraktikum 2013 geleitet von Till Zoppke)

von Tomas Bayer, Adrian Henning, Christoph Jaecks, Inga R�hl, Sebastian Sontberg, Max Willert

Story:
Die Anwesenheitspflicht wurde wieder eingef�hrt.
Notgedrungen qu�lst du dich eines fr�hen Morgens
nach mehreren Wochen Abwesenheit wieder in die Uni.
Dort stellst du mit Erschrecken fest, dass aus
irgendeinem Grund eine Epidemie ausgebrochen ist.
Viele deiner Kommilitonen sind bereits infiziert
und zu Zombies mutiert. Doch nicht nur Studierende
hat es erwischt, sowohl Mitarbeiter als auch Dozenten
wandeln als lebende Tote durch die Universit�t.
Aufgrund der Anwesenheitspflicht bleibt dir gar nichts
anderes �brig, als dich selbst mit dem Problem zu
befassen und du sagst den Zombies, ehrf�rchtige Erstis
oder respekt-einfl��ende Dozenten, den Kampf an.



Startklasse: ZombieFu.java

Um zwischen den Maps Campus oder Testraum zu wechseln muss in der startinfo.txt-Datei
die 2. Zeile auskommentiert werden oder nicht.

Steuerung (l�sst sich in der config.cfg �ndern):
    WASD zum Bewegen
    '.' tut gar nichts
    'Q' und 'E' wechseln die Waffe
    'I' �ffnet das Inventar
    'G' GodMode (nur f�rs Debugging)
    'F' Schatten/Nicht Schatten (nur f�rs Debugging)
    Enter Angriff
    Esc Beenden

Charaktere (nicht alle kann man in den Welten finden, sind aber implementiert):
	Monster/Zombies laufen auf einen zu und attackieren
	Menschen, dir nur reden (zur Zeit ohne Bewegung)
	Menschen, die Items verkaufen/kaufen/tauschen/verschenken (zur Zeit ohne Bewegung)
	Mensa-Automaten, Mensa-Shops (zur Zeit ohne Bewegung)

Eine Aktion (Angriff,Gespr�ch,Handel) wird ausgef�hrt wenn man auf den Charakter zul�uft.
Durch Enter kann man angreifen, nachdem eine Richtung angegeben wurde.
Greift man einen Menschen/Mensa-Shop/Mensa-Automaten (absichtlicher oder UNABSICHTLICH) an, wird man exmatrikuliert.

Items lassen sich im Ordner sources/items auslesen und unter Umst�nden konfigurieren
Sie werden automatisch beim Dar�berlaufen eingesammelt.
	HealingItems (im Inventar)
	KeyCards (im Invantar) (werden f�r das �ffnen von T�ren ben�tigt)
	Waffen (Anzeige links unten im Bildschirm)