package me.Mark.MT;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

public class Turtle {

	private String name;
	private Location loc;
	private Material mat;
//	private Script script;
	private Inventory inv;
	private String owner;
	private int mined = 0, placed = 0;


	public Turtle(String name, Material mat, Location loc, String owner) {
		this.name = name;
		this.loc = loc;
		this.mat = mat;
		this.owner = owner;
		inv = Bukkit.createInventory(null, 9 * 4,  name + " the turtle");
		TurtleMgr.add(this);
	}

//	public Turtle(String name, Material mat, Location loc, String owner, Script script) {
//		this(name, mat, loc, owner);
//		this.script = script;
//	}
	
//	Accessors
	/**
	 * Get the owner as a player object
	 * @return Player
	 */
	public Player getOwner() {
		return Bukkit.getPlayer(owner);
	}
	
	/**
	 * Get the name of the owner
	 * @return owner's name
	 */
	public String getOwnerName() {
		return owner;
	}

	/**
	 * Get the player specified name of the turtle
	 * @return the turtle's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the turtle's location
	 * @return Location
	 */
	public Location getLocation() {
		return loc;
	}
	
	/**
	 * Get the turtle's inventory
	 * @return Inventory
	 */
	public Inventory getInventory() {
		return inv;
	}
	
	/**
	 * Get the type of material the turtle is made from
	 * @return Material
	 */
	public Material getMaterial() {
		return mat;
	}
	
	/**
	 * Get the direction the turtle is facing
	 * @return Cardinal direction BlockFace.{NORTH, EAST, SOUTH, WEST}
	 */
	public BlockFace getDir() {
		
		try {
			//@todo what happens if the block is not directional?
			//@note throws an exception which we catch. Should be able to use reflection...
			Block b = this.loc.getBlock();
			BlockState state = b.getState();
			Directional d = ((Directional) state.getData());
			return d.getFacing();

		} catch (ClassCastException e) {
			System.out.println("Is not Instance");
			return null;
		}
	}

//	actions
	
	public boolean breakBlock(Face face) {
		if (getInventory().firstEmpty() == -1)
			return false;
		Block b = loc.getBlock().getRelative(getBlockFace(face));
		if (b.getType() == Material.BEDROCK 
				|| b.getType() == Material.CHEST 
				|| b.getType() == Material.AIR
				|| b.getType() == Material.MOB_SPAWNER 
				|| b.getType() == Material.PORTAL
				|| b.getType() == Material.ENDER_PORTAL_FRAME 
				|| b.getType() == Material.ENDER_PORTAL)
			return false;
		for (ItemStack is : b.getDrops())
			getInventory().addItem(is);
		b.setType(Material.AIR);
		return true;
	}

	/*
	 * private Material getBrokenType(Material type) { if (type ==
	 * Material.GRASS) return Material.DIRT; if (type == Material.COAL_ORE)
	 * return Material.COAL; if (type == Material.STONE) return
	 * Material.COBBLESTONE; if (type == Material.DEAD_BUSH) return
	 * Material.AIR; if (type == Material.LONG_GRASS) return Material.AIR; if
	 * (type == Material.LEAVES || type == Material.LEAVES_2) return
	 * Material.AIR; if (type == Material.GLASS) return Material.AIR; if (type
	 * == Material.THIN_GLASS) return Material.AIR; if (type == Material.ICE)
	 * return Material.AIR; return type; }
	 */

	/**
	 * Move the turtle to the newloc
	 * Does not check if there is a block in the way
	 * @param newloc new location to move to 
	 * @param facing direction to face
	 * @return success
	 */
	public boolean setLocation(Location newloc, BlockFace facing) {
		this.loc.getBlock().setType(Material.AIR);
		this.loc = newloc;
		Block b = loc.getBlock();
		b.setType(mat);
		try {
			//@todo what happens if the block is not directional?
			//@note throws an exception which we catch. Should be able to use reflection...
			BlockState state = b.getState();
			Directional d = ((Directional)state.getData());
			d.setFacingDirection(facing);
			state.update();
			System.out.println("Is Instance");
			return true;
		} catch (ClassCastException e) {
			System.err.println("Is not Instance");
		}
		return false;
	}

