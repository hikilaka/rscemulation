/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/

//NPC ID: 253  
package org.openrsc.server.npchandler.Heroes_Quest;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.logging.Logger;
import org.openrsc.server.logging.model.eventLog;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;
import org.openrsc.server.util.DataConversions;

// Required items, for this quest
// Item 557 Firebird Feather
// Item 586 Master Thief Armband
// Item 590 Cooked Lava Eel
// Note: They will have to actually acquire these items properly from the quest in order to complete, It's not enough just to have the items.


public class Achetties implements NpcHandler 
{
	public void handleNpc(final Npc npc, final Player owner) throws Exception 
	{
		npc.blockedBy(owner);
		owner.setBusy(true);
		
		Quest q = owner.getQuest(20);
		Quest shieldOfArrav = owner.getQuest(13);
		Quest dragonSlayer = owner.getQuest(17);
		Quest lostCity = owner.getQuest(19);
		Quest merlinsCrystal = owner.getQuest(22);

		if (q == null)
		{
			if (shieldOfArrav != null && shieldOfArrav.finished() && dragonSlayer != null && dragonSlayer.finished() && lostCity != null && lostCity.finished() && merlinsCrystal != null && merlinsCrystal.finished() && owner.getQuestPoints() >= 56) 
			{
				QuestNotStarted(npc, owner);
			}
			else
			{
				QuestReqNotMet(npc, owner);
			}
		}
		if (q != null)
		{
			if (q.finished())
			{
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Congratulations on joining the heroes guild"}, true) 
				{
					public void finished() 
					{
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Thank you"}) 
						{
							public void finished() 
							{
								owner.setBusy(false);
								npc.unblock();
							}
						});
					}
				});
			}
			else
			{	
				QuestStarted(npc, owner);	
			}
		}
	}
	
	private void QuestNotStarted(final Npc npc, final Player owner) 
	{ 
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Greetings welcome to the heroes guild", "Only the foremost heroes of the land can enter here"}, true) 
		{
			public void finished() 
			{
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) 
				{
					public void action() 
					{
						final String[] menu_options = { "I'm a hero, may I apply to join?", "Good for the foremost heroes of the land" };
						owner.setBusy(false);
						owner.sendMenu(menu_options);
						owner.setMenuHandler(new MenuHandler(menu_options)
						{
							public void handleReply(final int option, final String reply) 
							{
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) 
								{
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) 
								{
									case 0:
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Ok you may begin the tasks for joining the heroes guild", "You need the feather of an Entrana firebird", "A master thief armband", "And a cooked lava eel"}) 
										{
											public void finished() 
											{
												World.getDelayedEventHandler().add(new SingleEvent(owner, 2500) 
												{
													public void action() 
													{
														/*
														 * 	Add the quest and
														 *  quest stage to our
														 *  character, whos accepted.
														 */
														owner.addQuest(20, 1);
														owner.incQuestCompletionStage(20);
														
														final String[] menu_options = { "Any hints on getting the armband?", "Any hints on getting the feather", "Any hints on getting the eel", "I'll start looking for all those things then" };
														owner.setBusy(false);
														owner.sendMenu(menu_options);
														owner.setMenuHandler(new MenuHandler(menu_options)
														{
															public void handleReply(final int option, final String reply) 
															{
																owner.setBusy(true);
																for(Player informee : owner.getViewArea().getPlayersInView()) 
																{
																	informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
																}
																switch(option) 
																{
																	case 0:
																		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I'm sure you have relevant contacts to find out about that"}, false) 
																		{
																			public void finished()
																			{
																				owner.setBusy(false);
																				npc.unblock();
																			}
																		});
																	break;
																	
																	case 1:
																		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Not really - Entrana firebirds live on Entrana"}, false) 
																		{
																			public void finished()
																			{
																				owner.setBusy(false);
																				npc.unblock();
																			}
																		});
																	break;
																	
																	case 2:
																		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Maybe go and find someone who knows a lot about fishing?"}, false) 
																		{
																			public void finished()
																			{
																				owner.setBusy(false);
																				npc.unblock();
																			}
																		});
																	break;
																	
																	case 3:
																		owner.setBusy(false);
																		npc.unblock();
																	break;
																}
															}
														});
													}
												});
											}
										});
									break;
									
									case 1:
										owner.setBusy(false);
										npc.unblock();
									break;
								}
							}
						});
					}
				});
			}
		});	
	}
	
	private void QuestStarted(final Npc npc, final Player owner) 
    {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Greetings welcome to the heroes guild", "How goes thy quest?"}, true) 
		{
			public void finished()
			{
				if(owner.getInventory().countId(557) > 0 && owner.getInventory().countId(586) > 0 && owner.getInventory().countId(590) > 0 && owner.getQuest(20).getStage() >= 5) 
				{
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"I have all the things needed"}) 
					{
						public void finished()
						{
							World.getDelayedEventHandler().add(new SingleEvent(owner, 2000) 
							{
								public void action() 
								{
									owner.sendMessage("You hand over the items to Achetties");
									owner.getInventory().remove(557, 1);
									owner.getInventory().remove(586, 1);
									owner.getInventory().remove(590, 1);
									owner.sendInventory();	
									World.getDelayedEventHandler().add(new SingleEvent(owner, 2500) 
									{
										public void action() 
										{
											owner.sendMessage("@gre@You have completed the Heroes guild quest!");
											owner.sendMessage("@gre@You have been awarded 1 quest point!");
											owner.finishQuest(20);
											owner.incQuestExp(0, 3075 * 5); //attack exp
											owner.sendStat(0);
											owner.incQuestExp(1, 3075 * 5); //defence exp
											owner.sendStat(1);
											owner.incQuestExp(2, 3075 * 5); //strength exp
											owner.sendStat(2);
											owner.incQuestExp(3, 3075 * 5); //health exp
											owner.sendStat(3);
											owner.incQuestExp(4, 2075 * 5); //ranged exp
											owner.sendStat(4);
											owner.incQuestExp(7, 2825 * 5); //cooking exp
											owner.sendStat(7);
											owner.incQuestExp(8, 1575 * 5); //woodcutting exp
											owner.sendStat(8);
											owner.incQuestExp(10, 2725 * 5); //fishing exp
											owner.sendStat(10);
											owner.incQuestExp(11, 1575 * 5); //Firemaking exp
											owner.sendStat(11);
											owner.incQuestExp(13, 2258 * 5); //smithing exp
											owner.sendStat(13);
											owner.incQuestExp(14, 2575 * 5); //mining exp
											owner.sendStat(14);
											owner.incQuestExp(15, 1325 * 5); //Herblaw exp
											owner.sendStat(15);
											owner.setBusy(false);
											npc.unblock();
											Logger.log(new eventLog(owner.getUsernameHash(), owner.getAccount(), owner.getIP(), DataConversions.getTimeStamp(), "<strong>" + owner.getUsername() + "</strong>" + " has completed the <span class=\"recent_quest\">Heroes</span> quest!"));
										}
									});
								}
							});
						}
					});
				}
				else
				{
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"It's tough, I've not done it yet"}) 
					{
						public void finished()
						{
							World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Remember you need the feather of an Entrana firebird", "A master thief armband", "And a cooked lava eel"}, false) 
							{
								public void finished() 
								{
									World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) 
									{
										public void action() 
										{
											final String[] menu_options = { "Any hints on getting the armband?", "Any hints on getting the feather", "Any hints on getting the eel", "I'll start looking for all those things then" };
											owner.setBusy(false);
											owner.sendMenu(menu_options);
											owner.setMenuHandler(new MenuHandler(menu_options)
											{
												public void handleReply(final int option, final String reply) 
												{
													owner.setBusy(true);
													for(Player informee : owner.getViewArea().getPlayersInView()) 
													{
														informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
													}
													switch(option) 
													{
														case 0:
															World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I'm sure you have relevant contacts to find out about that"}, false) 
															{
																public void finished()
																{
																	owner.setBusy(false);
																	npc.unblock();
																}
															});
														break;
														
														case 1:
															World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Not really - Entrana firebirds live on Entrana"}, false) 
															{
																public void finished()
																{
																	owner.setBusy(false);
																	npc.unblock();
																}
															});
														break;
														
														case 2:
															World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Maybe go and find someone who knows a lot about fishing?"}, false) 
															{
																public void finished()
																{
																	owner.setBusy(false);
																	npc.unblock();
																}
															});
														break;
														
														case 3:
															owner.setBusy(false);
															npc.unblock();
														break;
													}
												}
											});
										}
									});
								}
							});
						}
					});
				}
			}
		});
    }
	
	private void QuestReqNotMet(final Npc npc, final Player owner) 
	{
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Greetings welcome to the heroes guild", "Only the foremost heroes of the land can enter here"}, true) 
		{
			public void finished() 
			{
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) 
				{
					public void action() 
					{
						final String[] options107 = {"I'm a hero, may I apply to join?", "Good for the foremost heroes of the land"};
						owner.setBusy(false);
						owner.sendMenu(options107);
						owner.setMenuHandler(new MenuHandler(options107) 
						{
							public void handleReply(final int option, final String reply) 
							{
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView())
								{
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) 
								{
									case 0:
										QuestDeclined(npc, owner);
									break;
									case 1:
										owner.setBusy(false);
										npc.unblock();
									break;
								}
							}
						});
					}
				});
			}
		});
	}	
	
	private void QuestDeclined(final Npc npc, final Player owner)
	{
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"you're a hero? I've never heard of you"}) 
		{
			public void finished()
			{
				World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
				{
					public void action() 
					{
						owner.sendMessage("You need to have 56 quest points to file for an application");
						World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
						{
							public void action() 
							{
								owner.sendMessage("You also need to have completed the following quests:");
								World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
								{
									public void action() 
									{
										owner.sendMessage("The shield of Arrav, The Lost City, Merlin's Crystal, Dragon Slayer");
										owner.setBusy(false);
										npc.unblock();
									}
								});
							}	
						});
					}
				});
			}
		});
	}
	
}