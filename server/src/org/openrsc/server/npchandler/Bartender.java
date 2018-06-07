package org.openrsc.server.npchandler;

import org.openrsc.server.model.Player;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.InvItem;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.World;
import org.openrsc.server.event.ShortEvent;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.event.DelayedQuestChat;

public class Bartender implements NpcHandler {

	private final void buyBeer(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Sure that will be 2 gold coins please"}) {
			public void finished() {
				if(owner.getInventory().remove(10, 2) != -1) {
					owner.sendInventory();
					World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Ok here you go thanks"}) {
						public void finished() {
							World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
								public void action() {
									owner.sendMessage("You buy a pint of beer");
									owner.getInventory().add(new InvItem(193, 1));
									owner.sendInventory();
									owner.setBusy(false);
									npc.unblock();
								}
							});
						}
					});
				} else {
					World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
						public void action() {
							owner.sendMessage("You do not have enough coins to buy the beer");
							owner.setBusy(false);
							npc.unblock();
						}
					});
				}
			}
		});
	}

	public void handleNpc(final Npc npc, Player player) throws Exception {
		switch(npc.getID()) {
			case 150:
				Quest q = player.getQuest(6);
				if(q == null) {
					final String[] options1 = {"Could I buy a beer please?", "Not very busy in here today is it?"};
					player.setBusy(false);
					player.sendMenu(options1);
					player.setMenuHandler(new MenuHandler(options1) {
						public void handleReply(final int option, final String reply) {
							owner.setBusy(true);
							for(Player informee : owner.getViewArea().getPlayersInView()) {
								informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
							}
							switch(option) {
								case 0:
									buyBeer(npc, owner);
									break;
								case 1:
									final String[] messages2 = {"No it was earlier", "There was a guy in here saying the goblins up by the mountain are arguing again", "Of all things about the colour of their armour.", "Knowing the goblins, it could easily turn into a full blown war", "Which wouldn't be good", "Gobin wars make such a mess of the countryside"};
									World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages2, true) {
										public void finished() {
											final String[] messages3 = {"Well if I have time I'll see if I can go knock some sense into them"};
											World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, messages3) {
												public void finished() {
													owner.addQuest(6, 5);
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
					break;
				}
			final String[] options0 = {"Could I buy a beer please?", "Have you heard any more rumours in here?"};
			player.setBusy(false);
			player.sendMenu(options0);
			player.setMenuHandler(new MenuHandler(options0) {
				public void handleReply(final int option, final String reply) {
					owner.setBusy(true);
					for(Player informee : owner.getViewArea().getPlayersInView()) {
						informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
					}
					switch(option) {
						case 0:
							buyBeer(npc, owner);
							break;
						case 1:
							final String[] messages0 = {"No it hasn't been very busy lately"};
							World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, messages0, true) {
								public void finished() {
									owner.setBusy(false);
									npc.unblock();
								}
							});
							break;
					}
				}
			});
			break;
			default:
				player.informOfNpcMessage(new ChatMessage(npc, "Would you like a beer?  Only 2 gold pieces.", player));
				player.setBusy(true);
					World.getDelayedEventHandler().add(new ShortEvent(player) {
						public void action() {
							owner.setBusy(false);
							String[] options = new String[]{"Sure", "No thanks"};
							owner.setMenuHandler(new MenuHandler(options) {
								public void handleReply(final int option, final String reply) {
									if(owner.isBusy()) {
										return;
									}
									owner.informOfChatMessage(new ChatMessage(owner, reply, npc));
									owner.setBusy(true);
									World.getDelayedEventHandler().add(new ShortEvent(owner) {
										public void action() {
											owner.setBusy(false);
											switch(option) {
												case 0:
													if(owner.getInventory().countId(10) < 2) {
														owner.informOfChatMessage(new ChatMessage(owner, "I'll just go get the cash", npc));
													}
													else if(owner.getInventory().remove(10, 2) > -1) {
														owner.getInventory().add(new InvItem(193, 1));
														owner.informOfNpcMessage(new ChatMessage(npc, "There ya' go!", owner));
														owner.sendInventory();
													}
													break;
											}
											npc.unblock();
										}
									});
								}
							});
							owner.sendMenu(options);
						}
					});
				npc.blockedBy(player);
		}
	}
	
}