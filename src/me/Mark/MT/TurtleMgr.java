/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.Mark.MT;


import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;


/**
 * Manage a list of turtles
 * @author techplex
 */
public class TurtleMgr {
	private static List<Turtle> turtles = new ArrayList<Turtle>();
	
	/**
	 * Get a list of turtles
	 * @return a collection of turtles
	 */
	public static List<Turtle> getTurtles() {
		return turtles;
	}
	
	/**
	 * Add a turtle to the list
	 * @param t turtle to add
	 * @return true on success, false otherwise
	 */
	public static boolean add(Turtle t) {
		if (TurtleMgr.getByName(t.getName()) == null) {
			turtles.add(t);
			return true;
		}
		return false;
		
	}
	
	/**
	 * Remove a turtle from the list
	 * @param t turtle to remove
	 */
	public static void remove(String name) {
		turtles.remove(TurtleMgr.getByName(name));
	}

	/**
	 * Get a turtle given its location
	 * @param l the location of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public static Turtle getByLoc(Location l) {
		for (Turtle t : turtles)
			if (t.getLocation().equals(l)) {
				return t;
			}
		return null;
	}

	/**
	 * Get a turtle given its name
	 * @param name the name of the turtle
	 * @return the turtle if found, null otherwise
	 */
	public static Turtle getByName(String name) {
		for (Turtle t : turtles)
			if (t.getName().equals(name)) {
				return t;
			}
		return null;
	}
}
