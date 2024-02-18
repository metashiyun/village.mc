package org.shiyun;

import org.bukkit.plugin.java.JavaPlugin;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Village extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("prefecture").setExecutor(new PrefectureCommandExecutor());
    }
}