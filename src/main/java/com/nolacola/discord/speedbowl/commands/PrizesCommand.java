package com.nolacola.discord.speedbowl.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

@CommandInfo(
    name = "PrizesCommand",
    description = "Returns the prizes for the event"
)
public class PrizesCommand extends Command{
	private PropertyManager propMan = new PropertyManager();
	
	public PrizesCommand() {
		this.help="Returns the prizes for the event";
		this.name="prizes";
	}


	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		event.reply(propMan.getPropertyByKey(PropertiesEnum.PRICETEXT));
	}

}
