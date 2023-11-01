package net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsMagical2;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;

public class MonasteryWeaponCheckAggressive extends WarriorCast3SkillsMagical2
{
	public MonasteryWeaponCheckAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsMagical2");
	}
	
	public MonasteryWeaponCheckAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22124,
		22125,
		22126,
		22127,
		22129
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player)
		{
			final Player player = creature.getActingPlayer();
			if (player.getActiveWeaponInstance() != null)
			{
				npc.broadcastNpcSay(NpcStringId.ID_1022122, player.getName());
				npc.getAI().addAttackDesire(creature, 500);
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
}
