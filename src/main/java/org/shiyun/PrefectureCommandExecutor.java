package org.shiyun;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class PrefectureCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            fetchRegionData(args[0], sender);
        } else {
            sender.sendMessage("Usage: /prefecture <id>");
        }
        return true;
    }

    private void fetchRegionData(String id, CommandSender sender) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Example URL, replace with your actual URL
                    String urlString = "https://region.shiyun.org/api/v1/region/" + id;
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    // Check if the connection is successful
                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200) {
                        throw new RuntimeException("HttpResponseCode: " + responseCode);
                    } else {
                        StringBuilder informationString = new StringBuilder();
                        Scanner scanner = new Scanner(url.openStream());

                        while (scanner.hasNext()) {
                            informationString.append(scanner.nextLine());
                        }
                        scanner.close();

                        // Parse the JSON response
                        Gson gson = new Gson();
                        JsonObject data = gson.fromJson(informationString.toString(), JsonObject.class);

                        // Extract data
                        String name = data.get("name").getAsString();
                        String nameLatin = data.get("name-latin").getAsString();
                        String regionCode = data.get("region-code").getAsString();
                        String zhAbbreviation = data.get("zh-abbreviation").getAsString();
                        String type = data.get("type").getAsString();
                        String est = data.get("est").getAsString();
                        String id = data.get("id").getAsString();

                        // Use Bukkit's main thread to send a message to the player
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sender.sendMessage("--- Information of " + name + " (" + nameLatin + ") ---");
                                sender.sendMessage("ID: " + id);
                                sender.sendMessage("Name: " + name);
                                sender.sendMessage("Name (Latin): " + nameLatin);
                                sender.sendMessage("Region Code: " + regionCode);
                                sender.sendMessage("Chinese Abbreviation: " + zhAbbreviation);
                                sender.sendMessage("Type: " + type);
                                sender.sendMessage("Established in: " + est);
                            }
                        }.runTask(Village.getPlugin(Village.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Village.getPlugin(Village.class));
    }
}
