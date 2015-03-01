package dk.muj.derius.perm.entity;

import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;

import dk.muj.derius.perm.Const;
import dk.muj.derius.perm.DeriusPerm;

public class PermColl extends Coll<Perm>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static PermColl i = new PermColl();
	public static PermColl get() { return i; }
	private PermColl()
	{
		super(Const.COLLECTION_PERMS, Perm.class, MStore.getDb(), DeriusPerm.get());
	}
	
	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String fixId(Object oid)
	{
		if (oid instanceof String) return (String) oid;
		if (oid instanceof Permission) return ((Permission) oid).getName();
		if (oid instanceof Perm) return ((Perm) oid).getId();
		
		return null;
	}
	
}