	public BlockFace str2blockFace(String face) {
		BlockFace out = null;
		if (face.equalsIgnoreCase("NORTH")) {
			out = BlockFace.NORTH;
		}
		if (face.equalsIgnoreCase("EAST")) {
			out = BlockFace.EAST;
		}
		if (face.equalsIgnoreCase("SOUTH")) {
			out = BlockFace.SOUTH;
		}
		if (face.equalsIgnoreCase("WEST")) {
			out = BlockFace.WEST;
		}
		if (face.equalsIgnoreCase("UP")) {
			out = BlockFace.UP;
		}
		if (face.equalsIgnoreCase("DOWN")) {
			out = BlockFace.DOWN;
		}
		return out;
	}
	
	/**
	 * Move the turtle in the given absolute direction
	 * @param face
	 * @return success 
	 */
	public boolean move(BlockFace face) {
		
		Location l = this.loc.getBlock().getRelative(face).getLocation();
		if (l.getBlock().getType() != Material.AIR) {
			System.out.println("Can't move, "+l.getBlock().getType()+" block in the way.");
			return false;
		}
		setLocation(l, getDir());
		return true;
	}
	
	/**
	 * Move the turtle in the given absolute direction
	 * Automatically resolve strings {NORTH, EAST, SOUTH, WEST} to the proper BlockFace
	 * @param face
	 * @return success
	 */
	public boolean move(String face) {
		BlockFace out = str2blockFace(face);
		if (out == null) {
			return false;
		} else {
			return move(out);
		}
	}
	
	/**
	 * Turn the turtle
	 * @param dir direction to head
	 * @return success
	 */
	public boolean rotate(BlockFace dir) {
		
		return setLocation(this.loc, dir);

	}
	
	/**
	 * Turn the turtle
	 * Automatically resolve strings {NORTH, EAST, SOUTH, WEST} to the proper BlockFace
	 * @param dir direction to head
	 * @return success
	 */
	public boolean rotate(String dir) {
		BlockFace out = str2blockFace(dir);
		if (out == null) {
			return false;
		} else {
			return rotate(out);
		}
	}
	
	
//	public BlockFace a(String dir) {
//		if(dir.equalsIgnoreCase("RIGHT")) {
//			
//		}
//	}

//	public void setScript(Script script) {
//		this.script = script;
//	}

//	private int task;
//	private boolean running;
//	private int timees;

//	public boolean isRunning() {
//		return running;
//	}
//
//	public boolean start(final int timess) {
//		if (isRunning())
//			return false;
//		running = true;
//		timees = timess * script.getLength();
//		mined = 0;
//		placed = 0;
//		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.inst, new Runnable() {
//			@Override
//			public void run() {
//				if (timees > 0) {
//					Command cmd = script.getNextCommand();
//					try {
//						processCommand(cmd.getLabel(), cmd.getArgs());
//					} catch (Exception e) {
//						if (getOwner() != null)
//							getOwner().sendMessage(ChatColor.RED + "There is an error in: \"" + cmd.getLabel() + " "
//									+ cmd.getArgs()[0] + "...\"");
//						else
//							System.out.println(
//									"There is an error in: \"" + cmd.getLabel() + " " + cmd.getArgs()[0] + "...\"");
//					}
//					timees--;
//				} else {
//					running = false;
//					if (Bukkit.getPlayer(owner) != null)
//						Bukkit.getPlayer(owner).sendMessage(name + " is done with the script! It mined " + mined
//								+ " blocks and placed " + placed + " blocks.");
//					Bukkit.getScheduler().cancelTask(task);
//				}
//			}
//		}, 0, 20);
//		return true;
//	}

	

//	public void stop() {
//		Bukkit.getScheduler().cancelTask(task);
//		running = false;
//	}

	public void destroy() {
//		if (isRunning())
//			stop();
		for (ItemStack is : getInventory().getContents())
			if (is != null)
				loc.getWorld().dropItem(loc.add(.5, .5, .5), is);
		inv.clear();
		loc.getBlock().breakNaturally();
		TurtleMgr.remove(this.getName());
	}

