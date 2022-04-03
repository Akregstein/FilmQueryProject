package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		// app.test();
		app.launch();

	}

	private void test() {
		Film film = db.findFilmById(1);
		System.out.println(film);
		Actor actor = db.findActorById(1);
		System.out.println(actor);
		List<Actor> actors = db.findActorsByFilmId(1);
		System.out.println(actors);
	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		boolean menu = true;

		System.out.println("      Welcome to Kazahkstans greatest moviefilm directory APP");
		System.out.println();
		System.out.println("      Please Make a selection from the following Menu");
		System.out.println();
		System.out.println("            Very niiiice");
		while (menu) {
			System.out.println();
			System.out.println("  #################################");
			System.out.println("     ########################      ");
			System.out.println("            ***MENU***     ");
			System.out.println("  1:  Look up a film by it's id           ");
			System.out.println("  2:  Look up a film by a search Keyword           ");
			System.out.println("  3:  Exit the application           ");
			int selection = input.nextInt();
			if (selection == 3) {
				System.out.println("Goodbye! see you at the disco dance \n I Like");
				menu = false;
			}
			if (selection == 1) {
				System.out.println("Please enter film Id");
				int filmId = input.nextInt();
				Film film = (db.findFilmById(filmId));
				if (film == null) {
					System.out.println("\n Film not found \n Borriiiing");
				} else {
					System.out.println(film);
				}
			}
			if (selection == 2) {
				System.out.println(" Please enter a keyword");
				String keyword = input.next();
				List<Film> kfilms = (db.findFilmByKeyword(keyword));
				if (kfilms.size() < 1) {
					System.out.println(
							"\n Sorry \n Keyword does not match any film \n Don't be jealous of my clock radio!");
				} else {
					System.out.println(kfilms);
				}
			}
		}
	}
}
