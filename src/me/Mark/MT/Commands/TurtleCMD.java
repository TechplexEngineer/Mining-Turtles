package me.Mark.MT.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

//import me.Mark.MT.Script;
import me.Mark.MT.Turtle;
import me.Mark.MT.TurtleMgr;

public class TurtleCMD implements CommandExecutor, TabCompleter {

	private static final String help = ChatColor.GREEN + "Turtle commands:\n"
			+ " - /t <name> start <direction> <script> <times>\n" + " - /t <name> stop\n" + " - /t <name> destroy";
	private static final String[] argss = new String[] { "start", "stop", "destroy" },
			argssf = new String[] { "NORTH", "EAST", "SOUTH", "WEST" };

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage("There are "+TurtleMgr.getTurtles().size()+" turtles:");
			for (Turtle tur : TurtleMgr.getTurtles()) {
				sender.sendMessage(tur.getName());
			}
			return false;
		}
		
		
		String name = args[0];
		Turtle t = TurtleMgr.getByName(name);
		if (t == null) {
			sender.sendMessage(ChatColor.RED + "Turtle " + name + " does not exist.");
			return false;
		}
		Player owner = t.getOwner();
		if (owner != sender) {
			sender.sendMessage(ChatColor.RED + "You don't own that turtle.");
			return false;
		}
		
		if(args[1].equalsIgnoreCase("delete")) {
			t.destroy();

			return false;
		}
		
		if(args[1].equalsIgnoreCase("move")) {
			if (args.length > 2) {
				t.move(args[2]);
			} else {
				boolean ret = t.move(BlockFace.DOWN);
				System.out.println(ret);
			}
		}
		
		if(args[1].equalsIgnoreCase("rotate")) {
			if (args.length > 2) {
				t.rotate(args[2]);
			} else {
				boolean ret = t.rotate(BlockFace.NORTH);
				System.out.println(ret);
			}
		}
		
		if(args[1].equalsIgnoreCase("mine")) {
			if (args.length > 2) {
				boolean ret = t.breakBlock(args[2]);
				System.out.println(ret);
			} else {
				boolean ret = t.breakBlock(BlockFace.NORTH);
				System.out.println(ret);
			}
		}
//		System.out.println(StringUtils.join(args," | "));
//		System.out.println();
//		for(Turtle tur : TurtleMgr.getTurtles()) {
//			System.out.println(tur.getName());
//		}
//		Turtle t = TurtleMgr.getByName("fred");
//		if (t != null) {
//			t.move(Face.BACK);
//			t.breakBlock(Face.DOWN);
//		} else {
//			System.out.println("Turtle fred not found");
//		}
		
		return false;

//		if (args[1].equalsIgnoreCase("list")) {
//			System.out.println(TurtleMgr.getTurtles().size());
//			for(Turtle tur : TurtleMgr.getTurtles()) {
//				System.out.println(tur.getName());
//			}
//			return false;
//		}
//		
//		if (!(args.length > 1)) {
//			sender.sendMessage(help);
//			return false;
//		}
//		String name = args[0];
//		Turtle t = TurtleMgr.getByName(name);
//		if (t == null) {
//			sender.sendMessage(ChatColor.RED + "Turtle " + name + " does not exist.");
//			return false;
//		}
//		Player owner = t.getOwner();
//		if (owner != sender) {
//			sender.sendMessage(ChatColor.RED + "You don't own that turtle.");
//			return false;
//		}
//		
//		if (args[1].equalsIgnoreCase("start")) {
////			if (t.isRunning()) {
////				sender.sendMessage(ChatColor.RED + "Turtle is already running a script. \"/t " + t.getName()
////						+ " stop\" to stop it.");
////				return false;
////			}
//			if (args.length != 5) {
//				sender.sendMessage(help);
//				return false;
//			}
////			t.setScript(Script.getFromConfig(args[3]));
//			BlockFace bf;
//			try {
//				bf = BlockFace.valueOf(args[2].toUpperCase());
//			} catch (Exception e) {
//				sender.sendMessage(ChatColor.RED + "Use the names for blockfaces. eg NORTH, EAST, SOUTH or WEST");
//				return false;
//			}
//			if (bf != BlockFace.NORTH && bf != BlockFace.EAST && bf != BlockFace.SOUTH && bf != BlockFace.WEST) {
//				sender.sendMessage(ChatColor.RED + "You can only use directions; NORTH, EAST, SOUTH or WEST");
//				return false;
//			}
//			t.setDir(bf);
////			t.start(Integer.parseInt(args[4]));
//			sender.sendMessage(ChatColor.GREEN + "Started " + t.getName());
//		} else if (args[1].equalsIgnoreCase("stop")) {
//			if (owner != sender && !sender.hasPermission("turtles.stop")) {
//				sender.sendMessage(ChatColor.RED + "You don't own that turtle.");
//				return false;
//			}
////			t.stop();
//			if (owner != null && t.getOwner() != sender)
//				owner.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + sender.getName() + " stopped "
//						+ t.getName() + "'s current task.");
//			sender.sendMessage(ChatColor.GREEN + "You stopped " + t.getName() + "'s current task.");
//		} else if (args[1].equalsIgnoreCase("destroy")) {
//			if (t.getOwner() == sender || sender.hasPermission("turtles.destroy")) {
//				t.destroy();
//				sender.sendMessage(ChatColor.GREEN + "Destroyed turtle " + t.getName());
//				if (!(t.getOwner() == null))
//					if (sender != t.getOwner())
//						t.getOwner().sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Your turtle" + t.getName()
//								+ " was destroyed by " + sender.getName());
//			} else
//				sender.sendMessage(ChatColor.RED + "You do not own that turtle.");
//		}
//		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			List<String> possibles = new ArrayList<>();

			for (Turtle t : TurtleMgr.getTurtles()) {
				if (sender != t.getOwner())
					continue;
				String name = t.getName();
				if (args[0].length() == 0) {
					possibles.add(name);
					continue;
				}
				if (name.toLowerCase().startsWith(args[0].toLowerCase()))
					possibles.add(name);
			}
			return possibles;
		}
		if (args.length == 2) {
			List<String> pos = new ArrayList<>();
			for (String s : argss)
				if (s.startsWith(args[1].toLowerCase()))
					pos.add(s);
			return pos;
		}
		if (args.length == 3) {
			List<String> pos = new ArrayList<>();
			for (String s : argssf)
				if (s.toLowerCase().startsWith(args[2].toLowerCase()))
					pos.add(s);
			return pos;
		}
//		if (args.length == 4) {
//			List<String> pos = new ArrayList<String>();
//			for (String s : Script.getScripts())
//				if (s.toLowerCase().startsWith(args[3].toLowerCase()))
//					pos.add(s);
//			return pos;
//		}
		return null;
	}
}
