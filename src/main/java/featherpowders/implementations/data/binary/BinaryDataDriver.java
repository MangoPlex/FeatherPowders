package featherpowders.implementations.data.binary;

import org.bukkit.plugin.Plugin;

import featherpowders.data.DataContainer;
import featherpowders.data.DataDriver;

public class BinaryDataDriver extends DataDriver {

    @Override
    public String getDriverName() { return "featherpowders-binary"; }

    @Override
    public DataContainer getContainerFor(Plugin plugin) { return new BinaryDataContainer(plugin); }

}
