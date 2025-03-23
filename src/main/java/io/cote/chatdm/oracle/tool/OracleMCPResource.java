package io.cote.chatdm.oracle.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

public class OracleMCPResource
{

//    public List<McpServerFeatures.SyncResourceRegistration> myResources() {
//        var systemInfoResource = new McpSchema.Resource(...);
//        var resourceRegistration = new McpServerFeatures.SyncResourceRegistration(systemInfoResource, request -> {
//            try {
//                var systemInfo = Map.of(...);
//                String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);
//                return new McpSchema.ReadResourceResult(
//                        List.of(new McpSchema.TextResourceContents(request.uri(), "application/json", jsonContent)));
//            }
//            catch (Exception e) {
//                throw new RuntimeException("Failed to generate system info", e);
//            }
//        });
//
//        return List.of(resourceRegistration);
//    }

}
