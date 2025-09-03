package tetyastan.customSteveChaosReworked.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private final File file;
	protected FileConfiguration config;
	private final boolean fileJar;

	public Config(File file, boolean fileJar) {
		this.file = file;
		this.fileJar = fileJar;
		load();
	}

	public Config(String file, boolean fileJar) {
		this(new File(file), fileJar);
	}

	public void save() {
		if (config == null) return;
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			String folder = "config";
			if (!file.exists()) {
				if (fileJar) {
					FileUtil.copyFromJar(getClass().getResourceAsStream("/" + folder + "/" + file.getName()), file);
				} else {
					file.createNewFile();
				}
			}
			config = YamlConfiguration.loadConfiguration(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		load();
	}

	public boolean contains(String path) {
		return config != null && config.contains(path);
	}

	public void set(String path, Object obj) {
		if (config != null) config.set(path, obj);
	}

	public Object get(String path) {
		return config != null ? config.get(path) : null;
	}

	public String getString(String path) {
		return config != null ? config.getString(path) : null;
	}

	public int getInt(String path) {
		return config != null ? config.getInt(path) : 0;
	}

	public boolean getBoolean(String path) {
		return config != null && config.getBoolean(path);
	}

	public List<String> getStringList(String path) {
		return config != null ? config.getStringList(path) : List.of();
	}

	public Set<String> getKeys(boolean deep) {
		return config != null ? config.getKeys(deep) : Set.of();
	}

	public Location getLocation(String path) {
		if (config == null) return null;
		try {
			return LocationUtil.parseLoc(getString(path));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setLocation(String path, Location loc, boolean rotation) {
		set(path, LocationUtil.toString(loc, rotation));
	}
}
