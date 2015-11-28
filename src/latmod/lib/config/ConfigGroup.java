package latmod.lib.config;

import java.lang.reflect.Field;

import latmod.lib.*;
import latmod.lib.util.FinalIDObject;

public final class ConfigGroup extends FinalIDObject implements Cloneable
{
	public final FastList<ConfigEntry> entries;
	private String displayName = null;
	public ConfigList parentList = null;
	
	public ConfigGroup(String s)
	{
		super(s);
		entries = new FastList<ConfigEntry>();
	}
	
	public void add(ConfigEntry e)
	{
		if(e != null && !entries.contains(e))
		{ entries.add(e); e.parentGroup = this; }
	}

	public ConfigGroup addAll(Class<?> c)
	{
		try
		{
			Field f[] = c.getDeclaredFields();
			
			if(f != null && f.length > 0)
			for(int i = 0; i < f.length; i++)
			{
				try
				{
					f[i].setAccessible(true);
					if(ConfigEntry.class.isAssignableFrom(f[i].getType()))
					{
						ConfigEntry entry = (ConfigEntry)f[i].get(null);
						if(entry != null) add(entry);
					}
				}
				catch(Exception e1) { }
			}
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		return this;
	}
	
	public ConfigGroup setName(String s)
	{ displayName = s; return this; }
	
	public String getDisplayName()
	{ return displayName == null ? LMStringUtils.firstUppercase(ID) : displayName; }
	
	public String getFullID()
	{
		if(!isValid()) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(parentList.ID);
		sb.append('.');
		sb.append(ID);
		return sb.toString();
	}
	
	public boolean isValid()
	{ return ID != null && parentList != null && parentList.ID != null; }
	
	public ConfigGroup clone()
	{
		ConfigGroup g = new ConfigGroup(ID);
		g.displayName = displayName;
		g.entries.addAll(entries);
		return g;
	}
}