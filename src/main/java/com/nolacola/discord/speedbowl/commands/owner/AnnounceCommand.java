package com.nolacola.discord.speedbowl.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

@CommandInfo(
    name = "AnnounceCommand",
    description = "Enter a new announcement"
)
public class AnnounceCommand extends Command{
	private PropertyManager propMan = new PropertyManager();
	
	public AnnounceCommand() {
		this.help="Enter a new announcement";
		this.name="announce";
		this.ownerCommand=true;
	}


	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInAdminChannel(event.getChannel())) {
			return;
		}
		
		propMan.setProperty(PropertiesEnum.ANNOUNCEMENT, event.getArgs());
		event.reactSuccess();
	}

}
