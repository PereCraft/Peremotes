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
package listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.configuration.file.FileConfiguration;
/**
 *
 * @author PereCraft
 */
public class ListenerEvent implements Listener, IListenerEvent {
    
    private HashMap<String, String> emotes;
    private boolean isEnabled;
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        
        if(!e.getPlayer().hasPermission("peremotes.sendemotes")) {
            e.getPlayer().sendMessage(ChatColor.RED + "Non hai i permessi");
            return;
        }else if(!isEnabled) {
            return;
        }
                
        
        String message = e.getMessage();
        Matcher match = Pattern.compile("[(-)*|0-z]*").matcher(message);
        Set<String> emotesInChat = new HashSet<String>();
        
        while(match.find())
            emotesInChat.add(match.group());
        
        for(String value: emotesInChat) 
            if(emotes.containsKey(value))
                message = message.replace(value, emotes.get(value) + "§r");
               
            
        e.setMessage(message);
                
    }
    
    @EventHandler
    public void onSign(SignChangeEvent e) {
        
        if(!e.getPlayer().hasPermission("peremotes.signemotes")) {
            e.getPlayer().sendMessage(ChatColor.RED + "Non hai i permessi");
            return;
        }else if(!isEnabled) {
            return;
        }
        
        int cont = 0;
        String message = String.join("\n", e.getLines());
        Matcher match = Pattern.compile("[(-)*|0-z]*").matcher(message);
        Set<String> emotesInChat = new HashSet<String>();
        
        while(match.find())
            emotesInChat.add(match.group());
        
        for(String value: emotesInChat) 
            if(emotes.containsKey(value))
                message = message.replace(value, emotes.get(value) + "§r");
        
        for(String line: message.split("\n")) {
            e.setLine(cont, line);
            cont++;
        }
        
    }
    
    @Override
    public void setEmotes(FileConfiguration config) {
        
        isEnabled = Boolean.parseBoolean(config.getString("enable"));
        if(!isEnabled)
            return;
        
        emotes = new HashMap<>();
        
        config.getConfigurationSection("emotes-list").getKeys(true).forEach((emote) -> {
            config.getStringList("emotes-list." + emote).forEach((key) -> {
                
                if(!emotes.containsKey(key)) {
                    emotes.put(key, emote);
                } else {
                    System.err.println("[Peremotes] Error in config.yml: " 
                            + "You can't use the same key for two different emotes.");
                }
                
            });
        });
        
    }
    
    @Override
    public HashMap<String, String> getEmotes() {
        return emotes;
    }
    
}
