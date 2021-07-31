package featherpowders.schematics.modifiers;

import featherpowders.schematics.Schematic;

public class ModifiersChain extends SchematicModifier {

    public final SchematicModifier[] modifiers;
    
    public ModifiersChain(SchematicModifier... modifiers) {
        this.modifiers = modifiers;
    }
    
    @Override
    public Schematic modifySchematic(Schematic input) {
        Schematic current = input;
        for (SchematicModifier modifier : modifiers) current = modifier.modifySchematic(current);
        return null;
    }

}
