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
package ml.perecraft.peremotes;

import java.util.HashMap;

import ml.perecraft.peremotes.command.Commands;
import ml.perecraft.peremotes.listener.ListenerEvent;

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
        plugin = this;
        saveDefaultConfig();        
        
        listenerEvent = new ListenerEvent();
        Bukkit.getPluginManager().registerEvents(listenerEvent, plugin);
        listenerEvent.setEmotes(getConfig());
        
        commands = new Commands();
        getCommand("peremotes").setExecutor(commands);
        getLogger().info(getName() + " enabled");
    }
    
    @Override
    public void onDisable() {
        listenerEvent = null;
        commands = null;
        getLogger().info(getName() + " disabled");
    }
    
    public static Peremotes getPlugin() {
        return plugin;
    }
    
    public void reload() {
        getLogger().info("Reloading " + getName());
        reloadConfig();
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }
    
    @Override
    public HashMap<String, String> getEmotes() {
        return listenerEvent.getEmotes();
    }
            
}
