package featherpowders.schematics;

import featherpowders.versions.ServerVersion;

public class SchematicNotSupportedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -6168689159003799448L;
    
    public SchematicNotSupportedException(ServerVersion current, ServerVersion targeted) {
        super("The current server version (" + current.friendlyName + ") is not supported by the schematic (" + targeted.friendlyName + ")");
    }

}
