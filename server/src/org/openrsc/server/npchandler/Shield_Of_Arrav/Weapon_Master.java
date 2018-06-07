package org.openrsc.server.npchandler.Shield_Of_Arrav;

import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;
public class Weapon_Master implements NpcHandler {
	@Override
	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		Quest phoenix = owner.getQuest(52);
		if(phoenix != null) {
			if(phoenix.finished()) {
				final String[] messages0 = {"Hello"};
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages0, true) {
					public void finished() {
						final String[] messages1 = {"Hello fellow phoenix", "What are you after?"};
						World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages1) {
							public void finished() {
								World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
									public void action() {
										final String[] options0 = {"I'm after a weapon or two", "I'm looking for treasure"};
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
														weapon(npc, owner);
														break;
													case 1:
														treasure(npc, owner);
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
			} else {
				World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hello"}, true) {
					public void finished() {
						World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Hey I don't know you", "You're not meant to be here"}) {
							public void finished() {
								owner.setBusy(false);
								npc.unblock();				
								npc.setAggressive(owner);
							}
						});		
					}
				});
			}
		} else {
			World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hello"}, true) {
				public void finished() {
					World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Hey I don't know you", "You're not meant to be here"}) {
						public void finished() {
							owner.setBusy(false);
							npc.unblock();				
							npc.setAggressive(owner);
						}
					});		
				}
			});

		}
	}

	private void weapon(final Npc npc, final Player owner) {
		final String[] messages2 = {"Sure have a look around"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages2) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}

	private void treasure(final Npc npc, final Player owner) {
		final String[] messages3 = {"We've not got any up here", "Go mug someone somewhere", "If you want treasure"};
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages3) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();
			}
		});
	}
}
