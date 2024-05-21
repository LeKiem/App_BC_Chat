/*
 * Copyright 2014 KC Ochibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *  The "‚‗‚" character is not a comma, it is the SINGLE LOW-9 QUOTATION MARK unicode 201A
 *  and unicode 2017 that are used for separating the items in a list.
 */

package hunre.it.app_bc_chat.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TinyDB {

    private SharedPreferences preferences;
    private Gson gson;

    public TinyDB(Context context) {
        preferences = context.getSharedPreferences("TinyDB", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public <T> void putListObject(String key, ArrayList<T> list) {
        SharedPreferences.Editor editor = preferences.edit();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public <T> ArrayList<T> getListObject(String key, Class<T> classType) {
        String json = preferences.getString(key, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Type type = TypeToken.getParameterized(ArrayList.class, classType).getType();
        return gson.fromJson(json, type);
    }
}
