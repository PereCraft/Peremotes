/*
 * Copyright (C) 2021 PereCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ml.perecraft.peremotes.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ml.perecraft.peremotes.Peremotes;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author PereCraft
 */
public class Commands implements CommandExecutor {
    
    private final HashMap<String, String> emotes = Peremotes.getPlugin().getEmotes();
    private FileConfiguration config = Peremotes.getPlugin().getConfig();
    private HashMap<String, String> replyList = new HashMap<>();
    private final boolean isEnabled = Boolean.parseBoolean(Peremotes.getPlugin().getConfig().getString("enable"));
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String name, String[] args) {

        if(cs instanceof Player) {
            Player p = (Player)cs;

            if(args.length == 0) {
                getHelp(p);
                return true;
            } else if((args.length == 1) && (args[0].equals("help"))){
                getHelp(p);
                return true;
            } else if((args.length == 1) && (args[0].equalsIgnoreCase("reload"))){
                
                if(!cs.hasPermission("peremotes.reload")){
                    p.sendMessage(ChatColor.RED + "Non hai i permessi!");
                    return false;
                }
                
                Peremotes.getPlugin().reload();
                p.sendMessage(ChatColor.GREEN + "Reload plugin...");
                return true;
            } else if((args[0].equalsIgnoreCase("msg")) || (args[0].equalsIgnoreCase("tell"))) {
                
                if(!cs.hasPermission("peremotes.msg")){
                    p.sendMessage(ChatColor.RED + "Non hai i permessi!");
                    return false;
                }
                
                if(args.length == 1) {
                    p.sendMessage(ChatColor.RED + "Specifica il nome del player!");
                    return false;
                } else if(args.length == 2) {
                    p.sendMessage(ChatColor.RED + "Specifica il messaggio!");
                    return false;
                } else if((args.length > 2) && (Bukkit.getServer().getPlayerExact(args[1]) != null)) {
                    
                    sendMessage(p, Bukkit.getServer().getPlayerExact(args[1]), args, 2);
                    return true;
                    
                } else {
                    p.sendMessage(ChatColor.RED + "Utente non esiste!");
                    return false;
                }
                
            } else if(args[0].equalsIgnoreCase("r")) {
                
                if(!cs.hasPermission("peremotes.reply")) {
                    p.sendMessage(ChatColor.RED + "Non hai i permessi!");
                    return false;
                }
                
                if(args.length == 1) {
                    p.sendMessage(ChatColor.RED + "Specifica il messaggio!");
                    return false;
                } else if((replyList.containsKey(p.getName())) && (args.length > 1) && 
                        (Bukkit.getServer().getPlayerExact(replyList.get(p.getName())) != null)) {
                        
                    sendMessage(p, Bukkit.getPlayer(replyList.get(p.getName())), args, 1);
                    replyList.remove(p.getName());
                    return true;
                
                } else {
                    p.sendMessage(ChatColor.RED + "Nessuno ti ha inviato un messaggio!");
                    return false;
                }
                
            }
            
        } else if(cs instanceof ConsoleCommandSender) {
            
            if((args.length == 1) && (args[0].equalsIgnoreCase("reload"))){
                Peremotes.getPlugin().reload();
                System.out.println("Reload plugin...");
                return true;
            }
            
        }
        
        return false;
        
    }
        
    public void getHelp(Player p) {
                
        p.sendMessage(ChatColor.GREEN + "----- Peremotes help page ------------");
                
        p.spigot().sendMessage(new ComponentBuilder("/pe msg <player> <messaggio>")
                    .color(ChatColor.YELLOW)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pe msg "))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Manda un messaggio privato con le emotes!")))
                    .create()
        );
        
        p.spigot().sendMessage(new ComponentBuilder("/pe r <messaggio>")
                    .color(ChatColor.YELLOW)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pe r "))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Rispondi a un messaggio rapidamente!")))
                    .create()
        );
        
        p.sendMessage(ChatColor.GREEN + "----- Peremotes emotes help page -----");
                      
        config.getConfigurationSection("emotes-list").getKeys(true).forEach((emote) -> {
            
            String helpRaw = emote + ChatColor.YELLOW + " -> ";
                        
            for(String key: config.getStringList("emotes-list." + emote))    
                helpRaw += key + " ";
            
            p.spigot().sendMessage(new ComponentBuilder(helpRaw.replaceAll("&", "§"))
                .color(ChatColor.YELLOW)
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
                        (String)config.getStringList("emotes-list." + emote).get(0) + " "))
                .create()
            );
            
        });
        
        p.sendMessage(ChatColor.GREEN + "-----------------------------------");
        
    }
    
    private void sendMessage(Player sender, Player reciver, String[] args, int index) {
        
        Set<String> emotesInChat = new HashSet<String>();
        String[] messageSplit = new String[args.length - index];

        for(int i = index; i < args.length; i++)
            messageSplit[i-index] = args[i];

        String message = String.join(" ", messageSplit);
        
        if(!isEnabled) {
            sender.sendMessage("§a[Peremotes]§f[io -> " + reciver.getDisplayName() + "] "+ message);
            reciver.sendMessage("§a[Peremotes]§f[" + sender.getDisplayName() + " -> io] "+ message);
            return;
        }
        
        Matcher match = Pattern.compile("[(-)*|0-z]*").matcher(message);

        while(match.find())
            emotesInChat.add(match.group());

        for(String value: emotesInChat)
            if(emotes.containsKey(value))
                message = message.replace(value, emotes.get(value) + "§r");

        sender.sendMessage("§a[Peremotes]§f[io -> " + reciver.getDisplayName() + "] "+ message);
        reciver.sendMessage("§a[Peremotes]§f[" + sender.getDisplayName() + " -> io] "+ message);
        
        if(reciver.hasPermission("peremotes.reply")) {
            if(!replyList.containsKey(reciver.getName()))
                replyList.put(reciver.getName(), sender.getName());
            else
                replyList.replace(reciver.getName(), sender.getName());
        }
            
        
    }
    
}
