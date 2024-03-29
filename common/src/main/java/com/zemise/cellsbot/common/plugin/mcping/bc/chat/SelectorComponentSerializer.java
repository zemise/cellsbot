package com.zemise.cellsbot.common.plugin.mcping.bc.chat;

import com.google.gson.*;
import com.zemise.cellsbot.common.plugin.mcping.bc.SelectorComponent;

import java.lang.reflect.Type;

public class SelectorComponentSerializer extends BaseComponentSerializer implements JsonSerializer<SelectorComponent>, JsonDeserializer<SelectorComponent>
{

    @Override
    public SelectorComponent deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject object = element.getAsJsonObject();
        if ( !object.has( "selector" ) )
        {
            throw new JsonParseException( "Could not parse JSON: missing 'selector' property" );
        }
        SelectorComponent component = new SelectorComponent( object.get( "selector" ).getAsString() );
        deserialize( object, component, context );
        return component;
    }

    @Override
    public JsonElement serialize(SelectorComponent component, Type type, JsonSerializationContext context)
    {
        JsonObject object = new JsonObject();
        serialize( object, component, context );
        object.addProperty( "selector", component.getSelector() );
        return object;
    }
}
