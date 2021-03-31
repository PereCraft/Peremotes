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
package peremotes;

import java.util.HashMap;

import listener.ListenerEvent;
import command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


/**
 *
 * @author PereCraft
 */
public class Peremotes extends JavaPlugin implements IPlugin {

    private static Peremotes plugin;
    private ListenerEvent listenerEvent;
    private Commands commands;
    
    @Override
    public void onEnable() {
        System.out.println("[Peremotes] Starting plugin...");
        
        plugin = this;
        saveDefaultConfig();        
        
        listenerEvent = new ListenerEvent();
        Bukkit.getPluginManager().registerEvents(listenerEvent, plugin);
        listenerEvent.setEmotes(getPlugin().getConfig());
        
        commands = new Commands();
        getCommand("peremotes").setExecutor(commands);
        
    }
    
    @Override
    public void onDisable() {
        System.out.println("[Peremotes] Stopping plugin...");
        listenerEvent = null;
        commands = null;
    }
    
    public static Peremotes getPlugin() {
        return plugin;
    }
    
    @Override
    public void onReload() {
        System.out.println("[Peremotes] Reloading plugin...");
        
        onDisable();
        onEnable();
    }
    
    @Override
    public void onReloadConfig() {
        System.out.println("[Peremotes] Reloading config file...");
        
        reloadConfig();
        onReload();
    }
    
    @Override
    public HashMap<String, String> getEmotes() {
        return listenerEvent.getEmotes();
    }
            
}
