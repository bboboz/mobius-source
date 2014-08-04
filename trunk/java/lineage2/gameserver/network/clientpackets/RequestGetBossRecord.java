/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExGetBossRecord;
import lineage2.gameserver.network.serverpackets.ExGetBossRecord.BossRecordInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestGetBossRecord extends L2GameClientPacket
{
	/**
	 * Field _bossID.
	 */
	@SuppressWarnings("unused")
	private int _bossID;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_bossID = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		int totalPoints = 0;
		int ranking = 0;
		
		if (activeChar == null)
		{
			return;
		}
		
		List<BossRecordInfo> list = new ArrayList<>();
		Map<Integer, Integer> points = RaidBossSpawnManager.getInstance().getPointsForOwnerId(activeChar.getObjectId());
		
		if ((points != null) && !points.isEmpty())
		{
			for (Map.Entry<Integer, Integer> e : points.entrySet())
			{
				switch (e.getKey())
				{
					case -1:
						ranking = e.getValue();
						break;
					
					case 0:
						totalPoints = e.getValue();
						break;
					
					default:
						list.add(new BossRecordInfo(e.getKey(), e.getValue(), 0));
				}
			}
		}
		
		activeChar.sendPacket(new ExGetBossRecord(ranking, totalPoints, list));
	}
}
