package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";

	private String user = "student";
	private String pass = "student";

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, title, description, release_year, language_id,"
					+ " rental_duration, rental_rate, length, replacement_cost," + " rating, special_features "
					+ "FROM film WHERE id = ?";
			PreparedStatement s = conn.prepareStatement(sql);
			s.setInt(1, filmId);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				film = new Film();
				film.setId(rs.getInt("id"));
				film.setTitle(rs.getString("title"));
				film.setDescription(rs.getString("description"));
				film.setRelease_year(rs.getInt("release_year"));
				film.setLanguage_id(rs.getInt("language_id"));
				film.setRental_duration(rs.getInt("rental_duration"));
				film.setRental_rate(rs.getDouble("rental_rate"));
				film.setReplacement_cost(rs.getDouble("replacement_cost"));
				film.setRating(rs.getString("rating"));
				film.setSpecial_features(rs.getString("special_features"));

				film.setActors(findActorsByFilmId(film.getId()));
				film.setLanguage(findLanguage(film.getLanguage_id()));

			}
			rs.close();
			s.close();
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id,first_name,last_name FROM actor WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, actorId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				actor = new Actor();
				actor.setId(rs.getInt("id"));
				actor.setFirst_name(rs.getString("first_name"));
				actor.setLast_name(rs.getString("last_name"));

			}
			rs.close();
			ps.close();
			conn.close();
		}

		catch (SQLException sqle) {

			sqle.printStackTrace();
		}

		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name " + " from actor a JOIN film_actor fa ON a.id = fa.actor_id "
					+ " WHERE fa.film_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, filmId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Actor actor = new Actor();
				actor.setId(rs.getInt("id"));
				actor.setFirst_name(rs.getString("first_name"));
				actor.setLast_name(rs.getString("last_name"));
				actors.add(actor);
			}
			rs.close();
			ps.close();
			conn.close();
		}

		catch (SQLException sqle) {

			sqle.printStackTrace();
		}

		// select id, first_name, last_name, from actor join film_actor on
		// actor.actor_id where film_actor.film_id = ?;

		return actors;

	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT * from film WHERE title LIKE ? OR description like ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				film.setId(rs.getInt("id"));
				film.setTitle(rs.getString("title"));
				film.setDescription(rs.getString("description"));
				film.setRelease_year(rs.getInt("release_year"));
				film.setLanguage_id(rs.getInt("language_id"));
				film.setRental_duration(rs.getInt("rental_duration"));
				film.setRental_rate(rs.getDouble("rental_rate"));
				film.setReplacement_cost(rs.getDouble("replacement_cost"));
				film.setRating(rs.getString("rating"));
				film.setSpecial_features(rs.getString("special_features"));
				film.setActors(findActorsByFilmId(film.getId()));
				film.setLanguage(findLanguage(film.getLanguage_id()));
				films.add(film);
			}
			rs.close();
			ps.close();
			conn.close();
		}

		catch (SQLException sqle) {

			sqle.printStackTrace();
		}
		return films;
	}

	public String findLanguage(int language_id) {
		String language = "";
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT l.name FROM language l JOIN film f ON l.id =  ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, language_id);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				language = rs.getString("l.name");

			}
			rs.close();
			ps.close();
			conn.close();
		}

		catch (SQLException sqle) {

			sqle.printStackTrace();
		}

		return language;
	}

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Driver not found.");
			throw new RuntimeException("Unable to load MySQL driver class");

		}
	}

}
