This is a collection of Model Context Controller tools, prompts, etc. for playing Dungeons and Dragons. They are implemented in Spring AI.

Currently, there are:

- [Oracles](https://github.com/cote/chatdm/tree/main/src/main/java/io/cote/chatdm/oracle) - these are tool wrappers around [YAML files](https://github.com/cote/chatdm/tree/main/src/main/resources/oracle) that can either be yes/no style solo RPG Oracles or the equivilent or random RPG tables. The Oracle service will find all the oracle YAML files in the classpath and elsewhere and auto-load them up, populating the MCP tool name, description, etc.
- [DM Journal](https://github.com/cote/chatdm/blob/main/src/main/java/io/cote/chatdm/dnd/PlayDnDTool.java) - this is a tool that write out DM notes to a markdown file and also read them back in. The AI (MCP Client) uses these to keep a lot of things to remember _and_ remember them as needed.
- [DM setup](https://github.com/cote/chatdm/blob/main/src/main/java/io/cote/chatdm/dnd/PlayDnDTool.java) - a tool used to iniatlize playing D&D. This is mostly to put in an initial prompt and point out there are other tools to use. 

(I have not written Java since 2005, so I have no idea what I'm doing with this fancy, new Spring Boot style.)
