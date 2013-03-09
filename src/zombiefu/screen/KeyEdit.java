package zombiefu.screen;

import jade.core.World;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import zombiefu.creature.Player;

public class KeyEdit {

	char up;
	char down;
	char left;
	char right;
	char hit;
	char change;
	
	public static void getKeys(TiledTermPanel term) throws InterruptedException{
		char up;
		char down;
		char left;
		char right;
		char hit;
		char change;
		
		System.out.println("Taste hoch");
		up = term.getKey();
		Direction.up=up;
		System.out.println("Taste runter");
		down = term.getKey();
		Direction.down=down;
		System.out.println("Taste links");
		left = term.getKey();
		Direction.left=left;
		System.out.println("Taste rechts");
		right = term.getKey();
		Direction.right=right;
		System.out.println("Aktionstaste");
		hit = term.getKey();
		Player.hit=hit;
		System.out.println("Item wechseln");
		change = term.getKey();
		Player.change=change;
		
	}
}
