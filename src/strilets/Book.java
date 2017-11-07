/**
 * @author Mykhailo Strilets
 * @version 1.2
 */

package strilets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book {

	/**
	 * Author of the book
	 */
	private String author;

	/**
	 * Name of the book
	 */
	private String name;

	/**
	 * Function adding book to library
	 * 
	 * @param statement
	 * @param data
	 * @throws SQLException
	 */
	public void addBook(Statement statement, String data) throws SQLException {

		int index = data.indexOf("\"");

		if (index == -1) {
			System.out.println("P: wrong command");
			return;
		}

		author = data.substring(0, index - 1);
		name = data.substring(index + 1, data.length() - 1);

		String sql = "INSERT INTO book (name, author)" + " VALUES " + "('" + name + "','" + author + "')";

		statement.executeUpdate(sql);
		System.out.println("P: book " + author + " \"" + name + "\" was added ");
	}

	/**
	 * Function viewing all books in library
	 * 
	 * @param statement
	 * @throws SQLException
	 */
	public void allBooks(Statement statement) throws SQLException {

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

	/**
	 * Function editing name of the book
	 * 
	 * @param statement
	 * @param data
	 * @throws SQLException
	 */
	public void editBook(Statement statement, String data) throws SQLException {

		if ("".equals(data)) {
			System.out.println("P: name not specified");
			return;
		}

		String oldName = data.substring(0, data.length());

		String sql = "SELECT * FROM BOOK WHERE name = '" + oldName + "' ORDER BY name;";
		ResultSet resultSet = statement.executeQuery(sql);

		@SuppressWarnings("resource")
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

	/**
	 * Function deleting book from the library
	 * 
	 * @param statement
	 * @param data
	 * @throws SQLException
	 */
	public void removeBook(Statement statement, String data) throws SQLException {

		if ("".equals(data)) {
			System.out.println("P: name not specified");
			return;
		}

		String name = data.substring(0, data.length());

		String sql = "SELECT * FROM BOOK WHERE name = '" + name + "' ORDER BY name;";
		ResultSet resultSet = statement.executeQuery(sql);

		@SuppressWarnings("resource")
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
