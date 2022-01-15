package featherpowders.enums;

import org.bukkit.Bukkit;

/**
 * Server versions
 * @author nahkd
 *
 */
public enum ServerVersion {

    R1_9 ("Legacy 1.9",      true,  true,  false, null,   0),
    R1_10("Legacy 1.10",     true,  true,  false, R1_9,   1),
    R1_11("Legacy 1.11",     true,  true,  false, R1_10,  2),
    R1_12("Legacy 1.12",     true,  true,  false, R1_11,  3),
    
    R1_13("Release 1.13",    false, false, false, R1_12,  4),
    R1_14("Release 1.14",    false, true,  false, R1_13,  5),
    R1_15("Release 1.15",    false, true,  false, R1_14,  6),
    R1_16("Release 1.16",    false, true,  false, R1_15,  7),
    R1_17("Release 1.17",    false, true,  true,  R1_16,  8),
    R1_18("Release 1.18",    false, true,  true,  R1_17,  9),
    
    UNKNOWN("Unknown",       false, true,  true,  R1_17,  10),
    ;
    
    public static final ServerVersion[] VERSIONS_BY_DATAID = {
        R1_9, R1_10, R1_11, R1_12, R1_13, R1_14, R1_15, R1_16, R1_17, R1_18, UNKNOWN
    }; 
    
    public final String friendlyName;
    
    /** Check if the server version is legacy (1.12 and below) */
    public final boolean legacyVersion;
    
    /** Check if the server version is modern (1.13+) */
    public final boolean modernVersion;
    
    /** Check if the Spigot API changed (1.14+) */
    public final boolean modernAPIChange;
    
    /** Check if the server version uses official mapping (1.17+) */
    public final boolean mojangMapping;
    
    /** The previous version */
    public final ServerVersion featuresInherit;
    
    public final int dataID;
    
    private ServerVersion(String friendlyName, boolean isLegacy, boolean modernApiChange, boolean mojangMapping, ServerVersion featuresInherit, int dataID) {
        this.friendlyName = friendlyName;
        this.legacyVersion = isLegacy;
        this.modernVersion = !isLegacy;
        this.modernAPIChange = modernApiChange;
        this.mojangMapping = mojangMapping;
        this.featuresInherit = featuresInherit;
        this.dataID = dataID;
    }
    
    public boolean inheritFeatures(ServerVersion version) {
        return (version == this) || (version == featuresInherit) || (featuresInherit != null && featuresInherit.inheritFeatures(version));
    }
    
    private static ServerVersion version;
    
    public static ServerVersion getCurrentVersion() {
        if (version != null) return version;
        
        String bukkitVersion = Bukkit.getBukkitVersion();
        if (bukkitVersion.startsWith("1.18")) return version = R1_18;
        if (bukkitVersion.startsWith("1.17")) return version = R1_17;
        if (bukkitVersion.startsWith("1.16")) return version = R1_16;
        if (bukkitVersion.startsWith("1.15")) return version = R1_15;
        if (bukkitVersion.startsWith("1.14")) return version = R1_14;
        if (bukkitVersion.startsWith("1.13")) return version = R1_13;
        if (bukkitVersion.startsWith("1.12")) return version = R1_12;
        if (bukkitVersion.startsWith("1.11")) return version = R1_11;
        if (bukkitVersion.startsWith("1.10")) return version = R1_10;
        if (bukkitVersion.startsWith("1.9")) return version = R1_9;
        return version = UNKNOWN;
    }
    
}
