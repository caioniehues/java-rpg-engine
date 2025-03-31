# March 31st, 2025

- Upgraded to Spring AI MCP 0.8; upgraded to Spring Boot 3.4.4; upgraded to Java 24.
- Still need to update some tool calls, I think. See [migration notes](https://github.
  com/modelcontextprotocol/java-sdk/blob/main/migration-0.8.0.md).
- NEXT: take advantage of the context that gets passed in from the tools?
- Started working on a DM Journal Resource, but commented it out to figure out upgrade.
- Did some clean-up of utils, added in a test for the Config to see how tess work.

# March 23rd, 2025

- I made an MCP resource (DMJournalResource), but Claude does not seem to call it automatically. I made a sync one, 
  so maybe an async one has more interesting magic, like I can tell it to notifiy Claude (the MCP Client) that there 
  are updates. As Claude implements it, an MCP Resource is interesting, but not for a file - I could just upload 
  that myself. I suppose if it connected to a database (or other service), it would save me the trouble of fetching 
  the document and converting it. I could attack to a D&D SRD store, or something, and have it retrieve the content. 
  But if the content is just static...I'm not sure it's useful.
- For now, retrieving the DM journal should be done with a tool so I don't have to use the clunky Claude interface.
- I could just have both for an example.
- Similarly, the MCP Prompt is not very useful in Claude - it just allows the user to pass in some fields. 
- For this, I should make a promp of the init prompt that allows the user to pass in their player name.
- The idea of MC Prompt is pretty good: you could do agentic workflows of step, step 2, etc. (Though, you could just 
  build and retrieve those with a tool too...)...so you could say: here is how to create an adveture...or write a 
  script...or whatever...
- Once Claude does more with MCP Resource and Prompt, it'll be interesting.
- That said - do the async ones do something interesting?
- NEXT: clean-up and document more.
- NEXT: how the hell do I test all of this?

# March 22nd, 2025

- An MCP "prompt" shows up as [an attachment (paper clip) in the Claude desktop](https://www.reddit.
  com/r/ClaudeAI/comments/1h4eywn/mcp_is_amazing_but_i_am_still_struggling_to/). Nice of them to document that 
  anywhere.
- Of course, this is all single player, single adventure, story. You can only be playing one adventue, character, 
  etc. I suppose you could swap chatdm directories out, actually...
- Good progress. Added the ability to read in simple oracles, must a result per line. This means I can bring in 
  [these](https://github.com/saif-ellafi/play-by-the-writing/tree/main/tables).
- Still feels like it's not doing enough. ALso, I think it's just reading the result back into the context window 
  which fills it quickly. I'll need some way of breaking down big chunks of text.
- **Next:** it should have an adventure writer where it comes up with a structured adventure and saves it to a file. It 
  can then follow that instead of making it up along the way. Maybe it can have clear nodes to follow, could use my 
  old one page adventure card prompt or something.
- I want to make it so when new files are added or existing ones are modified the FileDMStoreRepository reloads them.
  For example, if a new oracle file is put in there, it should be loaded for use. That feels like a little too much 
  work now.
- Redid  the files are loaded. It's pretty stupid right now and hard coded, but it works.
- Need to fix the race condition of setting up chatdmDir. Do this by moving the bootstrapping of files into the file 
  loader, lazy checking each of oracle, journal, etc. as needed. Putting it in the ApplicationRunner init thing 
  isn't syncronized enough!

Log files!

Claude desktop keeps logs per MCP server here: ~/Library/Logs/Claude

Stack traces and errors will show up. There's a lot mroe info than the main Claude log.


# March 21st, 2025

- Re-arranged package structure into more Spring Boot like way.
- Setup config class.
- Moving things to Repository-ies.
- Next: Need to re-implement the file way of getting files and use that in the DM Journal and as a way to load Oracles.
- Next: Need some way of loading adventures?
- Next: more oracles.
- Next: can I do some RAG stuff?

# March 20, 2025

- TK the way yaml files are read in OracleReader and associated stuff seems like a mess. There must be a Spring Boot 
  like way to do it. Also, I'd like to give it the option to read from databases or whatever else.

# March 19, 2025

- Want to covert the Oracle to a Record. I think this makes loading it a lot easier to load the yaml and also write 
  it back out. I really want the controller to be able to print out the yaml, save it to a bundle, etc.
- Oracle text files: https://github.com/saif-ellafi/play-by-the-writing/tree/main/tables
- I think Josh does a full MCP Server hookup [here](https://youtu.be/cE1h-rC2o2U?si=2vrs6ga1oioQ8Nrc&t=1523).