package net.sf.l2j.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.xml.RestartPointData;
import net.sf.l2j.gameserver.enums.RestartType;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.skills.L2Skill;

public class L2SkillTeleport extends L2Skill
{
	private final String _recallType;
	private final Location _loc;
	
	public L2SkillTeleport(StatSet set)
	{
		super(set);
		
		_recallType = set.getString("recallType", "");
		_loc = set.getLocation("teleCoords", null);
	}
	
	@Override
	public void useSkill(Creature activeChar, WorldObject[] targets)
	{
		if (activeChar instanceof Player)
		{
			// Check invalid states.
			if (activeChar.isAfraid() || ((Player) activeChar).isInOlympiadMode() || activeChar.isInsideZone(ZoneId.BOSS))
				return;
		}
		
		boolean bsps = activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject obj : targets)
		{
			if (!(obj instanceof Player))
				continue;
			
			final Player target = ((Player) obj);
			
			// Check invalid states.
			if (target.isFestivalParticipant() || target.isInJail() || target.isInDuel() || target.isRiding() || target.isFlying())
				continue;
			
			if (target != activeChar)
			{
				if (target.isInOlympiadMode())
					continue;
				
				if (target.isInsideZone(ZoneId.BOSS))
					continue;
			}
			
			// teleCoords are prioritized over recallType, if existing.
			Location loc = _loc;
			
			// If teleCoords aren't existing, we calculate the regular way using recallType.
			if (loc == null)
			{
				if (_recallType.equalsIgnoreCase("Castle"))
					loc = RestartPointData.getInstance().getLocationToTeleport(target, RestartType.CASTLE);
				else if (_recallType.equalsIgnoreCase("ClanHall"))
					loc = RestartPointData.getInstance().getLocationToTeleport(target, RestartType.CLAN_HALL);
				else
					loc = RestartPointData.getInstance().getLocationToTeleport(target, RestartType.TOWN);
			}
			
			if (loc != null)
			{
				target.setIsIn7sDungeon(false);
				target.teleportTo(loc, 20);
			}
		}
		
		activeChar.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, isStaticReuse());
	}
}