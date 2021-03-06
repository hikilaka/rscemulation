/**
* Generated By NPCScript :: A scripting engine created for RSCEmulation by Zilent
*/
package org.rscemulation.server.npchandler.The_Sheep_Shearer;

// Converting this monster
import org.rscemulation.server.event.SingleEvent;
import org.rscemulation.server.logging.Logger;
import org.rscemulation.server.logging.model.eventLog;
import org.rscemulation.server.model.Npc;
import org.rscemulation.server.model.ChatMessage;
import org.rscemulation.server.model.MenuHandler;
import org.rscemulation.server.model.World;
import org.rscemulation.server.event.DelayedQuestChat;
import org.rscemulation.server.model.InvItem;
import org.rscemulation.server.model.Player;
import org.rscemulation.server.model.Quest;
import org.rscemulation.server.npchandler.NpcHandler;
import org.rscemulation.server.util.DataConversions;
public class Fred_The_Farmer implements NpcHandler {

	private void acceptQuest(final Npc npc, final Player owner) {
		final String[] messages75 = {"Ok I'll see you when you have some wool"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages75) {
			public void finished() {
				owner.addQuest(12, 1);
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private void notExciting(final Npc npc, final Player owner) {
		final String[] messages76 = {"Well what do you expect if you ask a farmer for a quest?", "Now are you going to help me or not?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages76) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options30 = {"Yes okay.  I can do that", "No I'll give it a miss"};
						owner.setBusy(false);
						owner.sendMenu(options30);
						owner.setMenuHandler(new MenuHandler(options30) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										acceptQuest(npc, owner);
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
	
	private void theThing(final Npc npc, final Player owner) {
		final String[] messages78 = {"I wouldn't worry about it", "Something ate all the previous shearers", "They probably got unlucky", "So are you going to help me?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages78) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options31 = {"Yes okay. I can do that", "Erm I'm a bit worried about this thing"};
						owner.setBusy(false);
						owner.sendMenu(options31);
						owner.setMenuHandler(new MenuHandler(options31) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										acceptQuest(npc, owner);
										break;
									case 1:
										final String[] messages80 = {"I'm sure it's nothing to worry about", "It's possible the other shearers aren't dead at all", "And are just hiding in the woods or something"};
										World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages80) {
											public void finished() {
												final String[] messages81 = {"I'm not convinced"};
												World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages81) {
													public void finished() {
														owner.setBusy(false);
														npc.unblock();
													}
												});
											}
										});
										break;
								}
							}
						});
					}
				});
			}
		});
	}
	
	private void lookQuest(final Npc npc, final Player owner) {
		final String[] messages74 = {"You're after a quest, you say?", "Actually I could do with a bit of help", "My sheep are getting mighty woolly", "If you could shear them", "And while you're at it, spin the wool for me too", "Yes, that's it.  Bring me 20 balls of wool", "And I'm sure I could sort out some sort of payment", "Of course, there's the small matter of the thing"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages74) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options29 = {"Yes okay.  I can do that", "That doesn't sound a very exciting quest", "What do you mean, the thing?"};
						owner.setBusy(false);
						owner.sendMenu(options29);
						owner.setMenuHandler(new MenuHandler(options29) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										acceptQuest(npc, owner);
										break;
									case 1:
										notExciting(npc, owner);
										break;
									case 2:
										theThing(npc, owner);
										break;
								}
							}
						});
					}
				});
			}
		});
	}
	
	private void lookingToKill(final Npc npc, final Player owner) {
		final String[] messages82 = {"What on my land?", "Leave my livestock alone you scoundrel"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages82) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}
	
	private void lost(final Npc npc, final Player owner) {
		final String[] messages83 = {"How can you be lost?", "Just follow the road east and south", "You'll end up in Lumbridge fairly quickly"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages83) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}
	
	private void questNotStarted(final Npc npc, final Player owner) {
		final String[] messages73 = {"What are you doing on my land?", "You're not the one who keeps leaving all my gates open?", "And letting out all my sheep?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages73, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options28 = {"I'm looking for a quest", "I'm looking for something to kill", "I'm lost"};
						owner.setBusy(false);
						owner.sendMenu(options28);
						owner.setMenuHandler(new MenuHandler(options28) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										lookQuest(npc, owner);
										break;
									case 1:
										lookingToKill(npc, owner);
										break;
									case 2:
										lost(npc, owner);
										break;
								}
								owner.setBusy(false);
								npc.unblock();
							}
						});
					}
				});
			}
		});
	}
	
	private void finishQuest(final Npc npc, final Player owner) {
		final String[] messages0 = {"That's all of them"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages0) {
			public void finished() {
				final String[] messages1 = {"I guess I'd better pay you then"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1) {
					public void finished() {
						World.getDelayedEventHandler().add(new SingleEvent(owner, 1000) {
							public void action() {
								owner.sendMessage("The farmer hands you some coins");
								owner.sendMessage("Well done you have completed the sheep shearer quest");
								owner.getInventory().add(new InvItem(10, 60));
								owner.sendInventory();
								owner.incQuestExp(12, 580);
								owner.sendStat(12);
								owner.finishQuest(12);
								owner.sendMessage("@gre@You have gained 1 quest point!");
								Logger.log(new eventLog(owner.getUsernameHash(), owner.getAccount(), owner.getIP(), DataConversions.getTimeStamp(), "<strong>" + owner.getUsername() + "</strong>" + " has completed the <span class=\"recent_quest\">Sheep Shearer</span> quest!"));
							}
						});
					}
				});
			}
		});
	}
	
	private void outOfBalls(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"That's all I've got so far"}) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"I need " + (20 - owner.getBallsOfWool()) + " more before I can pay you"}) {
					public void finished() {
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Okay I'll work on it"}) {
							public void finished() {
								owner.setBusy(false);
								npc.unblock();
							}
						});
					}
				});
			}
		});
	}
	
	private void giveBall(final Npc npc, final Player owner) {
		owner.sendMessage("You give Fred a ball of wool");
		owner.getInventory().remove(new InvItem(207, 1));
		owner.sendInventory();
		owner.incBallsOfWool();		
		if(owner.getInventory().contains(new InvItem(207, 1)) && owner.getBallsOfWool() < 20) {
			World.getDelayedEventHandler().add(new SingleEvent(owner, 1800) {
				public void action() {
					giveBall(npc, owner);
				}
			});
		} else if(owner.getBallsOfWool() == 20) {
			finishQuest(npc, owner);
		} else {
			outOfBalls(npc, owner);
		}
	}
	
	private void questFinished(final Npc npc, final Player owner) {
		final String[] messages73 = {"What are you doing on my land?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages73, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options28 = {"I'm looking for something to kill", "I'm lost"};
						owner.setBusy(false);
						owner.sendMenu(options28);
						owner.setMenuHandler(new MenuHandler(options28) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										lookingToKill(npc, owner);
										break;
									case 1:
										lost(npc, owner);
										break;
								}
								owner.setBusy(false);
								npc.unblock();
							}
						});
					}
				});
			}
		});
	}
	
	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		Quest q = owner.getQuest(12);
		if(q != null) {
			if(q.finished()) {
					questFinished(npc, owner);
			} else {
				final String[] messages0 = {"How are you doing getting those balls of wool?"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages0, true) {
					public void finished() {
						if(owner.getInventory().contains(new InvItem(207, 1))) {
							final String[] messages7 = {"I have some"};
							World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages7) {
								public void finished() {
									final String[] messages8 = {"Give em here then"};
									World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages8) {
										public void finished() {
											giveBall(npc, owner);
											owner.setBusy(false);
											npc.unblock();
										}
									});
								}
							});
						} else {
							final String[] messages1 = {"I haven't got any at the moment"};
							World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages1) {
								public void finished() {
									final String[] messages2 = {"Ah well at least you haven't been eaten"};
									World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages2) {
										public void finished() {
											owner.setBusy(false);
											npc.unblock();
										}
									});
								}
							});						
						}
					}
				});
			}
		} else {
			questNotStarted(npc, owner);
		}
	}
}