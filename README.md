# Enterprise Railway AI Client 

POST http://localhost:8081/api/railway/ask
Content-Type: application/json

{
"question": "Where is train CSXT-7821, and list the open railway incidents."
}

{
"answer": "Train CSXT-7821 is currently in Jax. There are 2 open railway incidents: ..."
}


App: http://localhost:8081 
Health: http://localhost:8081/actuator/health  
MCP endpoint is served by Spring AI MCP server configuration (Streamable HTTP/SSE transport according to the configured starter).

