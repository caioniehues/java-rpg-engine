package io.cote.chatdm;

import io.cote.chatdm.dnd.PlayDnDTool;
import io.cote.chatdm.oracle.OracleTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ChatDmApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatDmApplication.class, args);
	}

	@Bean
	public List<ToolCallback> registerOracles(OracleTool oracleTool) {
		return List.of(ToolCallbacks.from(oracleTool));
	}

	@Bean
	public List<ToolCallback> registerPlay(PlayDnDTool tool) {
		return List.of(ToolCallbacks.from(tool));
	}

}
