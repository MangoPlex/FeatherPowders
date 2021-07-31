package featherpowders.schematics.modifiers;

import featherpowders.schematics.Schematic;

public abstract class SchematicModifier {
    
    /**
     * Modify the schematic
     * @param input
     * @return
     */
    public abstract Schematic modifySchematic(Schematic input);
    
}
