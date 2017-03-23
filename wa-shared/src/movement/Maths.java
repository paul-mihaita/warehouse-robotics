package movement;

import utils.Location;

public class Maths {
	public static Location changeAngle(Double theata, Location l) {
		int x = l.getX();
		int y = l.getY();
		theata = theata * (Math.PI / 180);
		int newx = round((x * Math.cos(theata)) + (y * Math.sin(theata)));
		int newy = round((x * (-Math.sin(theata))) + (y * Math.cos(theata)));
		l.setX(newx);
		l.setY(newy);
		return l;
	}
	public static int round(double d) {
		if (d > 0) {
			return (int) (d + 0.5);
		}
		return (int) (d - 0.5);
	}

	public static Location addLocation(Location a, Location b) {
		return new Location(a.getX() + b.getX(), a.getY() + b.getY());
	}
	public static Location minusLocation(Location a, Location b) {
		return new Location(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
	public static Location square(Location a) {
		return new Location(a.getX() * a.getX(), a.getY() * a.getY());
	}
	
	public static int findAngle(Location u, Location v) {
		double x = Math.atan2(u.getY(), u.getX());
		double y = Math.atan2(v.getY(), v.getX());
		return round((x - y) * (180/Math.PI));
	}
	
	
	
	public static double dotProduct(Location u, Location v) {
		return (u.getX() * v.getX()) + (u.getY() + v.getY());
	}
	
	public static double detProduct(Location u, Location v) {
		return (u.getX() * v.getY()) - (u.getY() + v.getX());
	}
	
}
