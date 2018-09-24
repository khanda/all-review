package free.review.allreview.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyBeanUtil {
    /**
     * Copies properties from one object to another
     *
     * @param source source
     * @return void
     * @destination destination
     */
    public static void copyNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source));
    }

    /**
     * Returns an array of null properties of an object
     *
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set emptyNames = new HashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }

    /**
     * Retrieve a value from a property using
     *
     * @param obj          The object who's property you want to fetch
     * @param property     The property name
     * @param defaultValue A default value to be returned if either a) The property is
     *                     not found or b) if the property is found but the value is null
     * @return THe value of the property
     */
    public static <T> T getProperty(Object obj, String property, T defaultValue) {

        T returnValue = (T) getProperty(obj, property);
        if (returnValue == null) {
            returnValue = defaultValue;
        }

        return returnValue;

    }

    /**
     * Fetch a property from an object. For example of you wanted to get the foo
     * property on a bar object you would normally call {@code bar.getFoo()}. This
     * method lets you call it like {@code BeanUtil.getProperty(bar, "foo")}
     *
     * @param obj      The object who's property you want to fetch
     * @param property The property name
     * @return The value of the property or null if it does not exist.
     */
    public static Object getProperty(Object obj, String property) {
        Object returnValue = null;

        try {
            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
            Class clazz = obj.getClass();
            Method method = clazz.getMethod(methodName, null);
            returnValue = method.invoke(obj, null);
        } catch (Exception e) {
            // Do nothing, we'll return the default value
        }

        return returnValue;
    }

    public static <T> List<String> filterNotAllowChange(T entity, List<String> notAllowFieldsConfig) {
        List<String> notAllowChangeFields = new ArrayList<>();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object o = MyBeanUtil.getProperty(entity, field.getName());
            if (null != o && notAllowFieldsConfig.indexOf(field.getName()) >= 0) {
                notAllowChangeFields.add(field.getName());
            }
        }

        return notAllowChangeFields;
    }

}
