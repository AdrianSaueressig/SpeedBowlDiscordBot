package com.nolacola.discord.speedbowl.commands;

import org.apache.commons.lang3.StringUtils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

@CommandInfo(
    name = "AnnouncementCommand",
    description = "Returns the current announcement"
)
public class AnnouncementCommand extends Command{
	private PropertyManager propMan = new PropertyManager();
	
	public AnnouncementCommand() {
		this.help="Returns the current announcement";
		this.name="announcement";
	}


	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		String announcement = propMan.getPropertyByKey(PropertiesEnum.ANNOUNCEMENT);
		if(StringUtils.isBlank(announcement)) {
			event.reply("There are currently no announcements");
		}else {
			event.reply(announcement);
		}
	}

}
