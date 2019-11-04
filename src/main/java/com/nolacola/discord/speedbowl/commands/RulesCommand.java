package com.nolacola.discord.speedbowl.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

@CommandInfo(
    name = "RulesCommand",
    description = "Returns the rules for the event"
)
public class RulesCommand extends Command{
	private PropertyManager propMan = new PropertyManager();
	
	public RulesCommand() {
		this.help="Returns the rules for the event";
		this.name="rules";
	}


	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInPublicChannel(event.getChannel())) {
			return;
		}
		
		event.reply(propMan.getPropertyByKey(PropertiesEnum.RULELINK)
				);
	}

}
