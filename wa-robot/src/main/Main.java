package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import communication.BTConnect;
import controller.Controller;
import movement.Movement.move;

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting");
		BTConnect connection = new BTConnect();
		try {
			List<move> moves = connection.getMoves();
			System.out.println("Recieved path");
			if (connection.shouldRun()) {
				System.out.println("Was told to run");
				System.out.println(Arrays.toString(moves.toArray()));
				Controller controller = new Controller(moves);
				controller.run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished path");
	}
}
