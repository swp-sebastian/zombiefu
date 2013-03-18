#  The Final Exam - Die Anwesenheitspflicht schlägt zurück #

## Story ##

Die Anwesenheitspflicht wurde wieder eingeführt.  Notgedrungen quälst
du dich eines frühen Morgens nach mehreren Wochen Abwesenheit wieder
in die Uni.  Dort stellst du mit Erschrecken fest, dass aus
irgendeinem Grund eine Epidemie ausgebrochen ist.  Viele deiner
Kommilitonen sind bereits infiziert und zu Zombies mutiert. Doch nicht
nur Studierende hat es erwischt, sowohl Mitarbeiter als auch Dozenten
wandeln als lebende Tote durch die Universität.  Aufgrund der
Anwesenheitspflicht bleibt dir gar nichts anderes übrig, als dich
selbst mit dem Problem zu befassen und du sagst den Zombies,
ehrfürchtige Erstis oder respekt-einflößende Dozenten, den Kampf an.

## Starten ##

Voraussetzung: Java 7.

`ant run`

## Anleitung ##

Um zwischen den Maps Campus oder Testraum zu wechseln muss in der Datei config.cfg
der Eintrag `player.start.map` auskommentiert werden oder nicht.

Steuerung (lässt sich in der config.cfg ändern):
+ `wasd` zum Bewegen
+ `.` tut gar nichts
+ `q` und `e` wechseln die Waffe
+ `i` öffnet das Inventar
+ `g` GodMode (nur fürs Debugging)
+ `f` Schatten/Nicht Schatten (nur fürs Debugging)
+ `enter` Angriff
+ `esc` Beenden

Charaktere (nicht alle kann man in den Welten finden, sind aber implementiert):
+ Monster/Zombies laufen auf einen zu und attackieren
+ Menschen, dir nur reden (zur Zeit ohne Bewegung)
+ Menschen, die Items verkaufen/kaufen/tauschen/verschenken (zur Zeit ohne Bewegung)
+ Mensa-Automaten, Mensa-Shops (zur Zeit ohne Bewegung)

Eine Aktion (Angriff,Gespräch,Handel) wird ausgeführt wenn man auf den
Charakter zuläuft.  Durch Enter kann man angreifen, nachdem eine
Richtung angegeben wurde.  Greift man einen
Menschen/Mensa-Shop/Mensa-Automaten (absichtlicher oder UNABSICHTLICH)
an, wird man exmatrikuliert.

Items lassen sich im Ordner /res/ auslesen und unter
Umständen konfigurieren Sie werden automatisch beim Darüberlaufen
eingesammelt.
+ HealingItems (im Inventar)
+ Schlüssel (im Invantar) (werden für das Öffnen von Türen benötigt)
+ Waffen (Anzeige links unten im Bildschirm)
