# Marsh 22nd, 2025

- I want to make it so when new files are added or existing ones are modified the FileDMStoreRepository reloads them.
  For example, if a new oracle file is put in there, it should be loaded for use. That feels like a little too much 
  work now.
- Redid  the files are loaded. It's pretty stupid right now and hard coded, but it works.
- Need to fix the race condition of setting up chatdmDir. Do this by moving the bootstrapping of files into the file 
  loader, lazy checking each of oracle, journal, etc. as needed. Putting it in the ApplicationRunner init thing 
  isn't syncronized enough!

Log files!

Claude desktop keeps logs per MCP server here: ~/Library/Logs/Claude

Stack traces and errors will show up. There's a lot mroe info than the main CLaide log.


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