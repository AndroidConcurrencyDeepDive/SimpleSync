/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.callmeike.android.simplesync.model;

import android.content.ContentValues;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class ContentValuesConverter
    implements JsonDeserializer<ContentValues>, JsonSerializer<ContentValues>
{
    public final List<String> fields;

    public ContentValuesConverter(String... fields) {
        this.fields = Collections.unmodifiableList(Arrays.asList(fields));
    }

    @Override
    public JsonElement serialize(ContentValues cv, Type typeOfSrc, JsonSerializationContext ctxt) {
        JsonObject json = new JsonObject();

        for (String field : fields) { json.add(field, new JsonPrimitive(cv.getAsString(field))); }

        return json;
    }

    @Override
    public ContentValues deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctxt)
        throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();

        ContentValues cv = new ContentValues();
        for (String field : fields) { cv.put(field, jsonObject.get(field).getAsString()); }

        return cv;
    }
}
