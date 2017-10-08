package strilets;

import java.sql.*;
import java.util.Scanner;

public class Book {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("There are not MySQL JDBC Driver!");
			e.printStackTrace();
			return;
		}

		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "08642");
		} catch (SQLException e) {
			System.out.println("Connection failed!");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("Connection successful!");
		} else {
			System.out.println("Connection failed!");
		}

		System.out.println("Welcome to library!");

		Statement statement = null;
		statement = connection.createStatement();

		Scanner sc = new Scanner(System.in);
		boolean exit = false;

		while (!exit) {
			String command;
			System.out.print("U: ");
			command = sc.nextLine();

			String operation;

			int i = command.indexOf(32);
			if (i == -1)
				operation = command;
			else
				operation = command.substring(0, i);

			if ("add".equals(operation))
				addBook(statement, command);

			else if ("all".equals(operation))
				allBooks(statement);

			else if ("edit".equals(operation))
				editBook(statement, command);

			else if ("remove".equals(operation))
				removeBook(statement, command);

			else if ("exit".equals(operation))
				exit = true;

			else
				System.out.println("P: wrong command");

		}

		if (statement != null) {
			statement.close();
		}

		if (connection != null) {
			connection.close();
		}

		System.out.println("Bye!");
	}

	public static void addBook(Statement statement, String command) throws SQLException {

		int index = command.indexOf("\"");

		if (index == -1) {
			System.out.println("P: wrong command");
			return;
		}

		String author = command.substring(4, index - 1);
		String name = command.substring(index + 1, command.length() - 1);

		String sql = "INSERT INTO book (name, author)" + " VALUES " + "('" + name + "','" + author + "')";

		statement.executeUpdate(sql);
		System.out.println("P: book " + author + " \"" + name + "\" was added ");
	}

	public static void allBooks(Statement statement) throws SQLException {

		String sql = "SELECT * FROM BOOK ORDER BY name;";
		ResultSet resultSet = statement.executeQuery(sql);

		System.out.println("P: Our books : ");

		while (resultSet.next()) {
			String author = resultSet.getString("author");
			String name = resultSet.getString("name");
			System.out.println("\t" + author + " \"" + name + "\"");
		}

		if (resultSet != null) {
			resultSet.close();
		}
	}

	public static void editBook(Statement statement, String command) throws SQLException {

		int index = command.indexOf(32);

		if (index == -1) {
			System.out.println("P: wrong command");
			return;
		}

		String oldName = command.substring(index + 1, command.length());

		String sql = "SELECT * FROM BOOK WHERE name = '" + oldName + "' ORDER BY name;";
		ResultSet resultSet = statement.executeQuery(sql);

		Scanner sc = new Scanner(System.in);

		int rowCount = 0;

		while (resultSet.next())
			rowCount++;

		if (rowCount == 1) {
			resultSet.first();
			System.out.println("P: enter new name book:");
			System.out.print("U: ");
			String newName = sc.nextLine();

			sql = "UPDATE book " + "SET name = '" + newName + "' WHERE name = '" + oldName + "';";

			statement.executeUpdate(sql);
			System.out.println("P: name of the book has been edited");
		}

		else if (rowCount > 1) {
			System.out.println("P: we have few books with such name please choose one by typing a number of book:");

			int num = 0;
			resultSet.absolute(num);
			int[] books = new int[rowCount];

			while (resultSet.next()) {
				books[num] = resultSet.getInt("id");
				System.out.println("\t" + ++num + ". " + resultSet.getString("author") + " \"" + oldName + "\"");
			}

			System.out.print("U: ");
			int id = sc.nextInt();

			while (id > num || id == 0) {
				System.out.println("P: wrong number of book");
				System.out.print("U: ");
				sc = new Scanner(System.in);
				id = sc.nextInt();
			}

			resultSet.absolute(id);
			id--;

			System.out.println("P: enter new name book:");
			System.out.print("U: ");
			sc = new Scanner(System.in);
			String newName = sc.nextLine();

			sql = "UPDATE book " + "SET name = '" + newName + "' WHERE id = " + books[id] + ";";

			statement.executeUpdate(sql);
			System.out.println("P: name of the book has been edited");
		}

		else
			System.out.println("P: not such book");

		if (resultSet != null) {
			resultSet.close();
		}
	}

	public static void removeBook(Statement statement, String command) throws SQLException {

		int index = command.indexOf(32);

		if (index == -1) {
			System.out.println("P: wrong command");
			return;
		}

		String name = command.substring(index + 1, command.length());

		String sql = "SELECT * FROM BOOK WHERE name = '" + name + "' ORDER BY name;";
		ResultSet resultSet = statement.executeQuery(sql);

		Scanner sc = new Scanner(System.in);

		int rowCount = 0;

		while (resultSet.next())
			rowCount++;

		if (rowCount == 1) {
			resultSet.first();
			String author = resultSet.getString("author");
			sql = "DELETE FROM book WHERE name= '" + name + "';";
			statement.executeUpdate(sql);
			System.out.println("P: book " + author + " \"" + name + "\" was removed");
		}

		else if (rowCount > 1) {
			System.out.println("P: we have few books with such name please choose one by typing a number of book:");

			int num = 0;
			resultSet.absolute(num);
			int[] books = new int[rowCount];

			while (resultSet.next()) {
				books[num] = resultSet.getInt("id");
				System.out.println("\t" + ++num + ". " + resultSet.getString("author") + " \"" + name + "\"");
			}

			System.out.print("U: ");
			int id = sc.nextInt();

			while (id > num || id == 0) {
				System.out.println("P: wrong number of book");
				System.out.print("U: ");
				sc = new Scanner(System.in);
				id = sc.nextInt();
			}

			resultSet.absolute(id);
			String author = resultSet.getString("author");
			id--;

			sql = "DELETE FROM book WHERE id= " + books[id] + ";";
			statement.executeUpdate(sql);
			System.out.println("P: book " + author + " \"" + name + "\" was removed");
		}

		else
			System.out.println("P: not such book");

		if (resultSet != null) {
			resultSet.close();
		}
	}

}