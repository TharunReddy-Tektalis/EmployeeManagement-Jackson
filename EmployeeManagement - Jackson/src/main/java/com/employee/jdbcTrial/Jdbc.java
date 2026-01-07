package com.employee.jdbcTrial;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Jdbc {
	public static void jdbcTrail() {
		Properties prop = new Properties();
		Scanner sc = new Scanner(System.in);
		try (InputStream input = new FileInputStream("src/main/resources/db-postgres.properties")) {
			prop.load(input);

			String url = prop.getProperty("db.url");
			String user = prop.getProperty("db.username");
			String password = prop.getProperty("db.password");

			Connection con = DriverManager.getConnection(url, user, password);
			con.setAutoCommit(false);
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String query = "select * from students;";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");

				System.out.println("id: " + id + ", name: " + name);
			}

			String preparedQuery = "select name from students where id = ?;";
			PreparedStatement pstmt = con.prepareStatement(preparedQuery);
			System.out.println("Enter id to view name");
			int id = sc.nextInt();

			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String name = rs.getString("name");
				System.out.println("name: " + name);
			}

			String updateQuery = "update students set name = ? where id = ?;";
			PreparedStatement updatePstmt = con.prepareStatement(updateQuery);
			System.out.println("Enter id to update: ");
			int uId = sc.nextInt();
			System.out.println("Enter name to update: ");
			String uName = sc.next();

			updatePstmt.setString(1, uName);
			updatePstmt.setInt(2, uId);

			int rows = updatePstmt.executeUpdate();
			System.out.println("rows: " + rows);

			rs = stmt.executeQuery(query);

			
			
			rs.close();
			stmt.close();
			con.close();
		} catch (IOException e) {
			System.out.println("error reading from property file");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
