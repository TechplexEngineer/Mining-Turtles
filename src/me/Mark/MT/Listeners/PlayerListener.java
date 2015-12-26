package me.Mark.MT.Listeners;

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
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {
	JavaPlugin plugin;
	
	public PlayerListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInteract(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Main.turtleMaterial) {
			return;
		}
		final Player p = event.getPlayer();
		ItemStack i = p.getItemInHand();
		if (i.getType() != Material.BLAZE_ROD || i.getItemMeta() == null || !i.getItemMeta().getDisplayName().equals("Create a Turtle"))
			return;
		final Block b = event.getClickedBlock();
		for (Turtle t : Turtle.turtles)
			if (t.getLocation().getBlockX() == b.getX() && t.getLocation().getBlockY() == b.getY()
					&& t.getLocation().getBlockZ() == b.getZ()) {
				p.sendMessage(ChatColor.RED + "That is " + t.getName() + " owned by " + t.getOwnerName());
				event.setCancelled(true);
				return;
			}
		SignGUI gui = new SignGUI(plugin);
		gui.open(p, new String[]{"", "Enter a turtle", "name on the first", "line of this sign."}, new SignGUI.SignGUIListener() {
			@Override
			public void onSignDone(Player player, String[] lines) {
				String name = lines[0];
				if (name.length() == 0) {
					p.sendMessage(ChatColor.RED + "Please enter a valid name.");
					return;
				}
				Turtle t = Turtle.getByName(name);
				if (t == null) {
					t = new Turtle(name, Main.turtleMaterial, b.getLocation(), p.getName());
					Turtle.turtles.add(t);
					p.sendMessage("Created turtle: " + t.getName());
				} else {
					p.sendMessage("A turtle with that name already exists.");
				}
			}
	
		});

//		gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemStack(Material.NAME_TAG));
//		gui.open();
	}

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
		Turtle t = Turtle.getTurtleAt(event.getClickedBlock());
		if (t == null)
			return;
		if (!t.getOwner().getName().equalsIgnoreCase(p.getName())) {
			p.sendMessage(ChatColor.RED + "You do not own this turtle!");
			return;
		}
		p.openInventory(t.getInventory());
		event.setCancelled(true);
	}
}