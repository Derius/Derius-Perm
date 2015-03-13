package dk.muj.derius.perm.entity;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.PermUtil;

public class Perm extends Entity<Perm>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static Perm get(Object oid)
	{
		return PermColl.get().get(oid, false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public Perm load(Perm that)
	{
		if (that == this || that == null) return that;
		
		this.setDefault(that.isDefault);
		
		return this;
	}
	
	@Override
	public void preAttach(String id)
	{
		this.setId(id);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	boolean isDefault = true;
	public boolean isDefault() { return isDefault; }
	public void setDefault(boolean def)
	{
		boolean old = this.isDefault();
		this.isDefault = def;
		// If it was updated update bukkit permission.
		// If not attached this is during Permcoll#init and the bukkit perm has not yet been created
		if (this.isDefault() != old && this.attached())
		{
			this.updateBukkitPermission();
			this.changed();
		}
	}
	public PermissionDefault getPermissionDefault() { return this.isDefault() ? PermissionDefault.TRUE : PermissionDefault.OP; }
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public void updateBukkitPermission()
	{
		PluginManager pm = Bukkit.getPluginManager();
		 if (pm == null) return; // This might happen on startup
		
		Permission perm = pm.getPermission(this.getId());
		PermUtil.set(perm, this.getPermissionDefault());
	}
	
}
