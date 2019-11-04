package com.nolacola.discord.speedbowl.commands.owner;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

public class GetPropertiesCommand extends Command{
	
	private static final Logger log = LogManager.getLogger(GetPropertiesCommand.class);

	private PropertyManager propMan = new PropertyManager();
	
	public GetPropertiesCommand() {
		this.name="props";
		this.ownerCommand = true;
		this.help="Sends you a PM with all the properties in plain text";
	}

	@Override
	protected void execute(CommandEvent event) {		
		if(!Checks.isInAdminChannel(event.getChannel())) {
			return;
		}
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		propMan.getAllProperties(writer);

		event.reactSuccess();
		
		event.replyInDm(stringWriter.toString());
		
		writer.close();
		try {
			stringWriter.close();
		} catch (IOException e) {
			log.error(e,e);
		}
	}
}
