# SpeedBowl3DiscordBot
SpeedBowl 3 (SB3) is a semi annual challenge event in Elite:Dangerous (E:D) developed by
Frontier Developments (FDEV) and invented by Messrs PrimetimeCasual (PTC) and Halo Jones.
The competition lasts 48 hours, in which participants will try to achieve the highest speed
possible given a set of starting and ending parameters.

This Discord bot will help the organizer during the submission and pre-race phase of the event so that they don't have to keep track of submissions manually. The bot will also help players asking about rules, communication channels as well as current standings and validity of their submissions.

The organizer has access to the bot in a way that they can retrieve the current sets of submissions in order to validate them with the judges and to update the leaderboard accordingly.

For participants this bot acts as a helper for basic questions when no admin or other participant is around. They can access current standings, as well as the validity and ranking of their submissions.

## Setup & Configuration
* You need to fill in the hibernate.cfg.xml in src/main/resources if you want to use a database
* You need to fill settings.properties->owner with your discord Id and token with your bot's token (https://discordapp.com/developers/applications/)

## Requirements by the organizer
Features that the bot now has, sometimes in a slightly altered version

**Must have**
* Recognize submissions in the submission channel
  * Gather submission time (UTC), link, CMDR name and Discord handle
  * Order by submission time (UTC) and only recognize latest submission
  * Maximum of three submissions per Discord handle
  * Only accept submissions during the race event
* Recognize judge validation in the submission channel
  * `<@DiscordHandle> - <cmdrname> - <shiptype> - <shipname> - <"VALID"/"INVALID"> for <x> m/s at <y> km - <remark>`
* Ability to retrieve current submissions (all, latest, un-judged, judged)
* Ability to configure race event start and end
* Ability to purge current data after testing and after pre-race week
  
**Should have**
* Bot commands accessible for participants (command syntax not final)
  * !help giving list of commands
  * !rules linking to the current set of rules
  * !forum linking to the official forum thread
  * !prizes giving details about the prizes, if any
  * !leaderboard giving link or text output of current leaderboard standings
  * !announcement giving current race judge announcements
  * !mysubmissions giving status and placement of participants submissions
* Bot commands accessible for admins (command syntax not final)
  * !submissions giving current list of latest submissions as outlined above
  * !submissions <X> outlining all submissions of a specific Discord handle
  * !generate generate leaderboard in a format that can be imported to GoogleDoc
  * !announce set up an announcement

**Could have**
* Puns depending on ship type submitted
* Greet new participants
* Automatic reminder of race dates

%help displays: 
```
SpeedBot commands:

%rules - Returns the rules for the event
%forum - Returns the forumpost for the event
%announcement - Returns the current announcement
%prizes - Returns the prizes for the event
%submit <cmdrname> - <shiptype> - <shipname> - <link>[ - <freetext>] - Submit your runs!
%mySubmissions - View your submissions
%leaderboard - View the Leaderboard
%link <submissionid> - Get the videolink of a submission

Admin Commands:
%setup - Sets everything up for the race
%announce - Enter a new announcement
%props - Sends you a PM with all the properties in plain text
%log - fetches logfile
%purge - Purge DB and purgeable properties for a fresh start. Everyone needs one, once in a while.
%list <all/latest/unjudged/judged> - List database entries
%judge <submissionid> <valid/invalid> for <speed>m/s at <height>km [- <freetext>] [(bonusChallenge:)<valid/invalid>] - Judge a submission
%raw <submissionid> - Get the raw submission text for a submission
%shutdown - safely shuts off the bot
```
