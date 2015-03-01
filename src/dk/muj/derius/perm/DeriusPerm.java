package dk.muj.derius.perm;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.permissions.Permission;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.util.PermUtil;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DeriusAPI;
import dk.muj.derius.api.Req;
import dk.muj.derius.api.Skill;
import dk.muj.derius.api.events.AbilityRegisteredEvent;
import dk.muj.derius.api.events.SkillRegisteredEvent;
import dk.muj.derius.perm.entity.Perm;
import dk.muj.derius.perm.entity.PermColl;
import dk.muj.derius.req.ReqHasPerm;

public class DeriusPerm extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusPerm i;
	public static DeriusPerm get() { return i; }
	public DeriusPerm() { i = this; }
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
		
		PermColl.get().init();
		
		for (Skill skill : DeriusAPI.getAllSkills())
		{
			this.setupSkill(skill);
		}
		
		for (Ability ability : DeriusAPI.getAllAbilities())
		{
			this.setupAbility(ability);
		}
		
		this.postEnable();
	}
	
	@Override
	public void onDisable()
	{
		// Remove requirements from skills
		DeriusAPI.getAllSkills().forEach(skill ->
		{
			// remove learn requirements
			skill.setLearnRequirements
			(
				skill.getLearnRequirements().stream()
				.filter(r -> ! (r instanceof ReqHasPerm))
				.collect(Collectors.toList())
			);
			
			// Remove see requirements
			skill.setSeeRequirements
			(
				skill.getSeeRequirements().stream()
				.filter(r -> ! (r instanceof ReqHasPerm))
				.collect(Collectors.toList())
			);
			
		});
		
		// Remove requirements from abilities
		DeriusAPI.getAllAbilities().forEach(ability ->
		{
			// Remove activate requirements
			ability.setActivateRequirements
			(
				ability.getActivateRequirements().stream()
				.filter(r -> ! (r instanceof ReqHasPerm))
				.collect(Collectors.toList())
			);
			
			// Remove see requirements
			ability.setSeeRequirements
			(
				ability.getSeeRequirements().stream()
				.filter(r -> ! (r instanceof ReqHasPerm))
				.collect(Collectors.toList())
			);
			
		});
		
		super.onDisable();
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW)
	public void setupSkill(SkillRegisteredEvent event)
	{
		this.setupSkill(event.getSkill());
	}
	
	public void setupSkill(Skill skill)
	{
		// Create see perm
		Permission seePermission = new Permission("derius.skill.see." + skill.getId(), "see that skill");
		Perm seePerm = PermColl.get().get(seePermission, true);
		PermUtil.set(seePermission, seePerm.getPermissionDefault());
		
		// Add see perm
		Bukkit.getPluginManager().addPermission(seePermission);
		Req reqHasSeePerm = new ReqHasPerm(seePermission);
		skill.addSeeRequirements(reqHasSeePerm);
		
		// Create learn perm
		Permission learnPermission = new Permission("derius.skill.learn." + skill.getId(), "learn the skill " + skill.getName());
		Perm learnPerm = PermColl.get().get(learnPermission, true);
		PermUtil.set(learnPermission, learnPerm.getPermissionDefault());
		
		// Add learn perm
		Bukkit.getPluginManager().addPermission(learnPermission);
		Req reqHasLearnPerm = new ReqHasPerm(seePermission);
		skill.addLearnRequirements(reqHasLearnPerm);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void setupAbility(AbilityRegisteredEvent event)
	{
		this.setupAbility(event.getAbility());
	}
	
	public void setupAbility(Ability ability)
	{
		// Create see perm
		Permission seePermission = new Permission("derius.ability.see." + ability.getId(), "ability that skill");
		Perm seePerm = PermColl.get().get(seePermission, true);
		PermUtil.set(seePermission, seePerm.getPermissionDefault());
		
		// Add see perm
		Bukkit.getPluginManager().addPermission(seePermission);
		Req reqHasSeePerm = new ReqHasPerm(seePermission);
		ability.addSeeRequirements(reqHasSeePerm);
		
		// Create learn perm
		Permission learnPermission = new Permission("derius.ability.learn." + ability.getId(), "learn the ability " + ability.getName());
		Perm learnPerm = PermColl.get().get(learnPermission, true);
		PermUtil.set(learnPermission, learnPerm.getPermissionDefault());
		
		// Add learn perm
		Bukkit.getPluginManager().addPermission(learnPermission);
		Req reqHasLearnPerm = new ReqHasPerm(seePermission);
		ability.addActivateRequirements(reqHasLearnPerm);
	}
	
}
