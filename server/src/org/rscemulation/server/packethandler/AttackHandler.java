package org.rscemulation.server.packethandler;

import org.apache.mina.common.IoSession;
import org.rscemulation.server.Config;
import org.rscemulation.server.event.DelayedQuestChat;
import org.rscemulation.server.event.DelrithFightEvent;
import org.rscemulation.server.event.FightEvent;
import org.rscemulation.server.event.NpcRangeEvent;
import org.rscemulation.server.event.PlayerRangeEvent;
import org.rscemulation.server.event.VampireFightEvent;
import org.rscemulation.server.event.WalkToMobEvent;
import org.rscemulation.server.logging.Logger;
import org.rscemulation.server.logging.model.ExploitLog;
import org.rscemulation.server.model.ChatMessage;
import org.rscemulation.server.model.Npc;
import org.rscemulation.server.model.Player;
import org.rscemulation.server.model.World;
import org.rscemulation.server.net.Packet;
import org.rscemulation.server.net.RSCPacket;
import org.rscemulation.server.states.Action;
import org.rscemulation.server.util.DataConversions;

public class AttackHandler implements PacketHandler {
	public void handlePacket(Packet p, IoSession session) {
		try {
			Player player = (Player)session.getAttachment();
			if (player != null && !player.isRemoved()) {
				int pID = ((RSCPacket)p).getID();
				int serverIndex = p.readShort();
				if (!player.isBusy()) {
					if (p.readShort() == 117) {
						player.resetAllExceptDMing();							
						if (pID == 18) {
							final Player affectedPlayer = World.getPlayer(serverIndex);
							if (affectedPlayer != null && !affectedPlayer.equals(player)) {
								if (!affectedPlayer.getLocation().isInWarZone() || !World.islandCombat) {
								if (!player.getLocation().isInDMArena() || (player.getLocation().isInDMArena() && affectedPlayer == player.getInDMWith())) 
								{
									if(!affectedPlayer.isDev() && !affectedPlayer.isMod() && !affectedPlayer.isEvent()) 
									{
										
										player.setFollowing(affectedPlayer);
										player.setStatus(Action.ATTACKING_MOB);

										if (player.getRangeEquip() < 0) 
										{
											World.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedPlayer, 2) 
											{
												public void arrived() 
												{
													owner.resetPath();
													owner.resetFollowing();
													
													if (affectedPlayer.getCombatTimer() != 0 && !affectedPlayer.inCombat() && affectedPlayer.getCombatTimer() - System.currentTimeMillis() > -2000)
														return;

													if (owner.isBusy() || affectedPlayer.isBusy() || !owner.nextTo(affectedPlayer) || !owner.withinRange(affectedPlayer, 2) || !owner.checkAttack(affectedPlayer, false) || owner.getStatus() != Action.ATTACKING_MOB || affectedPlayer.getStatus() == Action.FIGHTING_MOB) 
													{
														if (affectedPlayer.getStatus() == Action.FIGHTING_MOB)
															owner.sendMessage("I can't get close enough");

														owner.resetFollowing();
														return;
													}
													
													owner.setSkulledOn(affectedPlayer);	
													
													if(!owner.isFighting() && !affectedPlayer.isFighting())
													{
														FightEvent fe = new FightEvent(owner, affectedPlayer);
														owner.setFightEvent(fe);
														affectedPlayer.setFightEvent(fe);
														World.getDelayedEventHandler().add(fe);
														affectedPlayer.setInCombatWith(player);
														player.setInCombatWith(affectedPlayer);
													}
												}
											});			
										} 
										else 
										{
											if (player.getLocation().isInDMArena() && player.getDMSetting(2)) 
											{
												player.sendMessage(Config.PREFIX + "Ranged cannot be used during this Death Match");
												return;
											}

											if (affectedPlayer.getCombatTimer() != 0 && !affectedPlayer.inCombat() && affectedPlayer.getCombatTimer() - System.currentTimeMillis() > -2000)
												return;

											int radius = player.getRangeType(player.getRangeEquip());
											World.getDelayedEventHandler().add(new WalkToMobEvent(player, affectedPlayer, radius) {
												public void arrived() {
													owner.resetPath();
													if (!owner.isBusy() && owner.checkAttack(affectedPlayer, true) && owner.getStatus() == Action.ATTACKING_MOB) {
														owner.resetAllExceptDMing();
														owner.setStatus(Action.RANGING_MOB);
														owner.setSkulledOn(affectedPlayer);
														owner.setPlayerRangeEvent(new PlayerRangeEvent(owner, affectedPlayer), affectedPlayer.getUsernameHash());
													}
												}
											});
										}
									} else {
										player.sendMessage(Config.PREFIX + "You cannot attack RSCUnity staff");
										player.resetFollowing();
										player.resetPath();
									}
								} else {
									player.sendMessage(Config.PREFIX + "You aren't in a Death Match with " + affectedPlayer.getUsername());
									player.resetFollowing();
									player.resetPath();	
								}
							} else {
								player.sendMessage("Combat is currently disabled on this island.");
								player.resetFollowing();
								player.resetPath();
							}
						}
				} else if (pID == 19) {
							final Npc npc = World.getNpc(serverIndex);
							if (npc != null) {
								switch (npc.getID()) {
								case 37:
									if (player.getQuest(52) != null) {
										if (player.getQuest(52).finished()) {
											player.setBusy(true);
											World.getDelayedEventHandler().add(new DelayedQuestChat(player, player, new String[] {"Nope, I'm not going to attack a fellow gang member."}) {
												public void finished() {
													owner.setBusy(false);
												}
											});
										}
									}
									break;

								case 35:
									if (player.getQuestCompletionStage(3) == 3) {
										if (player.getInventory().wielding(52)) {
											player.setFollowing(npc);
											player.setStatus(Action.ATTACKING_MOB);
											World.getDelayedEventHandler().add(new WalkToMobEvent(player, npc, 2) {
												public void arrived() {
													if (owner.isBusy() || npc.isBusy()  || !owner.nextTo(npc) || !owner.checkAttack(npc, false) || owner.getStatus() != Action.ATTACKING_MOB) {
														owner.resetFollowing();
														return;
													}

													owner.setStatus(Action.FIGHTING_MOB);
													for( Player informee : owner.getViewArea().getPlayersInView())
														informee.removeWatchedPlayer(owner);
													if(!owner.isFighting() && !npc.isFighting())
													{

														DelrithFightEvent fighting = new DelrithFightEvent(owner, npc);
														owner.setFightEvent(fighting);
														npc.setFightEvent(fighting);
														World.getDelayedEventHandler().add(fighting);
													}
												}
											});
											break;
										}
										for (Player informee : player.getViewArea().getPlayersInView())
											informee.informOfChatMessage(new ChatMessage(player, "Maybe I'd better wield Silverlight first", player));
										break;
									} else {
										player.sendMessage("I'd rather not, he looks scary.");
										break;
									}
								case 96:

									if (player.getRangeEquip() < 0) {
										player.setFollowing(npc);
										player.setStatus(Action.ATTACKING_MOB);
										World.getDelayedEventHandler().add(new WalkToMobEvent(player, npc, 2) {
											public void arrived() {
												owner.resetPath();
												if (owner.isBusy() || npc.isBusy()  || !owner.nextTo(npc) || !owner.checkAttack(npc, false) || owner.getStatus() != Action.ATTACKING_MOB) {
													owner.resetFollowing();
													return;
												}
												owner.setStatus(Action.FIGHTING_MOB);
												for (Player informee : owner.getViewArea().getPlayersInView())
													informee.removeWatchedPlayer(owner);
												if(!owner.isFighting() && !npc.isFighting())
												{
													// If they've completed the quest,
													// don't allow them to attack the NPC.
													if (player.getQuest(15).finished())
													{
														owner.sendMessage(Config.PREFIX + "You have already completed this quest.");
														return;
													}

													VampireFightEvent vfe = new VampireFightEvent(owner, npc);
													owner.setFightEvent(vfe);
													npc.setFightEvent(vfe);
													World.getDelayedEventHandler().add(vfe);
												}
											}
										});
									}
									break;

								case 259:
									if (player.getQuest(51) != null && player.getQuest(51).finished())
									{
										player.sendMessage("I don't think that's a smart idea");
										player.resetPath();
										return;
									}
									else
									{
										player.setFollowing(npc);
										player.setStatus(Action.ATTACKING_MOB);
										if (player.getRangeEquip() < 0) {
											World.getDelayedEventHandler().add(new WalkToMobEvent(player, npc, 2) {
												public void arrived() {
													owner.resetPath();
													if (owner.isBusy() || npc.isBusy() || !owner.nextTo(npc) || !owner.checkAttack(npc, false) || owner.getStatus() != Action.ATTACKING_MOB) {
														if (npc.isBusy())
															owner.sendMessage("I can't get close enough");
														owner.resetFollowing();
														return;
													}
													
													if (owner.getCombatTimer() != 0 && !owner.inCombat() && owner.getCombatTimer() - System.currentTimeMillis() > -2000)
														return;
													
													//need to figure out how I want to do this..
													if(!owner.isFighting() && !npc.isFighting())
													{
														FightEvent fe = new FightEvent(owner, npc);
														npc.setFightEvent(fe);
														owner.setFightEvent(fe);
														World.getDelayedEventHandler().add(fe);
													}
												}
											});
										} else {
											int radius = player.getRangeType(player.getRangeEquip());
											World.getDelayedEventHandler().add(new WalkToMobEvent(player, npc, radius) {
												public void arrived() {
													owner.resetPath();
													if (!owner.isBusy() && owner.checkAttack(npc, true) && owner.getStatus() == Action.ATTACKING_MOB) {
														owner.resetAllExceptDMing();
														owner.setStatus(Action.RANGING_MOB);
														owner.setNpcRangeEvent(new NpcRangeEvent(owner, npc));
													}
												}
											});
										}
									}
									break;

								default:
									player.setFollowing(npc);
									player.setStatus(Action.ATTACKING_MOB);
									if (player.getRangeEquip() < 0) {
										World.getDelayedEventHandler().add(new WalkToMobEvent(player, npc, 2) {
											public void arrived() {
												owner.resetPath();
												if (owner.isBusy() || npc.isBusy() || !owner.nextTo(npc) || !owner.checkAttack(npc, false) || owner.getStatus() != Action.ATTACKING_MOB) {
													if (npc.isBusy())
														owner.sendMessage("I can't get close enough");
													owner.resetFollowing();
													return;
												}
												//need to figure out how I want to do this..
												if(!owner.isFighting() && !npc.isFighting())
												{
													FightEvent fe = new FightEvent(owner, npc);
													npc.setFightEvent(fe);
													owner.setFightEvent(fe);
													World.getDelayedEventHandler().add(fe);
												}
											}
										});
									} else {
										int radius = player.getRangeType(player.getRangeEquip());
										World.getDelayedEventHandler().add(new WalkToMobEvent(player, npc, radius) {
											public void arrived() {
												owner.resetPath();
												if (!owner.isBusy() && owner.checkAttack(npc, true) && owner.getStatus() == Action.ATTACKING_MOB) {
													owner.resetAllExceptDMing();
													owner.setStatus(Action.RANGING_MOB);
													owner.setNpcRangeEvent(new NpcRangeEvent(owner, npc));
												}
											}
										});
									}
									break;
								}
							}
						}
					} else
						Logger.log(new ExploitLog(player.getUsernameHash(), player.getAccount(), player.getIP(), "AttackHandler (1)", DataConversions.getTimeStamp()));
				}
			}
		} catch(Exception ex) {
			System.out.println("Error with AttackHandler... " + ex);
		}
	}
}