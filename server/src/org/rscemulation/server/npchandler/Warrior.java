/**
* Generated By NPCScript :: A scripting engine created for RSCEmulation by Zilent
*/
package org.rscemulation.server.npchandler;

import org.rscemulation.server.event.SingleEvent;
import org.rscemulation.server.model.Npc;
import org.rscemulation.server.model.ChatMessage;
import org.rscemulation.server.model.MenuHandler;
import org.rscemulation.server.model.World;
import org.rscemulation.server.event.DelayedQuestChat;
import org.rscemulation.server.model.Player;
import org.rscemulation.server.npchandler.NpcHandler;
public class Warrior implements NpcHandler {
	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		switch(new java.util.Random().nextInt(7)) {
			case 0:
				A(npc, owner);
				break;
			case 1:
				B(npc, owner);
				break;
			case 2:
				C(npc, owner);
				break;
			case 3:
				D(npc, owner);
				break;
			case 4:
				E(npc, owner);
				break;
			case 5:
				F(npc, owner);
				break;
			case 6:
				G(npc, owner);
				break;
		}
	}

	public void A(final Npc npc, final Player owner) {
		final String[] messages15 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages15, true) {
			public void finished() {
				final String[] messages16 = {"Not too bad", "I'm a little worried about the increase in Goblins these days"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages16) {
					public void finished() {
						final String[] messages17 = {"Don't worry. I'll kill them"};
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages17) {
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

	public void B(final Npc npc, final Player owner) {
		final String[] messages0 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages0, true) {
			public void finished() {
				final String[] messages1 = {"I think we need a new king", "The one we've got isn't very good"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}

	public void C(final Npc npc, final Player owner) {
		final String[] messages0 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages0, true) {
			public void finished() {
				final String[] messages1 = {"I'm fine", "How are you?"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1) {
					public void finished() {
						final String[] messages2 = {"Very well, thank you"};
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages2) {
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

	public void D(final Npc npc, final Player owner) {
		final String[] messages0 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages0, true) {
			public void finished() {
				final String[] messages1 = {"How can I help you?"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1) {
					public void finished() {
						World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
							public void action() {
								final String[] options0 = {"Do you wish to trade?", "I'm in search of a quest", "I'm in search of enemies to kill"};
								owner.setBusy(false);
								owner.sendMenu(options0);
								owner.setMenuHandler(new MenuHandler(options0) {
									public void handleReply(final int option, final String reply) {
										owner.setBusy(true);
										for(Player informee : owner.getViewArea().getPlayersInView()) {
											informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
										}
										switch(option) {
											case 0:
												final String[] messages2 = {"No, I have nothing I wish to get rid of", "If you want to do some trading,", "there are plenty of shops and market stalls around though"};
												World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages2) {
													public void finished() {
														owner.setBusy(false);
														npc.unblock();
													}
												});
												break;
											case 1:
												final String[] messages3 = {"I'm sorry, I can't help you there"};
												World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages3) {
													public void finished() {
														owner.setBusy(false);
														npc.unblock();
													}
												});
												break;
											case 2:
												final String[] messages4 = {"I've heard there are many fearsome creatures under the ground"};
												World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages4) {
													public void finished() {
														owner.setBusy(false);
														npc.unblock();
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
		});
	}

	public void E(final Npc npc, final Player owner) {
		final String[] messages6 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages6, true) {
			public void finished() {
				final String[] messages7 = {"Get out of my way", "I'm in a hurry"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages7) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}

	public void F(final Npc npc, final Player owner) {
		final String[] messages8 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages8, true) {
			public void finished() {
				final String[] messages9 = {"Who are you?"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages9) {
					public void finished() {
						final String[] messages10 = {"I am a bold adventurer"};
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages10) {
							public void finished() {
								final String[] messages11 = {"A very noble profession"};
								World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages11) {
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
		});
	}

	public void G(final Npc npc, final Player owner) {
		final String[] messages12 = {"Hello", "How's it going?"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages12, true) {
			public void finished() {
				final String[] messages13 = {"Do I know you?"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages13) {
					public void finished() {
						final String[] messages14 = {"No, I was just wondering if you had anything interesting to say"};
						World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages14) {
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
}