package dev.stxt.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSON 
{
	private static final ObjectMapper MAPPER = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    
	private static final ObjectMapper MAPPER_PRETTY = new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
					
	public static final String toJson(Object obj) 
	{
		try 
		{
			return MAPPER.writeValueAsString(obj);
		} 
		catch (JsonProcessingException e) 
		{
			throw new RuntimeException(e);
		}
	}

	public static final String toJsonPretty(Object obj) 
	{
		try 
		{
			return MAPPER_PRETTY.writeValueAsString(obj);
		}
		catch (JsonProcessingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
    public static JsonNode toJsonTree(String json)
    {
        try
        {
            return MAPPER.readTree(json);
        }
        catch (IOException e)
        {
            throw new RuntimeException("JSON parsing error", e);
        }
    }

}
