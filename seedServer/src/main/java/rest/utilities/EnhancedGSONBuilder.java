package rest.utilities;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by adam on 9/14/2017.
 */
public class EnhancedGSONBuilder {
    private List<String> fieldNames;
    private List<Class<?>> classes;

    public EnhancedGSONBuilder excludeFiledNames(String ...fieldNames) {
        this.fieldNames = new ArrayList<>(Arrays.asList(fieldNames));
        return this;
    }

    public EnhancedGSONBuilder excludeClasses(Class<?> ...classes) {
        this.classes = new ArrayList<>(Arrays.asList(classes));
        return this;
    }

    public ExclusionStrategy buildExclusionStrategy() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldNames != null && fieldNames.contains(fieldAttributes.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return classes != null && classes.contains(aClass);
            }
        };
    }

    public Gson buildGSON() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        ExclusionStrategy exclusionStrategy = buildExclusionStrategy();
        gsonBuilder.setExclusionStrategies(exclusionStrategy);
        return gsonBuilder.create();
    }
}
