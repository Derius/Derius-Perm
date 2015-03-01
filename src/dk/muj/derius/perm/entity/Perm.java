package dk.muj.derius.perm.entity;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

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
		
		boolean old = this.isDefault();
		this.setDefault(that.isDefault);
		if (this.isDefault() != old) this.updateBukkitPermission();
		
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean isDefault = true;
	public boolean isDefault() { return isDefault; }
	public void setDefault(boolean def) { this.isDefault = def; this.changed();}
	public PermissionDefault getPermissionDefault() { return this.isDefault() ? PermissionDefault.TRUE : PermissionDefault.OP; }
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public void updateBukkitPermission()
	{
		Permission perm = Bukkit.getPluginManager().getPermission(this.getId());
		PermUtil.set(perm, this.getPermissionDefault());
	}
	
}
