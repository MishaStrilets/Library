/**
 * @author Mykhailo Strilets
 * @version 1.2
 */

package strilets;

import java.sql.*;
import java.util.Scanner;

public class Driver {

	/**
	 * Main function
	 * 
	 * @param args
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
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

		Book book = new Book();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		boolean exit = false;

		while (!exit) {
			String command;
			System.out.print("U: ");
			command = sc.nextLine();

			String operation = new String();
			String data = new String();

			int i = command.indexOf(32);

			if (i == -1)
				operation = command;
			else {
				operation = command.substring(0, i);
				data = command.substring(i + 1);
			}

			if ("add".equals(operation))
				book.addBook(statement, data);

			else if ("all".equals(operation))
				book.allBooks(statement);

			else if ("edit".equals(operation))
				book.editBook(statement, data);

			else if ("remove".equals(operation))
				book.removeBook(statement, data);

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

}