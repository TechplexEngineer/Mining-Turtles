package me.Mark.MT.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Mark.MT.Main;

public class ReloadCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("turtles.reload")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission.");
			return false;
		}
		Main.inst.reloadConfig();
		Main.inst.config = Main.inst.getConfig();
		sender.sendMessage(ChatColor.GREEN + "Reloaded the scripts!");
		return false;
	}
}
