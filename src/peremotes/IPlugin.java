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

/**
 *
 * @author PereCraft
 */
public interface IPlugin {
    
    /**
     * Method that reload the entire plugin
     */
    public void onReload();
    
    /**
     * Method that reload the configuration file
     */
    public void onReloadConfig();
    
    /**
     * Method that return the HashMap with the emotes
     * @return The HashMap with emotes
     */
    public HashMap<String, String> getEmotes();
    
}
