/**
* Generated By NPCScript :: A scripting engine created for RSCEmulation by Zilent
*/
//npc ID 487
package org.rscemulation.server.npchandler.Biohazard;
import org.rscemulation.server.event.DelayedQuestChat;
import org.rscemulation.server.event.SingleEvent;
import org.rscemulation.server.model.ChatMessage;
import org.rscemulation.server.model.InvItem;
import org.rscemulation.server.model.MenuHandler;
import org.rscemulation.server.model.Npc;
import org.rscemulation.server.model.Player;
import org.rscemulation.server.model.Quest;
import org.rscemulation.server.model.World;
import org.rscemulation.server.npchandler.NpcHandler;



public class Kilron implements NpcHandler
 {
	public void handleNpc(final Npc npc, final Player owner) throws Exception
	{
		npc.blockedBy(owner);
		owner.setBusy(true);
		
		Quest q = owner.getQuest(38);
		Quest plagueCity = owner.getQuest(35);
		
		if(q != null) 
		{
			if(q.finished()) 
			{
				owner.sendMessage("Kilron seems to be too busy to talk");
				owner.setBusy(false);
				npc.unblock();
			}
			else 
			{
				if(owner.getQuest(38) != null && owner.getQuest(38).getStage() >= 3 && owner.getQuest(38).getStage() <= 6)
				{
					switch(q.getStage())
					{
						case 3:
						case 4:
						case 5:
						case 6:
							questStage3(npc, owner);
						break;
					}
				}
				else
				{
					owner.sendMessage("Kilron seems to be too busy to talk");
					owner.setBusy(false);
					npc.unblock();
				}
			}
		} 
		else
		{
			owner.sendMessage("Kilron seems to be too busy to talk");
			owner.setBusy(false);
			npc.unblock();
		}
	}
	
	private void questStage3(final Npc npc, final Player owner) 
	{
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hello kilron"}, true) 
		{
			public void finished()
			{
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Hello traveller", "Do you need to go back over?"}) 
				{
					public void finished()
					{
						World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
						{
							public void action()
							{
								final String[] options107 = {"Not yet Kilron", "Yes I do"};
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
												World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"No problem, take your time"})
												{
													public void finished()
													{
														owner.setBusy(false);
														npc.unblock();
													}
												});
											break;
											case 1:
												wallClimb(npc, owner);
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
	
	private void wallClimb(final Npc npc, final Player owner) 
	{
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Okay, quickly now"})
		{
			public void finished()
			{
				World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
				{
					public void action()
					{
						owner.sendMessage("You climb up the rope ladder");
						World.getDelayedEventHandler().add(new SingleEvent(owner, 2000)
						{
							public void action()
							{
								owner.sendMessage("and drop down on the other side");
								owner.teleport(622, 611, false);
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