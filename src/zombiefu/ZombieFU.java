package zombiefu;

import zombiefu.util.ZombieGame;

public class ZombieFU {

    public static void main(String[] args) {

        ZombieGame.createGame(args, "The Final Exam - Die Anwesenheitspflicht schlägt zurück");
        ZombieGame.showStaticImage("startscreen", true);
        ZombieGame.showStaticImage("story", true);
        ZombieGame.initialize();

        ZombieGame.startGame();

    }
}