	/*
	 * public BlockFace getBlockFace(Face face) { int dirr = dir +
	 * face.getDir(); if(face.getDir() == 4) return BlockFace.UP;
	 * if(face.getDir() == 5) return BlockFace.DOWN; if (dirr > 3) dir -= 3; if
	 * (dirr == 0) return BlockFace.SOUTH; if (dirr == 1) return BlockFace.WEST;
	 * if (dirr == 2) return BlockFace.NORTH; return BlockFace.EAST; }
	 */

	public BlockFace getBlockFace(Face fface) {
		BlockFace face = this.getDir();
		if (fface.getDir() == 4)
			return BlockFace.UP;
		if (fface.getDir() == 5)
			return BlockFace.DOWN;

		if (face == BlockFace.NORTH) {
			if (fface == Face.LEFT) {
				return BlockFace.WEST;
			} else if (fface == Face.RIGHT) {
				return BlockFace.EAST;
			} else if (fface == Face.FORWARD) {
				return BlockFace.NORTH;
			} else if (fface == Face.BACK) {
				return BlockFace.SOUTH;
			}
		} else if (face == BlockFace.SOUTH) {
			if (fface == Face.LEFT) {
				return BlockFace.WEST;
			} else if (fface == Face.RIGHT) {
				return BlockFace.EAST;
			} else if (fface == Face.FORWARD) {
				return BlockFace.SOUTH;
			} else if (fface == Face.BACK) {
				return BlockFace.NORTH;
			}
		} else if (face == BlockFace.WEST) {
			if (fface == Face.LEFT) {
				return BlockFace.SOUTH;
			} else if (fface == Face.RIGHT) {
				return BlockFace.NORTH;
			} else if (fface == Face.FORWARD) {
				return BlockFace.WEST;
			} else if (fface == Face.BACK) {
				return BlockFace.EAST;
			}
		} else if (face == BlockFace.EAST) {
			if (fface == Face.LEFT) {
				return BlockFace.NORTH;
			} else if (fface == Face.RIGHT) {
				return BlockFace.SOUTH;
			} else if (fface == Face.FORWARD) {
				return BlockFace.EAST;
			} else if (fface == Face.BACK) {
				return BlockFace.WEST;
			}
		}
		return BlockFace.SELF;
	}

//	public void processCommand(String label, String[] args) {
//		if (label.equalsIgnoreCase("move"))
//			move(Face.valueOf(args[0].toUpperCase()));
//		else if (label.equalsIgnoreCase("break")) {
//			if (breakBlock(Face.valueOf(args[0].toUpperCase())))
//				mined++;
//		} else if (label.equalsIgnoreCase("place")) {
//			place(Face.valueOf(args[0].toUpperCase()), getType(args[1]));
//		} else if (label.equalsIgnoreCase("if")) {
//			if (check(Face.valueOf(args[0].toUpperCase()), getType(args[1]))) {
//				String[] argss = new String[args.length - 3];
//				for (int i = 3; i < args.length; i++)
//					argss[i - 3] = args[i];
//				processCommand(args[2], argss);
//			}
//		}
//	}

	private Material getType(String arg) {
		if (arg.startsWith("slot:")) {
			ItemStack is = getInventory().getItem(Integer.parseInt(arg.substring(5)));
			return is == null ? Material.AIR : is.getType();
		}
		return Material.getMaterial(arg);
	}

	private boolean check(Face face, Material mat) {
		return this.loc.getBlock().getRelative(getBlockFace(face)).getType() == mat;
	}

	private boolean place(Face face, Material mat) {
		Block b = this.loc.getBlock().getRelative(getBlockFace(face));
		if (b.getType() != Material.AIR)
			return false;
		if (getInventory().containsAtLeast(new ItemStack(mat), 1)) {
			for (int i = 0; i < getInventory().getSize(); i++) {
				ItemStack is = getInventory().getItem(i);
				if (is == null)
					continue;
				if (is.getType() == mat) {
					int am = is.getAmount() - 1;
					is.setAmount(am);
					if (am <= 0)
						getInventory().setItem(i, null);
					break;
				}
			}
			b.setType(mat);
			placed++;
			return true;
		}
		return false;
	}

	// statics

	
}
