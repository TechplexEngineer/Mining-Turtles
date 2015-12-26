package me.Mark.MT.Listeners;

import java.util.logging.Logger;
import me.Mark.MT.Main;
import me.Mark.MT.Utils.SignGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Mark.MT.Turtle;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {
	JavaPlugin plugin;
	
	public PlayerListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Handle the removal of turtles when blocks are broken
	 * @param event 
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Main.turtleMaterial)
			return;
		Turtle t = Turtle.getTurtleAt(event.getBlock());
		if (t == null)
			return;
		if (!event.getPlayer().getName().equalsIgnoreCase(t.getOwner().getName()) && !event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You do not own this turtle.");
		} else {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Destroyed turtle " + t.getName());
			t.destroy();
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onOpeninv(PlayerInteractEvent event) {
		if (event.getPlayer().isSneaking() 
				|| event.getClickedBlock() == null 
				|| event.getClickedBlock().getType() != Main.turtleMaterial
				|| event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		Player p = event.getPlayer();
		Block blk = event.getClickedBlock();
		Turtle t = Turtle.getTurtleAt(blk);
		if (t == null) {
			Player player = event.getPlayer();
			if (player.getItemInHand().getType() == Main.turtleWand) {
				createTurtle(p, blk);
				event.setCancelled(true);
			}
			return;
		}
		if (!t.getOwner().getName().equalsIgnoreCase(p.getName())) {
			p.sendMessage(ChatColor.RED + "You do not own this turtle!");
			return;
		}
		p.openInventory(t.getInventory());
		event.setCancelled(true);
	}
	
	/**
	 * Ask the player for the name of a turtle and create one if name not exists
	 * @param player
	 * @param blk
	 */
	public void createTurtle(Player player, final Block blk) {
		//get the name of the turtle from the user
		SignGUI gui = new SignGUI(plugin);
		gui.open(player, new String[]{"", "Enter a turtle", "name on the first", "line of this sign."}, new SignGUI.SignGUIListener() {
			@Override
			public void onSignDone(Player player, String[] lines) {
				String name = lines[0];
				if (name.length() == 0) {
					player.sendMessage(ChatColor.RED + "Please enter a valid name.");
					return;
				}
				Turtle t = Turtle.getByName(name);
				if (t == null) {
					t = new Turtle(name, Main.turtleMaterial, blk.getLocation(), player.getName());
					Turtle.turtles.add(t);
					player.sendMessage("Created turtle: " + t.getName());
				} else {
					player.sendMessage("A turtle with that name already exists.");
				}
			}

		});
	}
}