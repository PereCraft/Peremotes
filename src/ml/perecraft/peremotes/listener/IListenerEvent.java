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
package ml.perecraft.peremotes.listener;

import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author PereCraft
 */
public interface IListenerEvent {
    
    /**
     * Method that set all the emotes from a configuration file
     * @param config 
     */
    public void setEmotes(FileConfiguration config);
    
    /**
     * Method that return all the emotes
     * @return HashMap with the emotes
     */
    public HashMap<String, String> getEmotes();
}
