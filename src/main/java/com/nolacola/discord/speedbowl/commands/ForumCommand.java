package com.nolacola.discord.speedbowl.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

@CommandInfo(
    name = "ForumCommand",
    description = "Returns the forumpost for the event"
)
public class ForumCommand extends Command{
	private PropertyManager propMan = new PropertyManager();
	
	public ForumCommand() {
		this.help="Returns the forumpost for the event";
		this.name="forum";
	}


	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		event.reply(propMan.getPropertyByKey(PropertiesEnum.FORUMLINK));
	}

}
